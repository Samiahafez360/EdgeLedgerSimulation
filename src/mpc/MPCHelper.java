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
	double SHA_ops[] = new double[6];
	boolean malicious;
	long numberofparticipations;
	long numberofcorrectlyproven;
	private Random rMaliciousbehavior;
	int myRep = 0;
	
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
		System.out.println("helper: " +id+"   "+(malicious?"malicious":"honest")+"   Staying till"+ timeInNetwork);
	}
	double getCollectiveSHA() {
		double sum = 0;
		for (int i =0; i <SHA_ops.length; i++) {
			sum += SHA_ops[i];
			
		}
		return sum;
	}
	
	double miningtime;
	boolean stayingforthisrange(long individualRange) {
		if (getCollectiveSHA()*individualRange+SimulationClock.getInstance().getTime() > timeInNetwork) return false;
		else return true;
		
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
			miningtime =  nonce * getCollectiveSHA();
		}else {
			miningtime = maxTime;
		}
		finishminingtime+= miningtime;
		//System.out.println("hello"+getMiningTime());
		return miningtime;
	}
	public double getonlyMiningTime() {
		return miningtime;
	}
	private boolean actMaliciously() {
		int val = rMaliciousbehavior.nextInt(10);
		int probX10 = (int) (10 * Double.parseDouble(SimulationProperties.getInstance().getParameter("mal_behavior_prob")));
		if (val <probX10) {
			return true;
		}else {
			//System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%   MAAAAAAAAAAAAAAAAAAALLLLLLIIICCCCCOOOOUUUUUUUSSSSS  GOT CAUGHT");
			return false;
		}
	}
	// nofops is the number of ops to be proved (already max * %)
	public boolean prove(int nofops) {
		
		System.err.println("proving"+nofops+" operations");
		boolean result = true;	
		
		if (!malicious) {
			// wait for proving time which is a memory access time
			// the proving time is already added in the caller 
			
			result= true;
		}else {
			//TODO the algorithm for improving reputation then execute the maliciousness
			//did the helper actually proved that.
			
			if (SimulationProperties.getInstance().getSimulationMode() == 1) {
				// the helper is not very smart
				result= ! actMaliciously();
				
			}else if (SimulationProperties.getInstance().getSimulationMode() == 2) {
				// the helper is smart it will act mallicously only if its reputation is max
				int mal_threshold = Integer.parseInt(SimulationProperties.getInstance().getParameter("mal_threshold")); 
				if (numberofcorrectlyproven> mal_threshold) {
					//getting caught
					result= !actMaliciously();					
				}else {
					numberofcorrectlyproven++;
					result= true;
				}
			
			}		
		}
		return result;
	}
}
