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
import neo.score24.parseScore24;
import com.enterprisedt.net.ftp.*;
import java.util.*;


class leagueServices 
{
	public static DocumentBuilderFactory docBuilderFactory = null;
	public static DocumentBuilder docBuilder = null;
	public static Document doc = null;
	public static Node root = null;
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	
	public static EventInfo parseLeague()
	{
		EventInfo eventInfo = null;
		try
		{
			NodeList participant = doc.getElementsByTagName("participant");	
			NodeList eventprimary = doc.getElementsByTagName("eventprimary");
			if((participant==null || participant.getLength()==0)&&(eventprimary==null || eventprimary.getLength()==0))	
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
			}			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return 	eventInfo;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int insertLeagues(String id,String name,String status,String parent)
	{
		System.out.println("Inserting Leagues...");
		int n=0;
		String sqlcmd="";		
		try
		{						
			sqlcmd="select * from score24.LEAGUES where league_id='"+id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{				
				if(name!=null && status!=null)
				{
					sqlcmd="insert into score24.LEAGUES values('"+id+"','"+name+"','"+name+"','"+status+"','2008-2009','"+parent+"',sysdate,sysdate,'Unknown',20)";
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
			sqlcmd="update score24.leagues set status='"+status+"' where league_id='"+id+"'";
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
			sqlcmd="update score24.leagues set league_name_vn='"+name+"' where league_id='"+id+"'";
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
		            	File f = new File("G:/Services/Score24/resources/leagues/"+filename);
		            	if (!f.exists())
		            	{
		            		try
		            		{
				            	System.out.println("Copy file "+ filename+"...");
			            		ftp.get("G:/Services/Score24/resources/leagues/"+filename,filename);			            					            					            					            		
			            		System.out.println(cnt + " file(s) copied!\nImport to database...");
			            		cnt++;
			            		//Phan tich file XML
								try
								{									
									docBuilderFactory = DocumentBuilderFactory.newInstance();
						        	docBuilder = docBuilderFactory.newDocumentBuilder();        	        
						        	doc=docBuilder.parse(new File("G:\\Services\\Score24\\resources\\leagues\\"+filename));                	
						        	root=doc.getDocumentElement(); 
						        	EventInfo eventInfo = parseLeague(); 
						        	if(eventInfo!=null)
						        	{						        		
						        		int n = insertLeagues(eventInfo.getId(),eventInfo.getName(),eventInfo.getStatus(),eventInfo.getParent());		
						        		n=setStatus(eventInfo.getId(),"1100");
						        		db.removeConnection();
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
