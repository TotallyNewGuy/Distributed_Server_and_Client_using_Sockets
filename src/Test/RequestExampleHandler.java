package Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class RequestExampleHandler {
	
	public final Properties configProp = new Properties();

	public RequestExampleHandler() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("Test/ExampleData.properties");
		System.out.println("Read all properties from file");
		try {
		  configProp.load(in);
		} catch (IOException e) {
		  e.printStackTrace();
		}
	}

	public static class InstanceKeeper {
		private static final RequestExampleHandler INSTANCE = new RequestExampleHandler();
	}
	 
	public static RequestExampleHandler getInstance()
	   {
		  return InstanceKeeper.INSTANCE;
	   }
	    
	public String getProperty(String key){
		if (!key.equals("CLIENT_SOCKET_TIMEOUT")) {
			System.out.println("Invoke " + key);
			System.out.println("Example data are: " + configProp.getProperty(key));
			System.out.println();
		}
		return configProp.getProperty(key);
	}
	    
	public Set<String> getAllPropertyNames(){
		  return configProp.stringPropertyNames();
	   }
	    
	public boolean containsKey(String key){
		  return configProp.containsKey(key);
	   }
}

