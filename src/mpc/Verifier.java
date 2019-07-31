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
	}
	
	
	
	double finishverifyingtime;
	public ArrayList<VerificationResponse> verify(double startverifying, ArrayList<VerificationRequest> verificationRequests)
	{
		ArrayList<VerificationResponse> verificationresult = new ArrayList<VerificationResponse>(verificationRequests.size());
		int maxops =0;
		for (int i = 0; i<verificationRequests.size();i++) {
			MPCHelper helper = verificationRequests.get(i).helper;
			int nofops = verificationRequests.get(i).percentage *verificationRequests.get(i).range / 100;
			if (maxops<nofops)maxops=nofops;
			boolean verified = helper.prove(nofops);
			verificationresult.add(new VerificationResponse(helper.id, verified?1:0));
		}
		double prove_single_op = Double.parseDouble(SimulationProperties.getInstance().getParameter("single_op_proving_time"));
		finishverifyingtime = startverifying+ prove_single_op*maxops;
		
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
