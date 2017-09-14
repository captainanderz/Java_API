package properties;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



public class GetConfigProperties
{
	InputStream inputStream;
	
	public Map<String, String> GetConfigPropValues()
	{
		try 
		{
			Properties prop = new Properties();
			String propFileName = "config.properties";
			
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null)
			{
				prop.load(inputStream);
			}
			else 
			{
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			
			Map<String, String> map = new HashMap<String, String>();
			
			
			// get the property values
			map.put("login", prop.getProperty("login"));
			map.put("passwd", prop.getProperty("passwd"));
			map.put("host", prop.getProperty("host"));
			
			return map;
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			return null;
		}
	}
}
