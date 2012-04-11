package neo.score24.services;

import neo.score24.db.DBConnection;
import neo.score24.object.*;


import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import javax.xml.transform.dom.*;
import java.io.StringWriter;


class groupServices 
{
	public static DocumentBuilderFactory docBuilderFactory = null;
	public static DocumentBuilder docBuilder = null;
	public static Document doc = null;
	public static Node root = null;
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	
	public static EventInfo parseGroup()
	{
		EventInfo eventInfo = null;
		try
		{
			NodeList annotation = doc.getElementsByTagName("annotations");
			NodeList eventprimary = doc.getElementsByTagName("eventprimary");
			if(annotation.getLength()<2&&(eventprimary==null || eventprimary.getLength()==0))	
			{
				eventInfo = new EventInfo();
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
				
				NodeList date = eventElem.getElementsByTagName("date");
				Element dateElem = (Element)date.item(0);
				NodeList dateList = dateElem.getChildNodes();
				String dateStr = dateList.item(0).getNodeValue();
				eventInfo.setTime(dateStr);	
				
				//Cap nhat danh sach cac doi thuoc bang dau
				NodeList participants = eventElem.getElementsByTagName("participant");
				if(participants.getLength()==4)
				{
					for(int i=0;i<participants.getLength();i++)
					{
						System.out.println((i+1)+"........................................");
						Node participant = participants.item(i);
						Element participantElem = (Element)participant;
						//Lay cac tham so cua doi bong
						String team_id = participantElem.getAttribute("id");
						System.out.println("ID: "+team_id);
						String order = participantElem.getAttribute("order");
						System.out.println("Order: "+order);
						String shortname = participantElem.getAttribute("shortname");
						System.out.println("Short Name: "+shortname);
						
						name = participantElem.getElementsByTagName("name");
						nameElem = (Element)name.item(0);
						nameList = nameElem.getChildNodes();
						nameStr = nameList.item(0).getNodeValue();
						System.out.println("Name: "+nameStr);
						//Inserting to Teams
						int n = insertTeams(team_id,nameStr,shortname,id);
					}
				}
			}	
			else
			{
				System.out.println("File XML khong thoa man!!!");
			}		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return 	eventInfo;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int insertGroups(String id,String name,String league_id,String status,String date)
	{
		System.out.println("Inserting Groups...");
		int n=0;
		String sqlcmd="";		
		try
		{						
			sqlcmd="select * from score24.GROUPS where group_id='"+id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{				
				if(name!=null && status!=null)
				{
					sqlcmd="insert into score24.GROUPS values('"+id+"','"+name+"','"+name+"','"+league_id+"','"+status+"','"+date+"')";
					n=db.stmt.executeUpdate(sqlcmd);
					if(n>0)
						System.out.println("Cap nhat co so du lieu thanh cong");
				}
			}
			else
			{
				System.out.println("Giai dau nay da ton tai.");
			}
			rs.close();			
		}
		catch(Exception e)
		{
			System.out.println("Error [DB-Connection]: "+e.getMessage());
		}		
		return n;       	
	}
	////////////////////////////////////////////////////////////////////////////
	public static int insertTeams(String id,String name,String shortname,String group_id)
	{
		System.out.println("Inserting Teams...");
		int n=0;
		String sqlcmd="";		
		try
		{						
			sqlcmd="select * from score24.TEAMS where TEAM_ID='"+id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{				
				if(name!=null && id!=null)
				{
					sqlcmd="insert into score24.TEAMS(team_id,team_name,team_name_vn,short_name,group_id) values('"+id+"','"+name+"','"+name+"','"+shortname+"','"+group_id+"')";
					n=db.stmt.executeUpdate(sqlcmd);
					if(n>0)
						System.out.println("Cap nhat co so du lieu thanh cong");
				}
			}
			else
			{
				System.out.println("Doi bong nay da ton tai.");
			}
			rs.close();			
		}
		catch(Exception e)
		{
			System.out.println("Error [DB-Connection]: "+e.getMessage());
		}		
		return n;       	
	}
	////////////////////////////////////////////////////////////////////////////
	public static int setStatus(String id, String status)
	{
		int n=0;
		String sqlcmd="";
		try
		{
			sqlcmd="update score24.GROUPS set status='"+status+"' where group_id='"+id+"'";
			n=db.stmt.executeUpdate(sqlcmd);
		}
		catch(Exception e)
		{
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int setName(String id, String name)
	{
		int n=0;
		String sqlcmd="";
		try
		{
			sqlcmd="update score24.GROUPS set group_name_vn='"+name+"' where group_id='"+id+"'";
			n=db.stmt.executeUpdate(sqlcmd);
		}
		catch(Exception e)
		{
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{
		try
		{
			db = new DBConnection();
			db.createConnection();
			docBuilderFactory = DocumentBuilderFactory.newInstance();
        	docBuilder = docBuilderFactory.newDocumentBuilder();        	        
        	doc=docBuilder.parse(new File("D:\\Utilities\\Score24\\resources\\tmp\\scr24_81293236.xml"));                	
        	root=doc.getDocumentElement(); 
        	EventInfo eventInfo = parseGroup(); 
        	if(eventInfo!=null)
        	{        		
        		String id = eventInfo.getId();
        		String name = eventInfo.getName();
        		String league_id = eventInfo.getParent();
        		String status = eventInfo.getStatus();
        		String time = eventInfo.getTime();
        		int n = insertGroups(id,name,league_id,status,time);	
        		
        	}
        	db.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			db.removeConnection();
		}
	}
}
