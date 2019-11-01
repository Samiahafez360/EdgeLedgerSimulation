package mpc;

import java.util.ArrayList;
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
		System.out.println("Miner Size = "+(startingNofHelpers*(1-f)));
		System.out.println("Miner Size = "+miners.size());
		
		minersqueue = new PriorityQueue<MPCHelper>((int) (startingNofHelpers*(1-f)));
		verifiers = new ArrayList<Verifier>((int) (startingNofHelpers*f));
	}

	//Reputation table 
	ArrayList<ReputationTableEntry> reptable;
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
		// TODO Auto-generated method stub
		return 512;
	}
	public void startNetwork() {
		int i;
		for (i = 0; i< miners.size();i++) {
			miners.add(new MPCHelper(i));
			reptable.add(new ReputationTableEntry(i,0));	
		}
		
		for (i = 0; i< verifiers.size();i++) {
			verifiers.add(new Verifier(i));
			reptable.add(new ReputationTableEntry(i+miners.size()-1,0));
			
		}
		
	}
	public void mine () {
		boolean noncefound =false;
		int minerCounter=0;
		int verCounter =0;
		double startingtime =SimulationClock.getInstance().getTime();
		PriorityQueue<MPCHelper> minersinqueue = new PriorityQueue<MPCHelper>();
		while (!noncefound) {
				
			// we are assigning the ranges till the nonce is found 
			
			miners.get(minerCounter).calcMiningTime(startingtime, getIndividualRange());
			noncefound = miners.get(minerCounter).getNonceFound();
			minersinqueue.add(miners.get(minerCounter));
			//advance to the next miner
			if (minerCounter+1 == miners.size()) {
				//all are assigned ranges with no use
				//advance starting time to keep the correct timing I can't assign another 
				//range to the helper unless it finishes
				startingtime = startingtime+ miners.get(minerCounter).getMiningTime();
				SimulationClock.getInstance().advanceTime(miners.get(minerCounter).getMiningTime());
				//verify after finishing the mining 
				minerCounter = 0;
				for (int i =0; i<verifiers.size();i++)verifiers.get(i).verify(startingtime);	
			}else {
				minerCounter++;
			}
			//verifying while the ranges are done 
			//add to verification requests and at %frac assign the verification array and advance the verCounter
			
			verifiers.get(verCounter).addverificationrequest(new VerificationRequest(miners.get(minerCounter), (int)getIndividualRange(), reptable.get(minerCounter).reputation)); 
			verCounter++;
			if (verCounter% verifiers.size()==0)verCounter=0;
				
			
		}
	}
public static void main (String[] args) {
	MPCController mc = new MPCController(10);
	mc.startNetwork();
	mc.mine();
}

}

