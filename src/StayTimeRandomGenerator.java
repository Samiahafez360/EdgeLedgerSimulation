import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import org.uncommons.maths.random.ExponentialGenerator;
import org.uncommons.maths.random.MersenneTwisterRNG;

public class StayTimeRandomGenerator {

	// Needs Setting from properties file
	static long oneMinute;
	
	// Generate events at an average rate of 10 per minute poisson's lambda.
	// Needs Setting from properties file
	static long rate;
	
	static StayTimeRandomGenerator instance;
	static Random rng;
	static ExponentialGenerator helperStayTimeGen;
	Properties prop;
	
	public static StayTimeRandomGenerator getIntance() {
		if (instance == null) {
			instance = new StayTimeRandomGenerator();
		}
		return instance;
	}
	
	
	private StayTimeRandomGenerator() {
		rng = new MersenneTwisterRNG();
		prop = new Properties();
		
		InputStream input = null;

	    try {

	        input = new FileInputStream(new java.io.File( "." ).getCanonicalPath()+"\\src\\config.properties");
            prop.load(input);
            oneMinute = Long.parseLong(prop.getProperty("OneMinDuration"));
    		rate = Long.parseLong(prop.getProperty("LabdaPoisson4Stay"));
            input.close();
	    }catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	       
	    }
	        // load a properties file
	        
		
		helperStayTimeGen = new ExponentialGenerator(rate, rng);
	}
	
	
	public  double getNextStayTime() {
		return  Math.round(helperStayTimeGen.nextValue() * oneMinute);
	}
	
	public static void main(String[] args) {
		// TODO Model waiting times
		try {
			System.out.println(new java.io.File( "." ).getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int x= 100;
		while(x>0) {
			System.out.println (StayTimeRandomGenerator.getIntance().getNextStayTime());
			x--;
		}
	}
}
