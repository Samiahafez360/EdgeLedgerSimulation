package basic;

import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.uncommons.maths.random.BinomialGenerator;
import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class FindtheNonceGenerator {

		
		// probability for finding the nonce at one try 
		// Needs Setting from properties file
		static double p;
		
		static FindtheNonceGenerator instance;
		static Random rng1;
		static Random rng2;
		
		static long noftrials;
		
		static BinomialGenerator  nonceFindGen;
		static ExponentialGenerator timeToFindNonce;
		Properties prop;
		
		public static FindtheNonceGenerator getIntance(long range) {
			if (instance == null) {
				instance = new FindtheNonceGenerator(range);
			}
			return instance;
		}
		
		
		private FindtheNonceGenerator(long range) {
			rng1 = new MersenneTwisterRNG();
			rng2 = new MersenneTwisterRNG();
			
			prop = new Properties();
			
			InputStream input = null;

		    int diff = Integer.parseInt(SimulationProperties.getInstance().getParameter("Difficulty"));
	        //System.out.println((2^diff));
	        p = 1.0/(Math.pow(2,diff));
	        //System.out.println(p);
	    	noftrials =range;
	    	nonceFindGen = new BinomialGenerator((int) noftrials,p, rng1);
	        
		}
		
		
		public  int isNonceFound() {
			int found = nonceFindGen.nextValue();
			timeToFindNonce = new ExponentialGenerator(found*1.0/noftrials,rng2);
			return  found;
		}
		public double timetoNonce() {
			return timeToFindNonce.nextValue();
		}
		
		public static void main(String[] args) {
			// TODO Model waiting times
		
			int x= 100;
			while(x>0) {
				//System.out.println (FindtheNonceGenerator.getIntance(1024).isNonceFound());
				x--;
			}
		}
}
