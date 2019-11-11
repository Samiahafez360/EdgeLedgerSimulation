package mpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import basic.Controller;
import basic.SimulationProperties;

public class MPCController extends Controller {

	/*
	 * this is the MPC simulator we are using to simulate the MPC way to decompose 
	 * the SHA operation into six 
	 * operations.
	 * the helpers are separated into verifiers and miners, we need to manage a reputation entry for
	 * each helper (whether its a verifier or a miner) 
	 */
	public MPCController(int startingNofHelpers) {
		super(startingNofHelpers);
		// TODO Auto-generated constructor stub
		reptable =new ArrayList<ReputationTableEntry>(startingNofHelpers);
		float f = Float.parseFloat(SimulationProperties.getInstance().getParameter("verifier_frac"));
		
		verifier_frac =f;
		miners =  new ArrayList<MPCHelper>((int) (startingNofHelpers*(1-f)));
		System.out.println("Miner Size = "+(int) (startingNofHelpers*(1-f)));
		System.out.println("Miner Size = "+miners.size());
		
		minersqueue = new PriorityQueue<MPCHelper>((int) (startingNofHelpers*(1-f)));
		verifiers = new ArrayList<Verifier>((int) (startingNofHelpers*f));
		min_rep= Integer.parseInt(SimulationProperties.getInstance().getParameter("min_rep"));
		max_rep= Integer.parseInt(SimulationProperties.getInstance().getParameter("max_rep"));
		full_rep=Integer.parseInt(SimulationProperties.getInstance().getParameter("full_rep"));
		
	}
	int min_rep;
	int max_rep;
	int full_rep;
	//Reputation table 
	ArrayList<ReputationTableEntry> reptable;
	Map<Integer, String> reputationhistory;
	//Miners
	ArrayList<MPCHelper>miners;
	int minerssize;
	//Miners ordered by mining time 
	PriorityQueue<MPCHelper> minersqueue;
	//all verifiers
	ArrayList<Verifier> verifiers;
	int verifierssize;
	float verifier_frac;
	@Override
	public long getIndividualRange() {
		// TODO from the difficulty 
		return 1024;
	}
	public void startNetwork() {
		int i;
		for (i = 0; i<startingNofHelpers*(1-verifier_frac);i++) {
			miners.add(new MPCHelper(i));
			reptable.add(i,new ReputationTableEntry(i,min_rep));	
		}
		
		for (i = 0; i< startingNofHelpers*(verifier_frac);i++) {
			verifiers.add(new Verifier(i));
			reptable.add(i+miners.size()-1,new ReputationTableEntry(i+miners.size()-1,min_rep));
			
		}
		reputationhistory = new HashMap<Integer,String>();
		System.out.println("________network up_________");
		
	}
	public void mine () {
		System.out.println("________Let us mine_________");
		boolean noncefound =false;
		int minerCounter=0;
		int verCounter =0;
		
		int deb_trials = 1;
		double startingtime = SimulationClock.getInstance().getTime();
		System.out.println("________time = "+startingtime+"_____" );
		//PriorityQueue<MPCHelper> minersinqueue = new PriorityQueue<MPCHelper>();
		while (!noncefound) {
			// we are assigning the ranges till the nonce is found 
			if (miners.get(minerCounter).stayingforthisrange(getIndividualRange())) {
				miners.get(minerCounter).calcMiningTime(SimulationClock.getInstance().getTime(), getIndividualRange());
				noncefound = miners.get(minerCounter).getNonceFound();
				minersqueue.add(miners.get(minerCounter));
				if (noncefound) {
					System.out.println("________nonce found by "+minerCounter+"_________");
					SimulationClock.getInstance().advanceTime(miners.get(minerCounter).getonlyMiningTime());
					
				}
			}
			
			//advance to the next miner
			if (minerCounter+1 == miners.size()) {
				//all are assigned ranges with no use
				//advance starting time to keep the correct timing I can't assign another 
				//range to the helper unless it finishes
				//remove from the queue 
				
				MPCHelper lastremovedfrmqueue = minersqueue.poll();
				while( minersqueue.size()>0) {
					lastremovedfrmqueue = minersqueue.poll();
					System.out.println("hi"+ lastremovedfrmqueue.miningtime);
				}
				
				SimulationClock.getInstance().advanceTime(lastremovedfrmqueue.getonlyMiningTime());
				//verify after finishing the mining 
				minerCounter = 0;
				System.out.println("________rotate on miners _________");
				for (int i =0; i<verifiers.size();i++) {
					ArrayList<VerificationResponse> responses = verifiers.get(i).verify(SimulationClock.getInstance().getTime());	
					// update reptable based on proving
					
					for (int j = 0; j<responses.size();j++) {
						if (responses.get(j).percentageOfCorrectness< 100) {
							//MALICIOUSSSSSSS detected 
							if (reptable.get(responses.get(j).helperid).reputation > 0) {
								reptable.get(responses.get(j).helperid).reputation = 0;
								
							}
						}else {
							reptable.get(responses.get(j).helperid).reputation += calculateReputationStep(responses.get(j).helperid);
							//reptable.get(responses.get(j).helperid).reputation
							
						}
						
					}
				}
				printRepTable();
				
			}else {
				minerCounter++;
				
			}
			//verifying while the ranges are done 
			//add to verification requests and at %frac assign the verification array and advance the verCounter
			
			verifiers.get(verCounter).addverificationrequest(new VerificationRequest(miners.get(minerCounter), (int)getIndividualRange(), (full_rep- reptable.get(minerCounter).reputation)/100)); 
			verCounter++;
			if (verCounter% verifiers.size()==0) verCounter=0;
				
			deb_trials++;
		}
		System.out.println("________Finishing time = "+SimulationClock.getInstance().getTime()+"_____" );
		
	}
private double calculateReputationStep(int helperid) {
	if (reptable.get(helperid).reputation >=max_rep) //already max reputation
		return 0;
		
	else if (reptable.get(helperid).reputation < min_rep ) {  //maliciousness detected before
		return (max_rep-min_rep)/200;
		}
		
	else { 
		return (max_rep-min_rep)/50;
	}
	
	}
private void printRepTable() {
	System.out.println("*********************REPTABLE**********************");
	for (int i =0; i< reptable.size();i++ ) {
		System.out.println(reptable.get(i).helperid+"\t"+reptable.get(i).reputation);
		reputationhistory.put(reptable.get(i).helperid,reputationhistory.get(reptable.get(i).helperid)+"\n"+SimulationClock.getInstance().getTime()+"\t"+reptable.get(i).reputation);
	}
		
	}
private void printRepHistory() {
	System.out.println("*********************REP History**********************");
	for (String s : reputationhistory.values()) {
		System.out.println("\n NEWHELPER\n");
		System.out.print(s);
	}
	}
public static void main (String[] args) {
	MPCController mc = new MPCController(30);
	mc.startNetwork();
	mc.mine();
	
	System.out.println("end of block 1");
	mc.mine();
	System.out.println("end of block 2");
	mc.printRepHistory();
}

}

