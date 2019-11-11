package basic;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SimulationProperties {

	private static SimulationProperties instance;
	private Properties prop;
	public static SimulationProperties getInstance() {
		if (instance ==null) {
			instance = new SimulationProperties();
		}
		return instance;
	}
	
	private SimulationProperties() {
		prop = new Properties();
		
		InputStream input = null;

	    try {
	        input = new FileInputStream(new java.io.File( "." ).getCanonicalPath()+"\\src\\config.properties");
            prop.load(input);
            input.close();
	    }catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	       
	    }
	}
	public int getSimulationMode() {
		return Integer.parseInt(getParameter("SMode"));
	}
	public String getParameter(String parameterName) {
		
		String s= prop.getProperty(parameterName,"600000");
		//System.out.println(parameterName+"  from the properties file  "+s);
		return s;
	}
}
