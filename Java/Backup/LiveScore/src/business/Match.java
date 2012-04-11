package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import neo.score24.object.MatchInfo;
import java.io.*;
import java.util.*;
import neo.score.client.*;
import vms.ingate.*;

public class Match 
{
	public static DBConnection db = null;
	public static ResultSet rs = null;
	public static String sqlcmd = "";
	public Match()
	{
				
	}
	public void makeConnection()
	{
		try
		{
			if(db==null) db = new DBConnection();
			//db.getConnectionAndVerify();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void removeConnection()
	{
		try
		{
			if(db!=null)
				db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public int insertMatch(MatchInfo match)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "insert into score24.matches values(match_id_seq.nextval,'"+match.getMatchName()+"','"+match.getHomeID()+"','"+match.getGuestID()+"','"+match.getIsActive()+"',to_date('"+match.getMatchDate()+"','dd/MM/yyyy'),'"+match.getReferee()+"','"+match.getStadium()+"','"+match.getParentID()+"','"+match.getEventPrimaryID()+"','"+match.getMatchSequence()+"')";
			response = db.stmt.executeUpdate(sqlcmd);
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////
	public int deleteMatch(String match_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="delete from score24.matches where match_id='"+match_id+"'";
			response = db.stmt.executeUpdate(sqlcmd);
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////
	public int updateMatch(MatchInfo match)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "update score24.matches set match_name='"+match.getMatchName()+"',home_id='"+match.getHomeID()+"',guest_id='"+match.getGuestID()+"',is_active='"+match.getIsActive()+"',match_date=to_date('"+match.getMatchDate()+"','dd/MM/yyyy'),referee='"+match.getReferee()+"',stadium='"+match.getStadium()+"',parent_id='"+match.getParentID()+"',eventprimary_id='"+match.getEventPrimaryID()+"',match_seq='"+match.getMatchSequence()+"' where match_id='"+match.getMatchID()+"'";
			response = db.stmt.executeUpdate(sqlcmd);
			db.putConnection();			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	/////////////////////////////////////////////////////////////////////////////
	public int updateSEQ(String match_id,String is_active,int match_seq)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "update score24.matches set is_active='"+is_active+"', match_seq="+match_seq+" where match_id='"+match_id+"'";
			response = db.stmt.executeUpdate(sqlcmd);			
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	/////////////////////////////////////////////////////////////////////////////
	public MatchInfo getMatch(String match_id)
	{
		MatchInfo match = new MatchInfo();
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "select * from score24.matches where match_id='"+match_id+"'";			
			rs = db.stmt.executeQuery(sqlcmd);				
			if(rs.next())
			{							
				match.setEventPrimaryID(rs.getString("eventprimary_id"));				
				match.setParentID(rs.getString("parent_id"));
				match.setMatchID(match_id);
				match.setMatchDate(rs.getString("match_date"));	
				match.setHomeID(rs.getString("home_id"));
				match.setGuestID(rs.getString("guest_id"));
			}
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return match;
	}
	////////////////////////////////////////////////////////////////////////////
	public String getResult(String match_id)
	{
		String result = "0-0";
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="select result from score24.matches where match_id='"+match_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
				result=rs.getString("result");
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return result;
	}
	///////////////////////////////////////////////////////////////////////
	//Lay danh sach tat cac cac tran dau sap dien ra chot danh sach dang ky
	public void toRegFile(String match_id)
	{
		ArrayList arrMatches = new ArrayList();
		String home_id = "";
		String guest_id = "";
		String league_id = "";
		String round_id = "";
		try
		{	
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			neo.score24.object.MatchInfo matchInfo = this.getMatch(match_id);
			if(matchInfo!=null)
			{
				league_id = matchInfo.getEventPrimaryID();
				home_id = matchInfo.getHomeID();
				guest_id = matchInfo.getGuestID();
				round_id = matchInfo.getParentID();
				String str = "";		
				//Lay danh sach cac thue bao can gui su kien
				sqlcmd = "select distinct msisdn from score24.livescore_subscriber where match_id='"+league_id+"' or match_id='"+home_id+"' or match_id='"+guest_id+"' or match_id='"+match_id+"' or match_id='"+round_id+"'";
				db.stmt = db.con.createStatement();
				rs = db.stmt.executeQuery(sqlcmd);
				ArrayList arrLost = new ArrayList();
				ArrayList arrBalance = new ArrayList();
				String msisdn = "";
				//INClient inclient = new INClient();
				//inclient.init();
				//int balance = 0;
				//String balanceStr = "";
				livescoreClient client = new livescoreClient();
				client.makeConnection();
				while(rs.next())
				{
					//msisdn = rs.getString("msisdn");
					//if(msisdn.startsWith("84"))
					//	msisdn = msisdn.substring(2,msisdn.length());
					//else if(msisdn.startsWith("0"))
					//	msisdn = msisdn.substring(1,msisdn.length());
					//balance = inclient.getBalance(msisdn);					
						
					//if(balance==-1 || balance>=1000)
					//{									
					str+=rs.getString("msisdn")+"\n";
					//client.toCDR(rs.getString("msisdn"),"KQ");
					//}	
					//else if(balance>=0 && balance<1000)
					//{
					//	arrLost.add(rs.getString("msisdn"));
					//	arrBalance.add(String.valueOf(balance));
					//}
				}
				rs.close();
				client.removeConnection();
				//Them vao bang LIVESCORE_SUBSCRIBER_LOST
				/*if(arrLost!=null && arrLost.size()>0)
				{
					for(int i=0;i<arrLost.size();i++)
					{
						msisdn = arrLost.get(i).toString();
						try
						{						
							balance = Integer.parseInt(arrBalance.get(i).toString());
						}
						catch(Exception ex)
						{
							balance = 0;
							ex.printStackTrace();
						}
						boolean fInsert = true;
						if(msisdn!=null && msisdn.length()>0)
						{
							sqlcmd = "select * from score24.livescore_subscriber_lost where msisdn='"+msisdn+"' and match_id='"+match_id+"'";
							rs = db.stmt.executeQuery(sqlcmd);
							if(rs.next())
								fInsert = false;
							rs.close();
							if(fInsert)
								sqlcmd = "insert into score24.livescore_subscriber_lost values(score24.LIVESCORE_SUBSCRIBER_LOST_SEQ.nextval,'"+msisdn+"',sysdate,'"+match_id+"','"+balance+"')";	
							else
								sqlcmd = "update score24.livescore_subscriber_lost set balance = '"+balance+"' where msisdn='"+msisdn+"' and match_id='"+match_id+"'";	
							db.stmt.executeUpdate(sqlcmd);
						}	
					}
				}*/			
				
				if(str.length()>0)
				{
					//if(str.length()>0)
					//	str = str.substring(0,str.length()-1);
					String path="G:/Services/Score24/subscriber/";
					try
					{
						path+=match_id+".txt";
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
						fo.write(str.getBytes());		
					}
					catch(Exception es)
					{
						System.out.println(es.getMessage());
					}    	
				}
			}
			db.putConnection();	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	//Lay danh sach cac thue bao dang ky tu file text
	public ArrayList getRegSub(String match_id,String description)
	{
		ArrayList arrSubs = null;		
		try
		{
			db.getConnectionAndVerify();
			db.stmt=db.con.createStatement();
			String path="G:/Services/Score24/subscriber/";
			File f = new File(path+match_id+".txt");
			livescoreClient client = new livescoreClient();
			client.makeConnection();			
			if(f.exists())
			{			
				long size = f.length();
		        FileInputStream fstream = new FileInputStream(path+match_id+".txt");
				DataInputStream in = new DataInputStream(fstream);
	    		BufferedInputStream bin = new BufferedInputStream(fstream);
	    		byte[] bt= new byte[1024*8];
	    		String text="";
				int cn = 0;
				int dem = 0;
				long total=0;
				arrSubs =  new ArrayList();				
				while (true)
				{
					int i=bin.read(bt);
	      			total += i;
	      			if (i<1) break;
	      			text += new String(bt,0,i);
	      			while(true)
	      			{
	      				int j = text.indexOf("\n");
	      				if (j==-1) break;
	      				String tmp = text.substring(0,j);
	      				text = text.substring(j+1,text.length()); 
	      				arrSubs.add(tmp);
	      				if(description.indexOf("Tran dau ket thuc")>=0)
							client.toCDR(tmp,"KQ");	      							      				
	      			}
				}
			}
			else
			{	
				ArrayList arrMatches = new ArrayList();
				String home_id = "";
				String guest_id = "";
				String league_id = "";
				String round_id = "";		
				neo.score24.object.MatchInfo matchInfo = this.getMatch(match_id);
				if(matchInfo!=null)
				{
					league_id = matchInfo.getEventPrimaryID();
					home_id = matchInfo.getHomeID();
					guest_id = matchInfo.getGuestID();
					round_id = matchInfo.getParentID();				
					sqlcmd = "select distinct msisdn from score24.livescore_subscriber where match_id='"+league_id+"' or match_id='"+home_id+"' or match_id='"+guest_id+"' or match_id='"+match_id+"' or match_id='"+round_id+"'";
					db.stmt = db.con.createStatement();
					rs=db.stmt.executeQuery(sqlcmd);					
					arrSubs = new ArrayList();
					while(rs.next())
					{
						arrSubs.add(rs.getString("msisdn"));
						//Chua co doan ghi cuoc o day
						if(description.indexOf("Tran dau ket thuc")>=0)
							client.toCDR(rs.getString("msisdn"),"KQ");
					}
					rs.close();
					
				}
					
			}
			client.removeConnection();
			db.putConnection();		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return arrSubs;
	}
	////////////////////////////////////////////////////////////////////////////
	//Lay danh sach cac thue bao dang ky tu Database
	public ArrayList getRegSubfromDB(String match_id)
	{
		ArrayList arrSubs = null;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "select * from score24.livescore_subscriber where match_id='"+match_id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			arrSubs = new ArrayList();
			while(rs.next())
			{
				arrSubs.add(rs.getString("msisdn"));
			}
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return arrSubs;
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
					Match match = new Match();	
					match.makeConnection();		
					db.getConnectionAndVerify();
					db.stmt = db.con.createStatement();
					sqlcmd = "select * from score24.matches where is_active='1000' and match_date-sysdate>=0 and match_date-sysdate<=1/24/6 order by match_date";
					System.out.println(sqlcmd);
					rs = db.stmt.executeQuery(sqlcmd);
					ArrayList arrID = new ArrayList();
					while(rs.next())
					{
						System.out.println("OK");
						arrID.add(rs.getString("match_id"));	
					}
					rs.close();
					if(arrID.size()>0)
					{
						for(int i=0;i<arrID.size();i++)
						{
							//Ghi danh sach cac thue bao dang ky vao file text
							String match_id = arrID.get(i).toString();
							if(match_id.length()>0)
								match.toRegFile(match_id);
							//Dai lai trang thai tran dau
							sqlcmd = "update score24.matches set is_active='1100', match_seq=1 where match_id='"+match_id+"'";
							db.stmt = db.con.createStatement();
							db.stmt.executeUpdate(sqlcmd);
						}	
					}
					db.putConnection();
					match.removeConnection();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					db.putConnection();
				}
				System.out.println((new Date())+"Completed, now sleep in 1 minutes...");				
				Thread.sleep(1000*60*1);
			}
			
			
			/*Match match = new Match();
			match.makeConnection();
			ArrayList arr = match.getRegSub("544507");
			for(int i=0;i<arr.size();i++)
			{
				System.out.println(arr.get(i));
			}	
			match.removeConnection();*/
		}
		catch(Exception e)
		{
			e.printStackTrace();			
		}
	}
	
}
