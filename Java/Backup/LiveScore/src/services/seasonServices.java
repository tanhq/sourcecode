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
import java.text.*;
import java.util.*;
import neo.score24.parseScore24;
import com.enterprisedt.net.ftp.*;


class seasonServices 
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
			NodeList participant = doc.getElementsByTagName("participant");
			NodeList primary = doc.getElementsByTagName("eventprimary");				
			if((participant==null || participant.getLength()==0)&&(primary.getLength()>0))	
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
				dateStr = dateStr.substring(0,dateStr.length()-6);
				String timeStr = dateStr.substring(dateStr.indexOf("T")+1,dateStr.length());
				dateStr = dateStr.substring(0,dateStr.indexOf("T"));				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Date dateObject = dateFormat.parse(dateStr+" "+timeStr);			
				dateObject = new Date(dateObject.getTime()+1000*60*60*7);
				dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
				StringBuffer dateBuff = new StringBuffer(dateFormat.format(dateObject));
				dateStr = dateBuff.toString();	
				eventInfo.setTime(dateStr);				
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
	public static int insertSeason(String id,String name,String date,String status,String league_id)
	{
		System.out.println("Inserting Season...");
		int n=0;
		String sqlcmd="";		
		try
		{						
			sqlcmd="select * from score24.SEASON where season_id='"+id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{				
				if(name!=null && status!=null)
				{
					sqlcmd="insert into score24.SEASON values('"+id+"','"+name+"',to_date('"+date+"','dd/MM/yyyy HH24:mi:ss'),'"+status+"','"+league_id+"')";
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
	public static int setStatus(String id, String status)
	{
		int n=0;
		String sqlcmd="";
		try
		{
			sqlcmd="update score24.SEASON set status='"+status+"' where group_id='"+id+"'";
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
			sqlcmd="update score24.SEASON set group_name_vn='"+name+"' where group_id='"+id+"'";
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
			while (true)
			{
				try				
				{
					parseScore24 parses = new parseScore24();
					//Connect to FTP SERVER			
					FTPClient ftp = new FTPClient("10.252.20.67",21);
		            //ftp.debugResponses(true);
		            ftp.login("vms","20061985");
		            System.out.println("Ket noi FTP ok!");
				    ftp.setType(FTPTransferType.BINARY);
		            ftp.setConnectMode(FTPConnectMode.PASV);					
		            ftp.chdir("/");	
		            FTPFile fileList[] = ftp.dirDetails("/");		           	 
		            int cnt=1; 
		            int cex=1;      
		            db = new DBConnection();
					db.createConnection();   
		            for (int i=0; i<fileList.length; i++)
		            {
		           		String filename = fileList[i].getName();		            
		            	File f = new File("G:/Services/Score24/resources/season/"+filename);
		            	if (!f.exists())
		            	{
		            		try
		            		{
				            	System.out.println("Copy file "+ filename+"...");
			            		ftp.get("G:/Services/Score24/resources/season/"+filename,filename);			            					            					            					            		
			            		System.out.println(cnt + " file(s) copied!\nImport to database...");
			            		cnt++;
			            		//Phan tich file XML
								try
								{								
									docBuilderFactory = DocumentBuilderFactory.newInstance();
						        	docBuilder = docBuilderFactory.newDocumentBuilder();        	        
						        	doc=docBuilder.parse(new File("G:\\Services\\Score24\\resources\\season\\"+filename));                	
						        	root=doc.getDocumentElement(); 
						        	EventInfo eventInfo = parseGroup(); 
						        	if(eventInfo!=null)
						        	{        		
						        		String id = eventInfo.getId();
						        		String name = eventInfo.getName();						        		
						        		String league_id = eventInfo.getParent();
						        		String status = eventInfo.getStatus();
						        		String time = eventInfo.getTime();
						        		if(name.trim().startsWith("20"))
						        			insertSeason(id,name,time,status,league_id);	
						        		
						        	}						        	
								}
								catch(Exception ex)
								{
									ex.printStackTrace();
								}							
							} 
	            			catch (Exception e)
	            			{
	            				System.out.println("Can not copy file "+e.getMessage());		            				
	            				
	            			}
		            	}
		            	else
		            	{
		            		System.out.println(cex+". "+f.getName()+" file already exists!");
		            		cex++;
		            	}		            	
		            }
		            db.removeConnection();            		
					try 
					{
						ftp.quit();
					} 
					catch (Exception exp)
					{
						System.out.println("FTP Connection: "+exp.getMessage());
					}
	 			}
	 			catch(Exception e)
	 			{
					e.printStackTrace();					
				}
				finally
				{
					db.removeConnection();
				}
				System.out.println((new Date())+"Completed, now sleep in 1 minutes...");
				System.out.println("......................................................................");		       
				Thread.sleep(1000*60*1);//1 minute
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
}
