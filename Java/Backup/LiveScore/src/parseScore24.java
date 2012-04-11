package neo.score24;

import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.transform.dom.*;
import java.io.StringWriter;
import neo.score24.object.*;

public class parseScore24 
{
	public static String path="";
	public static DocumentBuilderFactory docBuilderFactory = null;
	public static DocumentBuilder docBuilder = null;
	public static Document doc = null;
	public static Node root = null;
	
	public static MessageInfo parseMessage()
	{
		MessageInfo messageInfo = new MessageInfo();		
		try
		{		
			NodeList message = doc.getElementsByTagName("message");	
			Node messageNode = message.item(0);
			Element messageElem = (Element)messageNode;
			
			String id = messageElem.getAttribute("id");
			if(id==null) id="";
			messageInfo.setId(id);
			String result = messageElem.getAttribute("result");
			if(result==null) result="";
			messageInfo.setResult(result);
			String timezone = messageElem.getAttribute("timezone");
			if(timezone==null) timezone="";
			messageInfo.setTimeZone(timezone);
			NodeList timestamp = messageElem.getElementsByTagName("timestamp");			
			Element timestampElem = (Element)timestamp.item(0);
			NodeList timestampList = timestampElem.getChildNodes();
			String timestampStr=timestampList.item(0).getNodeValue();
			if(timestampStr==null) timestampStr="";
			messageInfo.setTimeStamp(timestampStr);      	
    	}
    	catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return messageInfo;
	}
	////////////////////////////////////////////////////////////////////////////
	public static EventInfo parseEvent()
	{
		EventInfo eventInfo = new EventInfo();
		try
		{
			NodeList event = doc.getElementsByTagName("event");	
			Node eventNode = event.item(0);
			Element eventElem = (Element)eventNode;				
			
			String id = eventElem.getAttribute("id");
			if(id==null) id = "";
			eventInfo.setId(id);
			String parent = eventElem.getAttribute("parent");
			if(parent==null) parent="";
			eventInfo.setParent(parent);
			String status = eventElem.getAttribute("status");
			if(status==null) status="";
			eventInfo.setStatus(status);
			String type = eventElem.getAttribute("type");
			if(type==null) type="";
			eventInfo.setType(type);
			
			NodeList name = eventElem.getElementsByTagName("name");
			Element nameElem = (Element)name.item(0);
			NodeList nameList = nameElem.getChildNodes();
			String nameStr = nameList.item(0).getNodeValue();
			eventInfo.setName(nameStr);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return eventInfo;	
	}
	////////////////////////////////////////////////////////////////////////////
	public static CommonInfo parseEventPrimary()
	{
		CommonInfo commonInfo = new CommonInfo();
		try
		{
			NodeList event = doc.getElementsByTagName("eventprimary");	
			Node eventNode = event.item(0);
			Element eventElem = (Element)eventNode;				
			
			String id = eventElem.getAttribute("id");
			if(id==null) id = "";
			commonInfo.setId(id);			
			NodeList name = eventElem.getElementsByTagName("name");
			Element nameElem = (Element)name.item(0);
			NodeList nameList = nameElem.getChildNodes();
			String nameStr = nameList.item(0).getNodeValue();
			commonInfo.setName(nameStr);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return commonInfo;	
	}
	////////////////////////////////////////////////////////////////////////////
	public static RefereeInfo parseReferee()
	{
		RefereeInfo refereeInfo = new RefereeInfo();
		try
		{
			NodeList referees = doc.getElementsByTagName("referees");	
			Node refereesNode = referees.item(0);
			Element refereesElem = (Element)refereesNode;	
			NodeList referee= refereesElem.getElementsByTagName("referee");
			Element refereeElem = (Element)referee.item(0);						
			
			String id = refereeElem.getAttribute("id");
			if(id==null) id = "";
			refereeInfo.setId(id);
						
			NodeList firstname = refereeElem.getElementsByTagName("firstname");
			Element firstnameElem = (Element)firstname.item(0);
			NodeList firstnameList = firstnameElem.getChildNodes();
			String firstStr = firstnameList.item(0).getNodeValue();
			refereeInfo.setFirstName(firstStr);
			
			NodeList lastname = refereeElem.getElementsByTagName("lastname");
			Element lastnameElem = (Element)lastname.item(0);
			NodeList lastnameList = lastnameElem.getChildNodes();
			String lastStr = lastnameList.item(0).getNodeValue();
			refereeInfo.setLastName(lastStr);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return refereeInfo;	
	}
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{
		try
		{			
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			
        	docBuilder = docBuilderFactory.newDocumentBuilder();
        	        	        
        	doc=docBuilder.parse(new File("D:\\Utilities\\Score24\\resources\\tmp\\scr24_81238637.xml"));                	
        	root=doc.getDocumentElement();    	
        		
        	//Doc cac tham so cua doi tuong Message
        	MessageInfo messageInfo = parseMessage(); 
        	System.out.println("ID: "+messageInfo.getId().trim());
        	System.out.println("Result: "+messageInfo.getResult().trim());
        	System.out.println("Timezone: "+messageInfo.getTimeZone());
        	System.out.println("Timestamp: "+messageInfo.getTimeStamp());
        	System.out.println("/////////////////////////////////////////////");
        	//Doc cac tham so cua doi tuong Event
        	EventInfo eventInfo = parseEvent();
        	System.out.println("ID: "+eventInfo.getId());
        	System.out.println("Parent: "+eventInfo.getParent());
        	System.out.println("Status: "+eventInfo.getParent());
        	System.out.println("Type: "+eventInfo.getType());
        	System.out.println("Name: "+eventInfo.getName());
        	System.out.println("/////////////////////////////////////////////");
        	//Doc cac tham so cua doi tuong eventprimary
        	CommonInfo commonInfo = parseEventPrimary();
        	System.out.println("ID: "+commonInfo.getId());
        	System.out.println("Name: "+commonInfo.getName());  
        	//Doc cac tham so cua doi tuong Referee
        	RefereeInfo refereeInfo = parseReferee();
        	System.out.println("ID: "+ refereeInfo.getId());
        	System.out.println("First Name: "+ refereeInfo.getFirstName());
        	System.out.println("Last Name: "+refereeInfo.getLastName());      	    	
    	}
    	catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
}
