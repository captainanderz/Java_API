package properties;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class Queries 
{
	private static final String propFileName = "queries.properties";
    private static Properties props;

    public static Properties getQueries()
    {
    		try 
    		{
	        InputStream propFileStream = Queries.class.getResourceAsStream("/" + propFileName);
	        
	        if (propFileStream == null)
	        {
	            throw new SQLException("Unable to load property file: " + propFileName);
	        }
	        
	        //singleton
	        if(props == null)
	        {
	            props = new Properties();
	            try 
	            {
	            		props.load(propFileStream);
	            } 
	            catch (IOException e) 
	            {
	                throw new SQLException("Unable to load property file: " + propFileName + "\n" + e.getMessage());
	            }           
	        }
	        
	        return props;
    		}
    		catch (Exception e) 
    		{
			return null;	
		}
    }

    public static String getQuery(String query)
    {
	    	try 
	    	{
	    		return getQueries().getProperty(query);
		}
	    	catch (Exception e) 
	    	{
			e.printStackTrace();
			return null;
		}
    }
}
