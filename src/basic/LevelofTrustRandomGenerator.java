package basic;
import java.util.Properties;
import java.util.Random;

import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class LevelofTrustRandomGenerator {
	private static LevelofTrustRandomGenerator instance;
	private static Random rLevelOfTrust ;
	private static Random rMalicious ;
	
	public static LevelofTrustRandomGenerator getIntance() {
		if (instance == null) {
			instance = new LevelofTrustRandomGenerator();
		}
		return instance;
	}
	private LevelofTrustRandomGenerator() {
		rLevelOfTrust = new Random();
		rMalicious = new MersenneTwisterRNG();
	}
	public  double getNextTrustLevel() {
		return rLevelOfTrust.nextFloat();
	}
	
	public boolean isMalicioushelper() {
		int val = rMalicious.nextInt(10);
		int probX10 = (int) (10 * Double.parseDouble(SimulationProperties.getInstance().getParameter("mal_prob")));
		if (val <probX10) {
			return true;
		}else {
			return false;
		}
	}
	
}
