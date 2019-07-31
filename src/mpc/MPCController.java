package mpc;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Vector;

import basic.Controller;
import basic.SimulationProperties;

public class MPCController extends Controller {

	public MPCController(int startingNofHelpers) {
		super(startingNofHelpers);
		// TODO Auto-generated constructor stub
		reptable =new ArrayList<ReputationTableEntry>(startingNofHelpers);
		float f = Float.parseFloat(SimulationProperties.getInstance().getParameter("verfier_frac"));
		verifier_frac =f;
		miners = new PriorityQueue<MPCHelper>((int) (startingNofHelpers*(1-f)));
		verifiers = new ArrayList<Verifier>((int) (startingNofHelpers*f));
	}

	ArrayList<ReputationTableEntry> reptable;
	ArrayList<MPCHelper>miners;
	ArrayList<Verifier> verifiers;
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
			reptable.add(new ReputationTableEntry(i,0));
			
		}
		
	}
	public void mine () {
		boolean noncefound =false;
		int minerCounter=0;
		int verCounter =0;
		int startingtime =0;
		PriorityQueue<MPCHelper> minersinqueue = new PriorityQueue<MPCHelper>();
		while (!noncefound) {
			miners.get(minerCounter).calcMiningTime(startingtime, getIndividualRange());
			noncefound = miners.get(minerCounter).getNonceFound();
			minersinqueue.add(miners.get(minerCounter));
			//add to verification requests and at %frac assign the verification array and advance the verCounter
		}
	}
}

