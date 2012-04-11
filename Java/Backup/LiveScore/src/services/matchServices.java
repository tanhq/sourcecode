package neo.score24.services;


import neo.score.db.DBConnection;
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
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.text.*;

class matchServices 
{
	public static DocumentBuilderFactory docBuilderFactory = null;
	public static DocumentBuilder docBuilder = null;
	public static Document doc = null;
	public static Node root = null;
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	
	public static MatchInfo parseMatch()
	{
		MatchInfo matchInfo = null;
		try
		{
			NodeList participant = doc.getElementsByTagName("participant");			
			NodeList primary = doc.getElementsByTagName("eventprimary");			
			NodeList annotation = doc.getElementsByTagName("annotations");
			if((annotation.getLength()<2)&&(primary.getLength()>0)&&(participant.getLength()==2))	
			{
				matchInfo = new MatchInfo();
				EventInfo eventInfo = new EventInfo();
				CommonInfo commonInfo = new CommonInfo();
				ParticipantInfo participantInfo1 = new ParticipantInfo();
				ParticipantInfo participantInfo2 = new ParticipantInfo();				
				
				//Lay cac thong so cua doi tuong EventInfo
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
				
				//Lay tham so date
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
				System.out.println("Cac tham so cua doi tuong EventInfo");
				eventInfo.display();			
							
								
				//Lay cac thong so vong dau					
				NodeList eventprimary = doc.getElementsByTagName("eventprimary");
				Node eventprimaryNode = eventprimary.item(0);
				Element eventprimaryElem = (Element)eventprimaryNode;
				
				id = eventprimaryElem.getAttribute("id");
				if(id==null) id = "";
				commonInfo.setId(id);
				
				name = eventprimaryElem.getElementsByTagName("name");
				nameElem = (Element)name.item(0);
				nameList = nameElem.getChildNodes();
				nameStr = nameList.item(0).getNodeValue();
				commonInfo.setName(nameStr);	
				System.out.println("Cac tham so cua doi tuong CommonInfo");
				commonInfo.display();
				
				//Lay cac tham so cua hai doi
				for(int i=0;i<participant.getLength();i++)
				{
					Node participantNode = participant.item(i);
					Element participantElem = (Element)participantNode;
					String orderMath = participantElem.getAttribute("order");
					String team_id = participantElem.getAttribute("id").trim();					
						
					name = participantElem.getElementsByTagName("name");
					nameElem = (Element)name.item(0);
					nameList = nameElem.getChildNodes();
					nameStr = nameList.item(0).getNodeValue();
					String shortname = participantElem.getAttribute("shortname");
					if(shortname==null||shortname.length()==0)
						shortname=nameStr;
					groupServices client = new groupServices();
					//client.db = new DBConnection();
					//client.db.createConnection();
					//client.insertTeams(team_id,nameStr,shortname,participantElem.getAttribute("id").toString().trim());
					//client.db.removeConnection();
					if(Integer.parseInt(orderMath)==1)
					{
						participantInfo1.setId(participantElem.getAttribute("id").trim());					
					}
					else if(Integer.parseInt(orderMath)==2)	
					{
						participantInfo2.setId(participantElem.getAttribute("id").trim());
					}	
				}				
				matchInfo.setEvent(eventInfo);
				matchInfo.setEventPrimary(commonInfo);
				matchInfo.setParticipant1(participantInfo1);
				matchInfo.setParticipant2(participantInfo2);
				matchInfo.setMatchDate(dateStr);
			}
			else
			{
				System.out.println("Cau truc file XML khong thoa man!!!");
			}			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return matchInfo;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int insertMatches(String match_id,String match_name,String home_id,String guest_id,String status,String match_date,String parent,String roundId)
	{
		System.out.println("Inserting Match ...");
		int n=0;
		String sqlcmd="";
		try
		{
			sqlcmd="select * from score24.MATCHES where match_id='"+match_id+"'";
			System.out.println(sqlcmd);
			rs=db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{
				sqlcmd="insert into score24.MATCHES(match_id,match_name,home_id,guest_id,is_active,match_date,parent_id,eventprimary_id) values('"+match_id+"','"+match_name+"','"+home_id+"','"+guest_id+"','"+status+"',to_date('"+match_date.trim()+"','dd/MM/yyyy HH24:mi:ss'),'"+parent+"','"+roundId+"')";
				n=db.stmt.executeUpdate(sqlcmd);				
			}
			else
			{
				sqlcmd = "update score24.MATCHES set match_name ='"+match_name+"',match_date=to_date('"+match_date.trim()+"','dd/MM/yyyy HH24:mi:ss') where match_id='"+match_id+"'";
				n=db.stmt.executeUpdate(sqlcmd);

			}
			if(n>0)
					System.out.println("Cap nhat co so du lieu thanh cong");
			rs.close();			
		}
		catch(Exception ex)
		{
			System.out.println("Error [DBConnection]: "+ex.getMessage());
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static void setStatus(String match_id,String status)
	{
		int n=0;
		String sqlcmd="";
		try
		{
			sqlcmd="Update MATCHES set is_active='"+status+"' where match_id='"+match_id+"'";
			n = db.stmt.executeUpdate(sqlcmd);
			if(n>0)
				System.out.println("Thiet lap trang thai tran dau thanh cong");
			else
				System.out.println("Co loi trong qua trinh thiet lap trang thai tran dau");
		}
		catch(Exception ex)
		{
			System.out.println("Error [Math Services]: "+ex.getMessage());
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public static void setResult(String match_id,String result)
	{
		int n = 0;
		String sqlcmd="";
		try
		{
			sqlcmd="Update MATCHES set result='"+result+"' where match_id='"+match_id+"'";
			n = db.stmt.executeUpdate(sqlcmd);
			if(n>0)
				System.out.println("Thiet lap ket qua tran dau thanh cong");
			else
				System.out.println("Co loi trong qua trinh thiet lap ket qua tran dau");
		}
		catch(Exception ex)
		{
			System.out.println("Error [Math Services]: "+ex.getMessage());
		}
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
					db.getConnectionAndVerify();  
					int counter=0;       
		            for (int i=0; i<fileList.length; i++)
		            {
		           		String filename = fileList[i].getName();		            
		            	File f = new File("G:/Services/Score24/resources/matchs/"+filename);
		            	if (!f.exists())
		            	{
		            		counter++;
		            		//if(counter<=10)
		            		//{		          
			            		try
			            		{
					            	System.out.println("Copy file "+ filename+"...");
				            		ftp.get("G:/Services/Score24/resources/matchs/"+filename,filename);			            					            					            					            		
				            		System.out.println(cnt + " file(s) copied!\nImport to database...");
				            		cnt++;
				            		//Phan tich file XML
				            		try
									{
									
										docBuilderFactory = DocumentBuilderFactory.newInstance();
							        	docBuilder = docBuilderFactory.newDocumentBuilder();        	        
							        	doc=docBuilder.parse(new File("G:\\Services\\Score24\\resources\\matchs\\"+filename));                	
							        	root=doc.getDocumentElement();        	
							        	MatchInfo matchInfo = parseMatch(); 
							        	if(matchInfo!=null&&matchInfo.getParticipant1().getId()!=null&&matchInfo.getParticipant2().getId()!=null)
							        	{        		
							        		String eventid = matchInfo.getEvent().getId();
							        		String matchName = matchInfo.getEvent().getName();
							        		String parent = matchInfo.getEvent().getParent();   
							        		String eventStatus = matchInfo.getEvent().getStatus();
							        		
							        		String roundId = matchInfo.getEventPrimary().getId();
							        		String dateStr = matchInfo.getMatchDate();
							        		String home_id = matchInfo.getParticipant1().getId();        		
							        		String guest_id = matchInfo.getParticipant2().getId();       		
							        		int n = insertMatches(eventid,matchName,home_id,guest_id,eventStatus,dateStr,parent,roundId);         			
							        			
							        	}						        	
									}
									catch(Exception ex)
									{
										ex.printStackTrace();
									}								
				            		///////////////////////////
			            		} 
		            			catch (Exception e)
		            			{
		            				System.out.println("Can not copy file "+e.getMessage());		            					            				
		            			}		            			
		            		//}
		            		//else
		            		//	break;
		            	}
		            	else
		            	{
		            		
		            	}		            	
		            } 
		            db.putConnection();           		
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
					db.putConnection();
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


