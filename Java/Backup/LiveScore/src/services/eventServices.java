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
import cds.football.*;


class eventServices 
{
	public static DocumentBuilderFactory docBuilderFactory = null;
	public static DocumentBuilder docBuilder = null;
	public static Document doc = null;
	public static Node root = null;
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
	public static ParticipantInfo parseEvent()
	{		
		ParticipantInfo participantInfo = null;
		try
		{			
			ArrayList arrPlayers = new ArrayList();
			NodeList message = doc.getElementsByTagName("message");
			Element messageElem = (Element)message.item(0);
			String event_id = messageElem.getAttribute("id");			
			NodeList event = doc.getElementsByTagName("event");			
			Element eventElem = (Element)event.item(0);			
			String match_id = eventElem.getAttribute("id");			
			NodeList matchNameList = eventElem.getElementsByTagName("name");
			
			Element matchNameElem = (Element)matchNameList.item(0);			
			String match_name = "";
			if(matchNameElem!=null)
				match_name = matchNameElem.getChildNodes().item(0).getNodeValue();
			//Lay cac thanh phan annotations
			NodeList annotations = eventElem.getElementsByTagName("annotations");
			Element annotationsElem = (Element)annotations.item(0);			
			//Lay cac thanh phan Participant
			NodeList participant = eventElem.getElementsByTagName("participant");			
						
			//Thiet lap ket qua hien tai
			for(int i=0;i<participant.getLength();i++)
			{
				Node participantNode = participant.item(i);
				Element participantElem = (Element)participantNode;	
				NodeList participantSub = participantElem.getElementsByTagName("participant");									
				if(participantSub.getLength()>0)
				{		
					String tmpHome = "";
					String tmpGuest = "";		
					NodeList result = participantElem.getElementsByTagName("result");
					boolean f1 = false;
					boolean f2 = false;
					for(int j=0;j<result.getLength();j++)
					{
						Node resultNode = result.item(j);
						Element resultElem = (Element) resultNode;						
						String tmpResult = "";						
						String type = "";						
						String period = "";
						period = resultElem.getAttribute("period").trim();
						type = resultElem.getAttribute("type").trim();						
						if(type.equals("200") && (period.equals("90") || period.equals("300")))
						{
							tmpHome = resultElem.getChildNodes().item(0).getNodeValue();
							f1 = true;
						}	
						else if(type.equals("210") && (period.equals("90") || period.equals("300")))
						{
							tmpGuest = resultElem.getChildNodes().item(0).getNodeValue();
							f2 = true;
						}			
						if(f1 && f2)
							break;						
					}
					if(tmpHome.length()>0 && tmpGuest.length()>0)
					{
						setResult(match_id,tmpHome+"-"+tmpGuest);
					}				
				}
				break;
			}
			
			//Lay danh sach id cua cau thu ghi ban va cau thu bi the do
			ArrayList arrGoalID = new ArrayList();
			ArrayList arrRedID = new ArrayList();
			for(int i=0;i<participant.getLength();i++)
			{
				Node participantNode = participant.item(i);
				Element participantElem = (Element)participantNode;	
				NodeList participantSub = participantElem.getElementsByTagName("participant");									
				if(participantSub.getLength()>0)
				{		
					String tmpHome = "";
					String tmpGuest = "";		
					NodeList result = participantElem.getElementsByTagName("result");
					for(int j=0;j<result.getLength();j++)
					{
						Node resultNode = result.item(j);
						Element resultElem = (Element) resultNode;											
						String type = "";
						String subtype = "";						
						type = resultElem.getAttribute("type").trim();
						subtype = resultElem.getAttribute("type");
						if(subtype==null)
							subtype="";
						if(type.equalsIgnoreCase("400") || type.equalsIgnoreCase("401") || type.equalsIgnoreCase("440"))
						{		
							//Lay danh sach cac su kien bang thang											
							arrGoalID.add(resultElem.getAttribute("id").trim());
						}
						else if(type.equalsIgnoreCase("420") || (type.equalsIgnoreCase("410") && subtype.equalsIgnoreCase("411")))
						{
							//Lay danh sach cac su kien the do											
							arrRedID.add(resultElem.getAttribute("id").trim());
						}											
					}									
				}
			
			}
			if(arrGoalID.size()>0)
			{
				neo.score.business.Goal goal = new neo.score.business.Goal();
				goal.makeConnection();
				goal.filGoal(arrGoalID,match_id);																																			
				goal.removeConnection();
			}
			else
			{
				neo.score.business.Goal goal = new neo.score.business.Goal();
				goal.makeConnection();
				goal.deleteAllGoal(match_id);																																			
				goal.removeConnection();
			}
			
			
			//Lay ten cac cau thu
			java.util.Hashtable hsPlayers=new java.util.Hashtable();			
			for(int i=0;i<participant.getLength();i++)
			{
				Node participantNode = participant.item(i);
				Element participantElem = (Element)participantNode;
				NodeList participantSub = participantElem.getElementsByTagName("participant");					
				if(participantSub.getLength()==0)
				{
					NodeList name = participantElem.getElementsByTagName("name");
					Element nameElem = (Element)name.item(0);
					NodeList nameList = nameElem.getChildNodes();
					String nameStr = nameList.item(0).getNodeValue();
					hsPlayers.put(participantElem.getAttribute("id").trim(),nameStr);
				}			
			}
			//Lay cac su kien ghi ban, the phat va thay nguoi
			for(int i=0;i<participant.getLength();i++)
			{
				Node participantNode = participant.item(i);
				Element participantElem = (Element)participantNode;				
				
				//Lay ID cua doi bong
				String team_id = participantElem.getAttribute("id");
					
				NodeList participantSub = participantElem.getElementsByTagName("participant");		
				if(participantSub.getLength()>0)
				{
					String strPlayers = "";
					NodeList name = participantElem.getElementsByTagName("name");
					Element nameElem = (Element)name.item(0);
					NodeList nameList = nameElem.getChildNodes();
					String nameStr = nameList.item(0).getNodeValue();
					String order = participantElem.getAttribute("order");
					if(order!=null && order.length()>0)
						order = order.trim();
						
					for(int j=0;j<participantSub.getLength();j++)
					{
						Node participantSubNode = participantSub.item(j);
						Element participantSubElem = (Element)participantSubNode;
						NodeList participantSubList = participantSubElem.getElementsByTagName("participant");
						if(participantSubList.getLength()==0)
						{
							NodeList result = participantSubElem.getElementsByTagName("result");
							String status = "";	
							for(int n=0;n<result.getLength();n++)
							{
								Node resultNode = result.item(n);
								Element resultElem = (Element)resultNode;
								//String trigger = "";
								//trigger = resultElem.getAttribute("trigger");
								//if(trigger!=null && trigger.length()>0 && trigger.equalsIgnoreCase("yes"))
								//{
									String type = "";
									String subtype = "";
									type = resultElem.getAttribute("type");	
									if(type==null) type = "";
									subtype = resultElem.getAttribute("subtype");					
									NodeList resultList = resultElem.getChildNodes();
									if(resultList!=null && resultList.getLength()>0)
										status = resultList.item(0).getNodeValue().trim();
									String result_id = resultElem.getAttribute("id");
									
									String minutes = resultElem.getAttribute("time");
									
									if(minutes==null || minutes.length()==0)
										minutes="0";
									if(minutes.indexOf(":")>=0)
										minutes = minutes.substring(0,minutes.indexOf(":"));
									//Lay ten cau thu
									String playerName = "";
									String playerID = "";
									playerID=participantSubElem.getAttribute("id").trim();
									NodeList nameSubPlay = participantSubElem.getElementsByTagName("name");
									Element nameSubElem = (Element)nameSubPlay.item(0);
									NodeList nameSubList = nameSubElem.getChildNodes();
									playerName = nameSubList.item(0).getNodeValue();
									//Them bao bang Events
									String description = "";
									neo.score.business.Match match = new neo.score.business.Match();
									match.makeConnection();
									String curResult = match.getResult(match_id);
									if(curResult==null || curResult.equals("Unknown"))
										curResult="0-0";
									match.removeConnection();
								
									if(type.equals("400") || type.equals("401") || type.equals("440"))
									{															
										if(strPlayers.indexOf(result_id)<0)
										{
											strPlayers+=result_id;
											int home = 0;
											int guest = 0;
											String homeName = "";
											String guestName = "";
											String homeStatus = "";
											String guestStatus = "";
											String strNotify = "";
											if(status.length()>0)
											{
												//setResult(Integer.parseInt(minutes),result_id,match_id,status);
												String[] arrScore = status.split("-");
												home=Integer.parseInt(arrScore[0].trim());
												guest = Integer.parseInt(arrScore[1].trim());
												String[] arrName = match_name.split("-");
												homeName = arrName[0].trim();
												guestName = arrName[1].trim();
												String[] arrStatus = status.split("-");
												homeStatus = arrStatus[0].trim();
												guestStatus = arrStatus[1].trim();
												strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;												
											}
											if(playerName.trim().equals("Own Goal"))
											{
												if(nameStr.equals(homeName))
												{											
													description="Phut "+minutes+": Cau thu doi "+guestName+" da phan luoi nha, "+strNotify+".";
													playerName = "Cau thu "+guestName+" (da phan luoi nha)";
												}
												else
												{											
													description="Phut "+minutes+": Cau thu doi "+homeName+" da phan luoi nha, "+strNotify+".";
													playerName = "Cau thu "+homeName+" (da phan luoi nha)";
												}
											}
											else
											{	
												if(subtype.equalsIgnoreCase("401"))
												{						
													playerName = playerName+"(Pen.)";																	
													description="Phut "+minutes+": Ban thang! Cau thu "+playerName+" (Pen.) ("+nameStr+"), "+strNotify+".";												
												}
												else
													description="Phut "+minutes+": Ban thang! Cau thu "+playerName+" ("+nameStr+"), "+strNotify+".";												
											}	
											//Them vao bang Goals
											neo.score.business.Goal goal = new neo.score.business.Goal();
											goal.makeConnection();
											goal.insertGoal(result_id,match_id,playerID,playerName,status,Integer.parseInt(minutes),0,team_id,nameStr);																		
											goal.removeConnection();
										}
									
									}
									else if(type.equals("450"))
									{
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;												
										description="Tran dau phai phan dinh thang thua bang da Penalty! "+strNotify+".";
									}						
									else if(type.equals("410"))
									{ 
										if(subtype.equals("411"))
										{
											String homeName = "";
											String guestName = "";
											String homeStatus = "";
											String guestStatus = "";
											String strNotify = "";
											String[] arrName = match_name.split("-");
											homeName = arrName[0].trim();
											guestName = arrName[1].trim();
											String[] arrStatus = curResult.split("-");
											homeStatus = arrStatus[0].trim();
											guestStatus = arrStatus[1].trim();
											strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;												
											description="Phut "+minutes+": The do! Cau thu "+playerName+" ("+nameStr+"), "+strNotify+".";
											//Them vao bang Cards
											neo.score.business.Card card = new neo.score.business.Card();
											card.makeConnection();
											card.insertCard(result_id,match_id,playerID,playerName,Integer.parseInt(minutes),team_id,nameStr);																																			
											card.removeConnection();
										}
									}
									else if(type.equals("420"))
									{
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;												
										description="Phut "+minutes+": The do! Cau thu "+playerName+" ("+nameStr+"), "+strNotify+".";
										//Them vao bang Cards
										neo.score.business.Card card = new neo.score.business.Card();
										card.makeConnection();
										card.insertCard(result_id,match_id,playerID,playerName,Integer.parseInt(minutes),team_id,nameStr);																																			
										card.removeConnection();
									}
																																	
									//Them vao bang Events
									if(!minutes.equals("0")&&description!=null&&description.length()>0)
										insertEvents(result_id,type,description,minutes,match_id);																			
								//}		
							}						
						}
					}
				}			
			}
			neo.score.business.Match match = new neo.score.business.Match();
			match.makeConnection();
			String curResult = match.getResult(match_id);
			match.removeConnection();
			
			//Lay cac su kien ve thoi gian tran dau
			for(int i=0;i<participant.getLength();i++)
			{
				Node participantNode = participant.item(i);
				Element participantElem = (Element)participantNode;
				NodeList participantSub = participantElem.getElementsByTagName("participant");		
				if(participantSub.getLength()>0)
				{					
					//Lay cac doi tuong result	
					NodeList result = participantElem.getElementsByTagName("result");
					String status = "";	
					for(int j=0;j<result.getLength();j++)
					{
						Node resultNode = result.item(j);
						Element resultElem = (Element)resultNode;
						//String trigger = "";
						//trigger = resultElem.getAttribute("trigger");
						//if(trigger!=null && trigger.length()>0)
						//{
							String type = resultElem.getAttribute("type");						
							NodeList resultList = resultElem.getChildNodes();
							if(resultList!=null && resultList.getLength()>0)
								status = resultList.item(0).getNodeValue().trim();
							String result_id = resultElem.getAttribute("id");						
							String minutes = resultElem.getAttribute("time");
							
							if(minutes==null || minutes.length()==0)
								minutes="0";						
							
							//Them bao bang Events
							String description = "";
							
							if(type.equals("500"))
							{
								if(status.equals("1100"))
								{	
									if(isEventExit(match_id,"Tran dau bat dau")==0)
									{
										String homeName = "";
										String guestName = "";										
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();										
										strNotify = homeName+" 0 - "+guestName+" 0";												
										minutes="01";
										setMatchStatus(match_id,"1100");
										setResult(1,result_id,match_id,"0-0");	
										description = "Tran dau bat dau! "+strNotify+".";																																														
									}					
								}	
								/*else if(status.equals("1400"))
								{
									if(isEventExit(match_id,"Hiep 1 ket thuc")==0)
									{
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;												
										minutes="45";
										description = "Hiep 1 ket thuc! "+strNotify+".";																										
									}								
								}*/											
								else if(status.equals("1420"))
								{
									if(isEventExit(match_id,"Hiep 2 ket thuc")==0)
									{								
										minutes="93";
										description = "Hiep 2 ket thuc! "+match_name+" ("+curResult+")";
									}
								}
								else if(status.equals("1500"))
								{
									if(isEventExit(match_id,"Tran dau ket thuc")==0)
									{	
										
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;																			
										minutes="94";
										description = "Tran dau ket thuc! "+strNotify+".";
										setMatchStatus(match_id,status);										
																		
									}
								}
								else if(status.equals("1200"))
								{
									if(isEventExit(match_id,"Tam dung tran dau")==0)
									{	
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;																		
										description = "Tam dung tran dau! "+strNotify;								
										setMatchStatus(match_id,status);
									}
								}
								else if(status.equals("1300"))
								{
									if(isEventExit(match_id,"Tran day bi huy bo")==0)
									{	
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;																			
										description="Tran dau bi huy bo! "+strNotify;
										setMatchStatus(match_id,status);
									}
								}
								else if(status.equals("1900"))
								{
									if(isEventExit(match_id,"Bi xu thua")==0)
									{	
										String homeName = "";
										String guestName = "";
										String homeStatus = "";
										String guestStatus = "";
										String strNotify = "";
										String[] arrName = match_name.split("-");
										homeName = arrName[0].trim();
										guestName = arrName[1].trim();
										String[] arrStatus = curResult.split("-");
										homeStatus = arrStatus[0].trim();
										guestStatus = arrStatus[1].trim();
										strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;																		
										description="Bi xu thua!" +strNotify;
										setMatchStatus(match_id,status);
									}
								}
							}					
							//Them vao bang Events
							if(!minutes.equals("0")&&description!=null&&description.length()>0)
								insertEvents(result_id,type,description,minutes,match_id);									
						//}		
					}
				}
			}
			//Lay cac su kien het hiep 1, bat dau hiep 2 va ket thuc tran dau
			NodeList annotation = annotationsElem.getElementsByTagName("annotation");
			for(int i=0;i<annotation.getLength();i++)
			{
				String minutes = "0";
				String description = "";
				Element annotationNodeElem = (Element)annotation.item(i);
				String result_id = annotationNodeElem.getAttribute("resultid");
				String type = annotationNodeElem.getAttribute("type");
				NodeList text = annotationNodeElem.getElementsByTagName("text");
				Element textElem = (Element)text.item(0);
				String str = textElem.getChildNodes().item(0).getNodeValue();				
				if(str!=null && str.length()>0)
				{
					if(str.indexOf(str.indexOf("blows his whistle, and the match is underway"))>=0 || str.indexOf("starts the match")>=0)
					{
						if(isEventExit(match_id,"Tran dau bat dau")==0)
						{
							String homeName = "";
							String guestName = "";										
							String strNotify = "";
							String[] arrName = match_name.split("-");
							homeName = arrName[0].trim();
							guestName = arrName[1].trim();										
							strNotify = homeName+" 0 - "+guestName+" 0";
							minutes="01";
							setMatchStatus(match_id,"1100");
							setResult(1,result_id,match_id,"0-0");	
							description = "Tran dau bat dau! "+strNotify+".";
						}
														
					}
					/*else if(str.indexOf("brings an end to the first 45 minutes")>=0 || str.indexOf("referee blows for half-time")>=0)
					{
						if(isEventExit(match_id,"Hiep 1 ket thuc")==0)
						{	
							String homeName = "";
							String guestName = "";
							String homeStatus = "";
							String guestStatus = "";
							String strNotify = "";
							String[] arrName = match_name.split("-");
							homeName = arrName[0].trim();
							guestName = arrName[1].trim();
							String[] arrStatus = curResult.split("-");
							homeStatus = arrStatus[0].trim();
							guestStatus = arrStatus[1].trim();
							strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;																	
							minutes="45";
							description = "Hiep 1 ket thuc! "+strNotify+".";								
						}
					}
					else if(str.indexOf("referee starts the second 45 minutes")>=0 || str.indexOf("starts the second half")>=0)
					{
						if(isEventExit(match_id,"Hiep 2 bat dau")==0)
						{					
							minutes = "46";
							description = "Hiep 2 bat dau! "+match_name+" ("+curResult+")";	
						}
					}*/
					else if(str.indexOf("referee ends the second half")>=0 || str.indexOf("referee blows the final whistle and the 90 minutes are over")>=0)
					{
						if(isEventExit(match_id,"Hiep 2 ket thuc")==0)
						{
							minutes="93";
							description = "Hiep 2 ket thuc! "+match_name+" ("+curResult+")";
						}
					}
					else if(str.indexOf("players leave the field and the match is over")>=0 || str.indexOf("The players are leaving the field")>=0)
					{
						if(isEventExit(match_id,"Tran dau ket thuc")==0)
						{		
							String homeName = "";
							String guestName = "";
							String homeStatus = "";
							String guestStatus = "";
							String strNotify = "";
							String[] arrName = match_name.split("-");
							homeName = arrName[0].trim();
							guestName = arrName[1].trim();
							String[] arrStatus = curResult.split("-");
							homeStatus = arrStatus[0].trim();
							guestStatus = arrStatus[1].trim();
							strNotify = homeName+" "+homeStatus+" - "+guestName+" "+guestStatus;														
							minutes="94";
							description = "Tran dau ket thuc! "+strNotify+".";
							setMatchStatus(match_id,"1500");								
						}
					}
				}
				//Them vao bang Events
				if(!minutes.equals("0")&&description!=null&&description.length()>0)
				{	
					insertEvents(result_id,type,description,minutes,match_id);				
					
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();toError(ex.toString());
		}
		return participantInfo;
	}

	////////////////////////////////////////////////////////////////////////////
	public static int setMatchStatus(String match_id,String status)
	{
		int n=0;
		String sqlcmd="";
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="update score24.MATCHES set is_active='"+status+"' where match_id='"+match_id+"'";
			n = db.stmt.executeUpdate(sqlcmd);
			if(n>0)
				//System.out.println("Thiet lap trang thai tran dau thanh cong");
				toError("Thiet lap trang thai tran dau thanh cong");
			else
				toError("Co loi trong qua trinh thiet lap trang thai tran dau");
				//System.out.println("Co loi trong qua trinh thiet lap trang thai tran dau");
			db.putConnection();	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();			
			toError("Set Status error message: "+ex.getMessage());
			toError("Set Status erorr: "+ex.toString());
			db.putConnection();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int setResult(int intMinutes,String result_id,String match_id,String result)
	{
		int n = 0;
		String sqlcmd="";
		java.sql.ResultSet rs = null;
		boolean fValid = true;
		int pointGoal=0;
		String tmpGoal = "1";
		int oldResult = 0;
		int newResult = 0;
		String tmpHome = "";
		String tmpGuess = "";
		if(result.indexOf("-")>0)
		{
			String arr[] = result.split("-");
			if(arr.length>1)
			{
				tmpHome = arr[0].toString().trim();
				tmpGuess = arr[1].toString().trim();
				newResult = Integer.parseInt(tmpHome)+Integer.parseInt(tmpGuess );
			}
		}
		try
		{		
			db.getConnectionAndVerify();	
			sqlcmd = "select result from score24.matches where match_id='"+match_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
			{
				String tmpResult = rs.getString("result");
				if(tmpResult.length()>0 && tmpResult.indexOf("-")>0)
				{
					String arrResult[] = tmpResult.split("-");
					if(arrResult.length>1)
					{
						tmpHome = arrResult[0].trim();
						tmpGuess = arrResult[1].trim();
						oldResult = Integer.parseInt(tmpHome)+Integer.parseInt(tmpGuess );
					}
				}						
			}
			rs.close();
			if(newResult>=oldResult)
			{
				sqlcmd = "select minutes from score24.events where match_id='"+match_id+"' order by minutes desc";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
				{
					tmpGoal = rs.getString("minutes");
					if(tmpGoal.indexOf(":")>=0)
						tmpGoal = tmpGoal.substring(0,tmpGoal.indexOf(":"));
					pointGoal = Integer.parseInt(tmpGoal);
				}	
				else
					pointGoal = 0;
				rs.close();
				if(intMinutes>=pointGoal)
				{
				
					sqlcmd="Update score24.MATCHES set result='"+result+"' where match_id='"+match_id+"'";
					n = db.stmt.executeUpdate(sqlcmd);
					if(n>0)
						//System.out.println("Thiet lap ket qua tran dau thanh cong");
						toError("Thiet lap ket qua tran dau "+match_id+" thanh cong");
					else
						//System.out.println("Co loi trong qua trinh thiet lap ket qua tran dau");
						toError("Co loi trong qua trinh thiet lap ket qua tran dau "+match_id);
				}	
				else
					toError("Thoi diem cap nhat khong thoa man!");		
			}
			db.putConnection();			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			toError("Set Result1 error message: "+ex.getMessage());
			toError("Set Result1 erorr: "+ex.toString());
			db.putConnection();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int setResult(String match_id,String result)
	{
		int n = 0;
		String sqlcmd="";
		java.sql.ResultSet rs = null;
		
		try
		{	
			db.getConnectionAndVerify();			
			sqlcmd="Update score24.MATCHES set result='"+result+"' where match_id='"+match_id+"' and is_active<>'1500'";
			n = db.stmt.executeUpdate(sqlcmd);
			if(n>0)
				toError("Thiet lap ket qua tran dau "+match_id+" thanh cong");
			else
				toError("Co loi trong qua trinh thiet lap ket qua tran dau "+match_id);		
			db.putConnection();			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			toError("Set Result1 error message: "+ex.getMessage());
			db.putConnection();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int isEventExit(String match_id,String description)
	{
		int n = 0;
		String sqlcmd = "";
		try
		{
			db.getConnection();
			db.stmt = db.con.createStatement();
			//sqlcmd = "select * from score24.events where match_id='"+match_id+"' and UPPER(description) like '%"+description.trim().toUpperCase().replaceAll("'","''")+"%'";			
			sqlcmd = "select * from score24.events where match_id=? and UPPER(description) like ?";			
			//rs = db.stmt.executeQuery(sqlcmd);
			db.setPrepareStatement(sqlcmd);
			db.pstmt.setString(1,match_id);
			db.pstmt.setString(2,"%"+description.trim().toUpperCase()+"%");
			rs = db.pstmt.executeQuery();
			if(rs.next())
				n = 1;
			else
				n = 0;
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			toError("Is event exit error message: "+ex.getMessage());
			db.putConnection();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static int insertEvents(String id,String name,String description,String minutes,String matchID)
	{
		int n=0;
		String sqlcmd="";
		String goals = "";
		try
		{
			db.getConnection();
			db.stmt = db.con.createStatement();
			sqlcmd="select * from score24.MATCHES where match_id='"+matchID+"'";			
			rs=db.stmt.executeQuery(sqlcmd);
			if(rs.next())
			{
				
				sqlcmd="select * from score24.EVENTS where event_id='"+id+"'";
				rs=db.stmt.executeQuery(sqlcmd);
				if(!rs.next())
				{
					if(isEventExit(matchID,description)==0)	
					{
						sqlcmd="insert into score24.events(event_id,event_name,description,minutes,match_id,update_time) values('"+id+"','"+name+"','"+description.trim().replaceAll("'","''")+"','"+minutes+"','"+matchID+"',sysdate)";
						db.stmt = db.con.createStatement();
						n=db.stmt.executeUpdate(sqlcmd);
						if(n>0)
						{
							//Gui tin nhan den khach hang
							neo.score.client.livescoreClient client = new neo.score.client.livescoreClient();
							client.init();
							client.makeConnection();
							if(description.indexOf("Hiep 1 ket thuc")>=0 || description.indexOf("Tran dau ket thuc")>=0)
							{
								neo.score.business.Goal goal = new neo.score.business.Goal();
								goal.makeConnection();
								String tmp = goal.getListofGoals(matchID);
								goals = tmp;						
						
								if(tmp!=null && !tmp.equals("") && tmp.length()>0)
									description += " Ghi ban: "+tmp;
								goal.removeConnection();
								
								neo.score.business.Card card = new neo.score.business.Card();
								card.makeConnection();
								String tmpCard = card.getListofCards(matchID);
								if(tmpCard!=null && !tmpCard.equals("") && tmpCard.length()>0)
									description += ". The do: "+tmpCard+".";
								card.removeConnection();																						
								
							}							
							client.sendEvent(matchID,description);
							client.removeConnection();	
							if(description.indexOf("Tran dau ket thuc")>=0)
							{
								try
								{
									toError("Gui ket qua sang he thong 8x55");
									send8X55(matchID,goals);
								}
								catch(Exception ex)
								{		
								
									toError("\n"+ex.getMessage());
								}
							}					
						}
					}					
				}			
				rs.close();					
			}
			else
			{
				//System.out.println("Su kien nay khong thuoc tran dau nao hoac tran dau da ket thuc!");
				toError("Su kien nay khong thuoc tran dau nao hoac tran dau da ket thuc!");
			}
			db.putConnection();			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
			toError("Insert events Error [DBConnection]: "+ex.getMessage());
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public static void send8X55(String matchID,String goals)
	{
		try
		{
			System.out.println("Gui ket qua sang 8X55");
			toError("Gui ketqua sang 8X55");
			String sqlcmd="";
			db.getConnection();
			db.stmt = db.con.createStatement();
			sqlcmd="select a.match_name,a.result,b.league_code from matches a, leagues b where a.match_id='"+matchID+"' and a.eventprimary_id=b.league_id";	
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
			{
				String match_name = rs.getString("match_name");
				String result = rs.getString("result");
				String league_code = rs.getString("league_code");
				FootballClient con = new FootballClient("10.252.6.125:1099","cds/football");
		      	con.initConnectionToAS(); 
		      	toError("\nSend 8X55 result: "+con.updateResult(0,league_code,1,match_name,result,goals));
				//System.out.println(con.updateResult(0,league_code,1,match_name,result,goals));
			}
			rs.close();
			toError("Gui ketqua sang 8X55 THHANH CONG");
			db.putConnection();
		}
		catch(Exception ex)
		{
			toError("\nError message: "+ex.getMessage());
			ex.printStackTrace();
			db.putConnection();
		}
		finally
		{
			db.putConnection();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public static void toError(String content)
    {		   
		String path="G:/Services/Score24/log/";
		try
		{
			path+="livescore_"+sdf.format(new Date()).toString()+".txt";
			File f = new File(path);
			FileOutputStream fo = null;
			if (!f.exists())
			{
				fo = new FileOutputStream(f);		
			}
			else
			{
				fo = new FileOutputStream(f,true);
			}
			content=sdf1.format(new Date()).toString()+" "+content;
			content+="\n";
			fo.write(content.getBytes());		
		}
		catch(Exception es)
		{
			System.out.println(es.getMessage());
		}    
    }
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{		
		try 
		{	
			//while (true)
			//{
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
		            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		            int cnt=1; 
		            int cex=1; 
		            db = new DBConnection();
					db.getConnection();
					db.stmt = db.con.createStatement();
					int counter=0;        
		            for (int i=0; i<fileList.length; i++)
		            {
		           		String filename = fileList[i].getName();		            
		            	File f = new File("G:/Services/Score24/resources/events/"+filename);
		            	if (!f.exists())
		            	{
		            		counter++;
		            		if(counter<=10)
		            		{		            		
			            		try
			            		{
					            	//System.out.println("Copy file "+ filename+"...");
				            		ftp.get("G:/Services/Score24/resources/events/"+filename,filename);			            					            					            					            		
				            		//System.out.println(cnt + " file(s) copied!\nImport to database...");
				            		cnt++;
				            		//Phan tich file XML
				            		//db = new DBConnection();
				            		try
									{											
										db.getConnectionAndVerify();							
										docBuilderFactory = DocumentBuilderFactory.newInstance();
							        	docBuilder = docBuilderFactory.newDocumentBuilder();        	        
							        	doc=docBuilder.parse(new File("G:\\Services\\Score24\\resources\\events\\"+filename));                	
							        	root=doc.getDocumentElement();						        	 
							        	ParticipantInfo participantInfo = parseEvent();							        	
							        	db.putConnection();
							        	System.out.println("==========================================================");         	
							        	
									} 
									catch(Exception ex)
									{
										//db.removeConnection();
										toError(ex.getMessage());
										ex.printStackTrace();
									}
									//finally
									//{
									//	db.removeConnection();
									//}
				            		///////////////////////////
			            		} 
		            			catch (Exception e)
		            			{
		            				toError(e.getMessage());
		            				//System.out.println("Can not copy file "+e.getMessage());		            				
		            				
		            			}
		            		}
		            		else
		            			break;
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
						toError("FTP Connection: "+exp.getMessage());
					}
	 			}
	 			catch(Exception e)
	 			{
					e.printStackTrace();
					toError("Main error message: "+e.getMessage());					
				}
				finally
				{
					db.putConnection();
				}
				toError((new Date())+" Completed, now sleep in 1 minutes...");
				System.out.println((new Date())+"Completed, now sleep in 1 minutes...");
				toError("......................................................................");
				//System.out.println("......................................................................");		       
				//Thread.sleep(1000*15*1);//1 minute
				
			//}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			toError("Main error message: "+ex.getMessage());
		}
	}
}


