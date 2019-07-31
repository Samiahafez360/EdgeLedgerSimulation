package mpc;

import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;

import basic.FindtheNonceGenerator;
import basic.Helper;
import basic.LevelofTrustRandomGenerator;
import basic.SimulationProperties;
import basic.StayTimeRandomGenerator;

public class MPCHelper extends Helper {
	int id;
	double SHA_ops[];
	boolean malicious;
	long numberofparticipations;
	long numberofcorrectlyproven;
	private Random rMaliciousbehavior;
	
	public MPCHelper(int id) {
		this.id = id;
		timeInNetwork = StayTimeRandomGenerator.getIntance().getNextStayTime();
		
		//TODO
		//degreeoftrust = Double.parseDouble(SimulationProperties.getInstance().getParameter("InitialDegreeofTrust"));
		// the degree of trust TO be CHANGED 
		//degreeoftrust =LevelofTrustRandomGenerator.getIntance().getNextTrustLevel();
		
		degreeoftrust = 1;//Math.ceil(degreeoftrust*10)/10;
		SHA_ops[0] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op0"));
		SHA_ops[1] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op1"));
		SHA_ops[2] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op2"));
		SHA_ops[3] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op3"));
		SHA_ops[4] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op4"));
		SHA_ops[5] = Double.parseDouble(SimulationProperties.getInstance().getParameter("SHA_op5"));
		malicious = LevelofTrustRandomGenerator.getIntance().isMalicioushelper();
		rMaliciousbehavior =  new MersenneTwisterRNG();
	}
	double getCollectiveSHA() {
		double sum = 0;
		for (int i =0; i <SHA_ops.length; i++) {
			sum += SHA_ops[i];
			
		}
		return sum;
	}
	
	
	// get the probability to find the nonce, then the proper nonce (randomly).
	// multiply the nonce index by the SHA Collective time.
	@Override
	public double calcMiningTime(double startingtime, long individualRange) {
		
		maxTime = getCollectiveSHA()*individualRange;
		
		nonceProb = FindtheNonceGenerator.getIntance(individualRange).isNonceFound();
		
		int nonce = (int) (FindtheNonceGenerator.getIntance(individualRange).timetoNonce()/getCollectiveSHA());
		
		if (nonceProb>0) {
			while (nonce> individualRange) nonce = (int) (FindtheNonceGenerator.getIntance(individualRange).timetoNonce()/getCollectiveSHA());
			finishminingtime = startingtime + nonce * getCollectiveSHA();
		}else {
			finishminingtime = maxTime;
		}
		return finishminingtime;
	}
	private boolean actMaliciously() {
		int val = rMaliciousbehavior.nextInt(10);
		int probX10 = (int) (10 * Double.parseDouble(SimulationProperties.getInstance().getParameter("mal_behavior_prob")));
		if (val <probX10) {
			return true;
		}else {
			return false;
		}
	}
	public boolean prove(int nofops) {
		
			
		if (!malicious) {
			//TODO wait for proving time which is a memory access time
			return true;
		}else {
			//TODO the algorithm for improving reputation then execute the maliciousness
			//did the helper actually proved that.
			int mal_threshold = Integer.parseInt(SimulationProperties.getInstance().getParameter("mal_threshold")); 
			if (numberofcorrectlyproven> mal_threshold) {
				//getting caught
				if (actMaliciously()) return false;
				//not caught
				else return true;
				
			}else {
				numberofcorrectlyproven++;
				return true;
			}
			
		}
		
	}
}
