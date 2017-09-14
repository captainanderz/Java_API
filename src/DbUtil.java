import java.sql.*;
import java.util.Map;

import properties.GetConfigProperties;

public class DbUtil
{
	public Connection con;
    
    private final String login, passwd, host;
    
    public DbUtil() 
    {
    		GetConfigProperties getConfigProperties = new GetConfigProperties();
		Map<String, String> propValues = getConfigProperties.GetConfigPropValues();
		
		// Set login information to MySQL DB from config.properties
		this.login = propValues.get("login");
		this.passwd = propValues.get("passwd");
		this.host = propValues.get("host");
	}

    //Database.open() returns a Connection ready to use
    public void OpenConnection ()
    {
        if (!IsOpened ())
        {
	    		try 
	    		{
	        		// MySQL library
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://" + host, login, passwd); // MySQL login Url
			}
        		catch (Exception e) 
        		{
        			e.printStackTrace();
			}
        }
    }

    public void Close ()
    {
	    	try 
	    	{
			if (IsOpened ())
			{
			    con.close ();
			    con = null;
			}
		} 
	    	catch (Exception e) 
	    	{
	    		e.printStackTrace();
		}
    }

    public boolean IsOpened ()
    {
        return con != null;
    }
    
    public ResultSet ExecuteQuary(String quary)
    {
		try 
		{
			if (!IsOpened()) 
			{
				OpenConnection();
			}
			
			return con.createStatement().executeQuery(quary);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
    
    public void Execute(String quary) 
    {
    		try 
		{
    			if (!IsOpened()) 
    			{
    				OpenConnection();
    			}
    			
    			con.createStatement().execute(quary);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
    
    public PreparedStatement PreparedStatement(String quary) 
    {
		try 
		{
			if (!IsOpened()) 
			{
				OpenConnection();
			}
			
			return con.prepareStatement(quary);
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
    
    public PreparedStatement StatementSetString(PreparedStatement stm, int index, String value) 
    {
		try 
		{
			if (!IsOpened()) 
			{
				OpenConnection();
			}
			
			stm.setString(index, value);
			return stm;
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
    
    public PreparedStatement StatementSetInt(PreparedStatement stm, int index, int value) 
    {
		try 
		{
			if (!IsOpened()) 
			{
				OpenConnection();
			}
			
			stm.setInt(index, value);
			return stm;
		} 
		catch (Exception e) 
		{
			return null;
		}
	}
}
