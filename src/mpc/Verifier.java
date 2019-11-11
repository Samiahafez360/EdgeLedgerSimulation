package mpc;

import java.util.ArrayList;

import basic.Helper;
import basic.LevelofTrustRandomGenerator;
import basic.SimulationProperties;

public class Verifier implements Comparable<Verifier>{
	int id;
	boolean malicious;
	
	public Verifier(int id) {
		this.id=id;
		
		malicious = LevelofTrustRandomGenerator.getIntance().isMalicioushelper();	
		verificationRequests = new ArrayList<VerificationRequest>();
	}
	
	
	ArrayList<VerificationRequest> verificationRequests;
	double finishverifyingtime;
	public void addverificationrequest(VerificationRequest r) {
		verificationRequests.add(r);
	}
	public ArrayList<VerificationResponse> verify(double startverifying)
	{
		System.out.println("***************Verifier "+id +" is verifying");
		
		
		ArrayList<VerificationResponse> verificationresult = new ArrayList<VerificationResponse>(verificationRequests.size());
		
		double prove_single_op = Double.parseDouble(SimulationProperties.getInstance().getParameter("single_op_proving_time"));
		
		int maxops =0;
		for (int i = 0; i<verificationRequests.size();i++) {
			MPCHelper helper = verificationRequests.get(i).helper;
			int nofops =(int) Math.ceil( verificationRequests.get(i).percentage *verificationRequests.get(i).range / 100);
			System.out.println ("Helper"+ helper.id+" operations are being verified only "+verificationRequests.get(i).percentage+"% of the operations are verified");
			if (maxops<nofops)maxops=nofops;
			boolean verified = helper.prove(nofops);
			verificationresult.add(new VerificationResponse(helper.id, verified?100:0));
			verificationRequests.remove(i);
		}
		finishverifyingtime = startverifying+ prove_single_op*maxops;
		
		SimulationClock.getInstance().advanceTime(prove_single_op*maxops);
		return verificationresult;
	}
	@Override
	public int compareTo(Verifier arg0) {
		// TODO Auto-generated method stub
		if (this.finishverifyingtime< arg0.finishverifyingtime) return -1;
		else if (this.finishverifyingtime> arg0.finishverifyingtime) return 1;
		else return 0;
	}
	
}
