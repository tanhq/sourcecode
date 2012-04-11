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
import java.text.*;
import neo.score24.parseScore24;
import com.enterprisedt.net.ftp.*;
import java.util.*;

class roundServices 
{
	public static DocumentBuilderFactory docBuilderFactory = null;
	public static DocumentBuilder docBuilder = null;
	public static Document doc = null;
	public static Node root = null;
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	
	public static RoundInfo parseRound()
	{
		RoundInfo roundInfo = null;
		try
		{
			NodeList participant = doc.getElementsByTagName("participant");
			NodeList primary = doc.getElementsByTagName("eventprimary");				
			if((participant==null || participant.getLength()==0)&&(primary.getLength()>0))	
			{
				roundInfo = new RoundInfo();
				EventInfo eventInfo = new EventInfo();				
				
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
				System.out.println(nameStr);
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
				CommonInfo commonInfo = new CommonInfo();
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
				
				roundInfo.setEventInfo(eventInfo);
				roundInfo.setCommonInfo(commonInfo);
				roundInfo.setTime(dateStr);						
			}			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return roundInfo;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int insertRounds(String id,String name,String time,String status,String leagueID,String parentID)
	{
		System.out.println("Inserting Round ...");
		int n=0;
		String sqlcmd="";
		String name_vn = "";
		try
		{
			name_vn=name.replaceAll("Round","Vòng");
			if(name_vn==null || name_vn.length()==0)
				name_vn="Unknown";
			sqlcmd="select * from score24.ROUNDS where round_id='"+id+"'";
			System.out.println(sqlcmd);
			db.stmt=db.con.createStatement();
			rs=db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{
				sqlcmd="insert into score24.rounds values("+id+",'"+name+"',to_date('"+time+"','dd/MM/yyyy HH24:mi:ss'),'"+name_vn+"','"+status+"','"+leagueID+"','"+parentID+"','"+parentID+"')";
				System.out.println(sqlcmd);
				n=db.stmt.executeUpdate(sqlcmd);
				if(n>0)
					System.out.println("Cap nhat co so du lieu thanh cong");
			}
			else
			{
				System.out.println(rs.getString("round_name"));
				System.out.println("Vong dau nay da ton tai trong DB.");
			}
			rs.close();			
		}
		catch(Exception ex)
		{
			System.out.println("Error [DBConnection]: "+ex.getMessage());
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
		            db.getConnectionAndVerify();     
		            for (int i=0; i<fileList.length; i++)
		            {
		           		String filename = fileList[i].getName();		            
		            	File f = new File("G:/Services/Score24/resources/rounds/"+filename);
		            	if (!f.exists())
		            	{
		            		try
		            		{
				            	System.out.println("Copy file "+ filename+"...");
			            		ftp.get("G:/Services/Score24/resources/rounds/"+filename,filename);			            					            					            					            		
			            		System.out.println(cnt + " file(s) copied!\nImport to database...");
			            		cnt++;
			            		//Phan tich file XML
								try
								{									
									docBuilderFactory = DocumentBuilderFactory.newInstance();
						        	docBuilder = docBuilderFactory.newDocumentBuilder();        	        
						        	doc=docBuilder.parse(new File("G:\\Services\\Score24\\resources\\rounds\\"+filename));                	
						        	root=doc.getDocumentElement(); 
						        	RoundInfo roundInfo = parseRound(); 
						        	if(roundInfo!=null)
						        	{						        		
						        		String id = roundInfo.getEventInfo().getId();
						        		String name = roundInfo.getEventInfo().getName();
						        		
						        		String parent = roundInfo.getEventInfo().getParent();        		
						        		String date = roundInfo.getTime();
						        		String status = roundInfo.getEventInfo().getStatus();
						        		String league = roundInfo.getCommonInfo().getId();						        		
						        		if(!name.trim().startsWith("20"))
						        			insertRounds(id,name,date,status,league,parent);         							        		
						        			
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
		            	}
		            	else
		            	{
		            		//System.out.println(cex+". "+f.getName()+" file already exists!");
		            		cex++;
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


