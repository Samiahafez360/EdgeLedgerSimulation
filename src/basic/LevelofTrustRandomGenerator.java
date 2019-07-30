package basic;
import java.util.Properties;
import java.util.Random;

import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class LevelofTrustRandomGenerator {
	private static LevelofTrustRandomGenerator instance;
	private static Random r = new Random();
	
	public static LevelofTrustRandomGenerator getIntance() {
		if (instance == null) {
			instance = new LevelofTrustRandomGenerator();
		}
		return instance;
	}
	private LevelofTrustRandomGenerator() {
		Random r = new Random();
	}
	public  double getNextTrustLevel() {
		return r.nextFloat();
	}
	
}
