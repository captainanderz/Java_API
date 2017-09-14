import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.lang.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.tribes.util.StringManager;

import properties.GetConfigProperties;
import properties.Parameters;
import properties.Queries;
import properties.QueryNames;

/**
 * Servlet implementation class xml
 */
// Real address localhost:PORT/Java_II_Web/xml
@WebServlet("/xml")
public class xml extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	private StringBuilder StringBuilderXml;
	private DbUtil DbUtil = new DbUtil();
	
	public xml()
	{
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		StringBuilderXml = StringBuilderForXML();
		PrintWriter resWriter = response.getWriter();
		PreparedStatement stm = null;
		
		// Get parameter values
		String id =       GetParamValOrDefault(request.getParameterValues(Parameters.FilterId));
		String maxPrice = GetParamValOrDefault(request.getParameterValues(Parameters.FilterMaxPrice));
		String minPrice = GetParamValOrDefault(request.getParameterValues(Parameters.FilterMinPrice));
		String dateSort = GetParamValOrDefault(request.getParameterValues(Parameters.SortByDate));
		
		if (id != "")
		{
			// Return XML
			stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.SelectId));
			stm = DbUtil.StatementSetString(stm, 1, id);
			
			if (stm != null) 
			{
				getTableData(stm);
			}
		}
		
		if (maxPrice != "") 
		{
			stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.SelectPriceHigher));
			stm = DbUtil.StatementSetString(stm, 1, maxPrice);
			getTableData(stm);
		}
		
		if (minPrice != "") 
		{
			stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.SelectPriceLower));
			stm = DbUtil.StatementSetString(stm, 1, minPrice);
			getTableData(stm);
		}
		
		if(dateSort != "")
		{
			switch (dateSort) {
			case "asc":
				stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.SelectOrderAsc));
				getTableData(stm);
				break;
			case "desc":
				stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.SelectOrderDesc));
				getTableData(stm);
				break;
			default:
				break;
			}
		}
		
		// Default. If no parameter is sent
		if(!StringBuilderXml.toString().contains("<products>"))
		{
			// Return XML
			stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.SelectAll));
			getTableData(stm);
		}
		
		resWriter.append(StringBuilderXml);
		
		// Close connection to DB
		DbUtil.Close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// Open connection
		DbUtil.OpenConnection();
		String name = GetParamValOrDefault(request.getParameterValues(Parameters.Name));
		String price = GetParamValOrDefault(request.getParameterValues(Parameters.Price));
		
		if (name != "" || price != "") 
		{
			PostProduct(name, price);
		}
		
		doGet(request, response);
	}
	
	// Get product data from MySQL DB
	public String getTableData(PreparedStatement searchQuery)
	{
		ResultSet resultSet = null;
		
	       try 
	       {
	           if (!DbUtil.IsOpened()) 
	           {
	        	   	   DbUtil.OpenConnection(); // Open connection
	           }
	           
	           resultSet = searchQuery.executeQuery(); // Query request result
	           
	           // Get table column count
	           ResultSetMetaData rsmd = resultSet.getMetaData();
	           int colCount = rsmd.getColumnCount();
	           
	           // Write XML table
	           StringBuilderXml.append("<products>");
	           while (resultSet.next()) 
	           {
	        	       StringBuilderXml.append("<product>");
	               for (int i = 1; i <= colCount; i++) 
	               {
	                   String columnName = rsmd.getColumnName(i);
	                   StringBuilderXml.append(XmlHelper.WriteXmlTag(columnName, resultSet.getObject(i)));
	               }
	               
	               StringBuilderXml.append("</product>");
	           }
	           
	           StringBuilderXml.append("</products>");
	           
	           // Close connections
	           DbUtil.Close();
	           
	           return StringBuilderXml.toString();
	       } 
	       catch (Exception e) 
	       {
	           e.printStackTrace();
	           
	           return "FAILED: " + e.getMessage();
	       }
	   }
	
	public void PostProduct(String name, String price) 
	{
		PreparedStatement stm = null;
		
		try
		{
			if (!DbUtil.IsOpened()) 
			{
				DbUtil.OpenConnection();
			}
			
			stm = DbUtil.PreparedStatement(Queries.getQuery(QueryNames.InsertNamePrice));
			stm = DbUtil.StatementSetString(stm, 1, name);
			stm = DbUtil.StatementSetInt(stm, 2, Integer.parseInt(price));

			stm.execute();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static StringBuilder StringBuilderForXML() 
	{
		StringBuilder writer = new StringBuilder();
		
		// Set returning text to be XML
		writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		return writer;
	}

	public String GetParamValOrDefault(String[] s)
	{	
		return s != null ? s[0] : "";
	}
}
