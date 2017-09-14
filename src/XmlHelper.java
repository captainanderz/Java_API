
public class XmlHelper 
{
	public static String WriteXmlTag(String tagName, Object object) 
	{
		StringBuilder writer = new StringBuilder();
		
		writer.append('<').append(tagName).append('>');  // Open tag
        writer.append(object);				  			// Value
        writer.append("</").append(tagName).append('>'); // Close tag
		
		return writer.toString();
	}

}
