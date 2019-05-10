import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.uncommons.maths.random.BinomialGenerator;

import org.uncommons.maths.random.MersenneTwisterRNG;

public class FindtheNonceGenerator {

		
		// probability for finding the nonce at one try 
		// Needs Setting from properties file
		static double p;
		
		static FindtheNonceGenerator instance;
		static Random rng;
		static int noftrials;
		
		static BinomialGenerator  nonceFindGen;
		Properties prop;
		
		public static FindtheNonceGenerator getIntance() {
			if (instance == null) {
				instance = new FindtheNonceGenerator();
			}
			return instance;
		}
		
		
		private FindtheNonceGenerator() {
			rng = new MersenneTwisterRNG();
			prop = new Properties();
			
			InputStream input = null;

		    try {

		        input = new FileInputStream(new java.io.File( "." ).getCanonicalPath()+"\\src\\config.properties");
	            prop.load(input);
	            int diff = Integer.parseInt(prop.getProperty("Difficulty"));
	            System.out.println((2^diff));
	            p = 1.0/(Math.pow(2,diff));
	            System.out.println(p);
	    		noftrials =Integer.parseInt(prop.getProperty("Range"));
	            input.close();
		    }catch (IOException ex) {
		        ex.printStackTrace();
		    } finally {
		       
		    }
		        // load a properties file
		        
			
		    nonceFindGen = new BinomialGenerator(noftrials,p, rng);
		}
		
		
		public  int isNonceFound() {
			return nonceFindGen.nextValue() ;
		}
		
		public static void main(String[] args) {
			// TODO Model waiting times
		
			int x= 100;
			while(x>0) {
				System.out.println (FindtheNonceGenerator.getIntance().isNonceFound());
				x--;
			}
		}
}
