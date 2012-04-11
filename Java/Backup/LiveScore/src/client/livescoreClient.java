package neo.score.client;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.*;
import neo.score.db.*;
import neo.score.object.*;
import neo.score.ejb.*;
import java.text.SimpleDateFormat;
import infogate.core.sms.HandlerBean;
import java.io.*;
import com.enterprisedt.net.ftp.*;
import cds.football.*;




public class livescoreClient 
{
	public LiveScore livescore;
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	public int status=-1;//-1: chua khoi tao; 0: Chua ket noi; 1: da ket noi
	public void init()
	{
		InitialContext jndiContext;
		Object ref;
		LiveScoreHome home;
		try
		{
			Properties jndiProps = new Properties();
			jndiContext = new InitialContext(jndiProps);			
      		jndiProps.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
      		//jndiProps.setProperty("java.naming.provider.url", "10.252.2.25:1099");
      		jndiProps.setProperty("java.naming.provider.url", "10.252.20.66:1099");
      		jndiProps.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");      		
      		jndiContext = new InitialContext(jndiProps); 
      		System.out.println("Looking server");     		
      		ref = jndiContext.lookup("livescore/ejb");
      		System.out.println("Lookup OfsfsK");
      		home = (LiveScoreHome)PortableRemoteObject.narrow(ref,LiveScoreHome.class);
      		System.out.println("32434");
			livescore = home.create();
			
			System.out.println("Remote object is created");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();			
		}
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
	public ArrayList getLeagues()
	{
		ArrayList arrLeagues = null;
		try
		{
			long t1 = System.currentTimeMillis();
			livescore.makeConnection();
			arrLeagues = livescore.getLeagues();
			livescore.removeConnection();
			long t2 = System.currentTimeMillis();
			System.out.println("Thoi gian thu hien Core: "+(t2-t1)+" mini giay");
			return arrLeagues;			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrLeagues;
	}
	////////////////////////////////////////////////////////////////////////////
	public int isLeagueExit(String league_code)
	{
		int response = 0;
		try
		{
			livescore.makeConnection();
			response = livescore.isLeagueExit(league_code);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofTeams(String league_code)
	{
		ArrayList arrTeams = null;
		try
		{
			livescore.makeConnection();
			arrTeams = livescore.getListofTeams(league_code);	
			livescore.removeConnection();		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrTeams;
	}
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofTeams(String league_code,String msisdn,int show)
	{
		ArrayList arrTeams = null;
		try
		{
			livescore.makeConnection();
			arrTeams = livescore.getListofTeams(league_code,msisdn,show);
			livescore.removeConnection();			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrTeams;	
	}	
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofRounds(String league_code)
	{
		ArrayList arrRounds = null;
		try
		{
			livescore.makeConnection();
			arrRounds = livescore.getListofRounds(league_code);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrRounds;
	}
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofRounds(String league_code,String msisdn,int show)
	{
		ArrayList arrRounds = null;
		try
		{
			livescore.makeConnection();
			arrRounds = livescore.getListofRounds(league_code,msisdn,show);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrRounds;
	}
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofMatchs(String league_code)
	{
		ArrayList arrMatchs = null;
		try
		{
			livescore.makeConnection();
			arrMatchs = livescore.getListofMatchs(league_code);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrMatchs;
	}	
	////////////////////////////////////////////////////////////////////////////
    public ArrayList getListofMatchs(String league_code,int show)
    {
        ArrayList arrMatchs = null;
        try
        {
        	livescore.makeConnection();
            arrMatchs = livescore.getListofMatchs(league_code,show);
            livescore.removeConnection();
        }
        catch(Exception ex)
        {
                ex.printStackTrace();
        }
        return arrMatchs;
    }
    ////////////////////////////////////////////////////////////////////////////
    public ArrayList getListofStartedMatchs(String league_code,int show)
    {
        ArrayList arrMatchs = null;
        try
        {
        	livescore.makeConnection();
            arrMatchs = livescore.getListofStartedMatchs(league_code,show);
            livescore.removeConnection();
        }
        catch(Exception ex)
        {
                ex.printStackTrace();
        }
        return arrMatchs;
    }
    ////////////////////////////////////////////////////////////////////////////
    public ArrayList getListofMatchs(String league_code,String msisdn,int show)
    {
        ArrayList arrMatchs = null;
        try
        {
        	livescore.makeConnection();
            arrMatchs = livescore.getListofMatchs(league_code,msisdn,show);
            livescore.removeConnection();
        }
        catch(Exception ex)
        {
                ex.printStackTrace();
        }
        return arrMatchs;
    }
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListMatchsofTeam(String team_id,String league_id,String status)
	{
		ArrayList arrMatchs = null;
		try
		{
			livescore.makeConnection();
			arrMatchs = livescore.getListMatchsofTeam(team_id,league_id,status);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrMatchs;
	}
	////////////////////////////////////////////////////////////////////////////
	public int regMatchs(String league_code,String msisdn,String matchs,String type)
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.regMatchs(league_code,msisdn,matchs,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public int regRounds(String league_code,String msisdn,String rounds,String type)
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.regRounds(league_code,msisdn,rounds,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public int regTeams(String league_code,String msisdn,String teams,String type)
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.regTeams(league_code,msisdn,teams,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public int regMatchsWeb(String league_code,String msisdn,String match_id,String type) 
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.regMatchsWeb(league_code,msisdn,match_id,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}	
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofMatchsReg(String league_code,String msisdn,String type)
	{
		ArrayList arrMatchs = null;
		try
		{
			livescore.makeConnection();
			arrMatchs = livescore.getListofMatchsReg(league_code,msisdn,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrMatchs;
	}
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofTeamsReg(String league_code,String msisdn,String type)
	{
		ArrayList arrTeams = null;
		try
		{
			livescore.makeConnection();
			arrTeams = livescore.getListofTeamsReg(league_code,msisdn,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return arrTeams;
	}
	////////////////////////////////////////////////////////////////////////////
	public ArrayList getListofRoundsReg(String league_code,String msisdn,String type)
	{
		ArrayList arrRounds = null;
		try
		{
			livescore.makeConnection();
			arrRounds = livescore.getListofRoundsReg(league_code,msisdn,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();	
		}
		return arrRounds;
	}
	////////////////////////////////////////////////////////////////////////////
	public int unRegMatchs(String league_code,String msisdn,String match_id,String type)
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.unRegMatchs(league_code,msisdn,match_id,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public int unRegRounds(String league_code,String msisdn,String rounds,String type)
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.unRegRounds(league_code,msisdn,rounds,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	public int unRegTeams(String league_code,String msisdn,String teams,String type)
	{
		int n = 0;
		try
		{
			livescore.makeConnection();
			n = livescore.unRegTeams(league_code,msisdn,teams,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public int unRegMatchsWeb(String msisdn,String match_id,String type)
	{
		int n=0;
		try
		{
			livescore.makeConnection();
			n = livescore.unRegMatchsWeb(msisdn,match_id,type);
			livescore.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
	////////////////////////////////////////////////////////////////////////////
	public void toLog(String sender, String receiver,String content)
	{
		SimpleDateFormat sdfm = new SimpleDateFormat("yyMM");
		String table = "LOG_LIVESCORE_" + String.valueOf(sdfm.format(new Date()));        
        String sqlcmd = "";
        DBConnection dbCon = new DBConnection();
        try
        {          	      
        	dbCon.getConnection();    	
        	dbCon.stmt = dbCon.con.createStatement();        		
        	sqlcmd = "insert into score24."+table+" values('"+sender+"','"+receiver+"',sysdate,'"+content.trim().replaceAll("'","''")+"')";        	                	
        	int n = dbCon.stmt.executeUpdate(sqlcmd);        	
        	dbCon.putConnection();
        	
        }
        catch(Exception ex)
        {
        	System.out.println("Loi ghi Log: "+ex.getMessage());
        	ex.printStackTrace();
        	dbCon.putConnection();
        }
	}
	////////////////////////////////////////////////////////////////////////////
	public void toEventLog(String sender,String receiver,String content,String match_id)
	{
		SimpleDateFormat sdfm = new SimpleDateFormat("yyMM");
		String table = "EVENT_LOG_" + String.valueOf(sdfm.format(new Date()));        
        String sqlcmd = "";
        
        try
        {
        	db.getConnection();
            db.stmt = db.con.createStatement();
            System.out.println("WRITE TO LOG...."+db.currentId);
        	sqlcmd = "insert into score24."+table+" values('"+sender+"','"+receiver+"',sysdate,'"+content.trim().replaceAll("'","''")+"','"+match_id+"')";
        	System.out.println(sqlcmd);        	
            db.sqlcommands[db.currentId] = sqlcmd;
        	int n = db.stmt.executeUpdate(sqlcmd);
        	System.out.println("WRITE TO LOG  END..."+db.currentId);              
            db.putConnection();  
        }
        catch(Exception ex)
        {
        	db.putConnection();  
        	ex.printStackTrace();
        }
	}
	////////////////////////////////////////////////////////////////////////////
	public void toLogOnline(String sender,String receiver,String content)
	{
        String sqlcmd = "";
        int n = 0;        
        try
        {
        	db.getConnection();
            db.stmt = db.con.createStatement();
            System.out.println("WRITE TO LOG...."+db.currentId);
        	//Xoa tat cac tin nhan trong bang co thoi gian qua 5 phut
        	sqlcmd="delete from score24.LOG_LIVESCORE_ONLINE where time<(sysdate-(1/24/6))";        	
        	n = db.stmt.executeUpdate(sqlcmd);
        	//Ghi noi dung tin nhan moi
        	sqlcmd = "insert into score24.LOG_LIVESCORE_ONLINE values('"+sender+"','"+receiver+"',sysdate,'"+content.trim().replaceAll("'","''")+"')";
        	System.out.println(sqlcmd);        	
            db.sqlcommands[db.currentId] = sqlcmd;
        	n = db.stmt.executeUpdate(sqlcmd);
        	System.out.println("WRITE TO LOG  END..."+db.currentId);              
            db.putConnection();  
        }
        catch(Exception ex)
        {
        	db.putConnection();  
        	ex.printStackTrace();
        }
	}
	////////////////////////////////////////////////////////////////////////////
	public void toCDR(String receiver,String volumCharge)
    {
		  try
		  {		  	
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		    Date receive;
		    receive=new Date();		    
		    neo.score.utils.CDRWriter writer = new
		    neo.score.utils.CDRWriter("G:/CDR/livescore_tmp/","Y:/ftp/livescore/","txt",86000,9999);
		    String fileName = writer.write(receiver,sdf.format(receive),volumCharge);		    
		  }
		  catch(Exception ex)
		  {
		    ex.printStackTrace();
		  }      
    }
    ////////////////////////////////////////////////////////////////////////////
    public void toError(String content)
    {		   
		String path="/data/log/";
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");	
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
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
	/*Lay tat cac cac tin nhan trong bang log online cua mot thue bao*/
	public ArrayList getLogSMSOnline(String msisdn)
	{
		ArrayList arrLogs = new ArrayList();
        String sqlcmd = "";
        java.sql.ResultSet rs = null;
        try
        {
        	db.getConnectionAndVerify();
        	db.stmt = db.con.createStatement();
        	sqlcmd = "select content from score24.log_livescore_online where sender='"+msisdn+"' order by time desc";
        	rs = db.stmt.executeQuery(sqlcmd);
        	while(rs.next())
        	{
        		arrLogs.add(rs.getString("content"));
        	}
        	rs.close();
        	db.putConnection();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        	db.putConnection();
    	}
    	return arrLogs;
	}
	////////////////////////////////////////////////////////////////////////////
	/*Ham kiem tra danh sach tin nhan trong vong 3 phut: Tin nhan gan nhat la 
	 *tin nhan dang ky hay huy dang ky
	 *0:Tin nhan dang ky
	 *1:Tin nhan huy
	 *2:Khong tim thay dieu kien thoa man*/
	public String[] isRegCanMatch(String msisdn)
	{
		String[] response = null;
		int n = 2;
		ArrayList arrLogs = new ArrayList();
		String content = "";
		String[] tmp = null;
		try
		{
			System.out.println("Lay lich su SMS cua thue bao: "+msisdn);
			arrLogs = this.getLogSMSOnline(msisdn);
			
			if(arrLogs.size()>0)
				response = new String[2];
			else
				return null;
			for(int i=0;i<arrLogs.size();i++)
			{	
				content = arrLogs.get(i).toString().trim();
				if(content.indexOf(" ")>0)
					tmp = content.split(" ");
				else
				{
					tmp = new String[1];
					tmp[0]=content;
				}	
				if(tmp.length>1)
				{					
					if(tmp[0].equalsIgnoreCase("l") || tmp[0].equalsIgnoreCase("list") || tmp[0].equalsIgnoreCase("reg") || tmp[0].equalsIgnoreCase("register") || tmp[0].equalsIgnoreCase("dk") || tmp[0].equalsIgnoreCase("dangky"))
					{
						n = 0;						
						response[0]=String.valueOf(n);
						response[1]=tmp[1];	
						return response;					
					}					
					else if(tmp[0].equalsIgnoreCase("can") || tmp[0].equalsIgnoreCase("cancel") || tmp[0].equalsIgnoreCase("huy") || tmp[0].equalsIgnoreCase("unreg") || tmp[0].equalsIgnoreCase("unregister"))
					{
						n = 1;	
						response[0]=String.valueOf(n);
						response[1]=tmp[1];	
						return response;				
					}
					else
					{
						n = 2;
						response[0]=String.valueOf(n);
						response[1]=tmp[1];					
					}
									
				}				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////
	/*Ham kiem tra danh sach tin nhan trong vong 3 phut: Tin nhan gan nhat la 
	 *tin nhan dang ky hay huy dang ky doi bong
	 *0:Tin nhan dang ky
	 *1:Tin nhan huy
	 *2:Khong tim thay dieu kien thoa man*/
	public String[] isRegCanTeam(String msisdn)
	{
		String[] response = null;
		int n = 2;
		ArrayList arrLogs = new ArrayList();
		String content = "";
		String[] tmp = null;
		try
		{
			arrLogs = this.getLogSMSOnline(msisdn);
			if(arrLogs.size()>0)
				response = new String[2];
			else
				return null;
			for(int i=0;i<arrLogs.size();i++)
			{
				content = arrLogs.get(i).toString().trim();
				tmp = content.split(" ");
				if(tmp.length>1)
				{
					if(tmp[0].equalsIgnoreCase("team") || tmp[0].equalsIgnoreCase("doi") || tmp[0].equalsIgnoreCase("doibong"))
					{
						n = 0;	
						response[0]=String.valueOf(n);
						response[1]=tmp[1];	
						return response;					
					}					
					else if(tmp[0].equalsIgnoreCase("canteam") || tmp[0].equalsIgnoreCase("cancelteam") || tmp[0].equalsIgnoreCase("huydoi"))
					{
						n = 1;	
						response[0]=String.valueOf(n);
						response[1]=tmp[1];	
						return response;				
					}
					else
					{
						n = 2;
						response[0]=String.valueOf(n);
						response[1]=tmp[1];					
					}
									
				}
				else
					return null;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////
	/*Ham kiem tra danh sach tin nhan trong vong 3 phut: Tin nhan gan nhat la 
	 *tin nhan dang ky hay huy dang ky vong dau
	 *0:Tin nhan dang ky
	 *1:Tin nhan huy
	 *2:Khong tim thay dieu kien thoa man*/
	public String[] isRegCanRound(String msisdn)
	{
		String[] response = null;
		int n = 2;
		ArrayList arrLogs = new ArrayList();
		String content = "";
		String[] tmp = null;
		try
		{
			arrLogs = this.getLogSMSOnline(msisdn);
			if(arrLogs.size()>0)
				response = new String[2];
			else
				return null;
			for(int i=0;i<arrLogs.size();i++)
			{
				content = arrLogs.get(i).toString().trim();
				tmp = content.split(" ");
				if(tmp.length>1)
				{
					if(tmp[0].equalsIgnoreCase("round") || tmp[0].equalsIgnoreCase("vong") || tmp[0].equalsIgnoreCase("vongdau"))
					{
						n = 0;	
						response[0]=String.valueOf(n);
						response[1]=tmp[1];	
						return response;					
					}					
					else if(tmp[0].equalsIgnoreCase("canround") || tmp[0].equalsIgnoreCase("cancelround") || tmp[0].equalsIgnoreCase("huyvong"))
					{
						n = 1;	
						response[0]=String.valueOf(n);
						response[1]=tmp[1];	
						return response;				
					}
					else
					{
						n = 2;
						response[0]=String.valueOf(n);
						response[1]=tmp[1];					
					}
									
				}
				else
					return null;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////
	public void sendSMS(String sender,String receiver,String content)
	{
		try 
		{	    
		  infogate.gateway.sms.client.SMSGatewayConnection con=new infogate.gateway.sms.client.SMSGatewayConnection("10.252.20.66",10003);
		  
		  if(content.length()>160)
		  {
		  	String tmp = content.substring(0,160);
		  	int idx = tmp.lastIndexOf(" ");
		  	tmp = content.substring(0,idx);
		  	infogate.gateway.sms.utils.SMSMessage msg=new infogate.gateway.sms.utils.SMSMessage(sender,receiver,tmp);  
		  	con.sendMessage(msg);
		  	content = content.substring(idx+1,content.length());
		  	msg=new infogate.gateway.sms.utils.SMSMessage(sender,receiver,content);  
		  	con.sendMessage(msg);
		  }
		  else
		  {	
		  	  infogate.gateway.sms.utils.SMSMessage msg=new infogate.gateway.sms.utils.SMSMessage(sender,receiver,content);  	      
		      con.sendMessage(msg);
		  } 
		      
    	}
    	catch (Exception e) 
    	{
      		e.printStackTrace();
    	}
	}
	////////////////////////////////////////////////////////////////////////////
	public void sendSMStoRTC(String sender,String receiver,String content)
	{
		try 
		{	      
		  
		      infogate.gateway.sms.client.SMSGatewayConnection con=new infogate.gateway.sms.client.SMSGatewayConnection("10.252.2.23",10003);
		      infogate.gateway.sms.utils.SMSMessage msg=new infogate.gateway.sms.utils.SMSMessage(sender,receiver,content);
		      con.sendMessage(msg);
		      
    	}
    	catch (Exception e) 
    	{
      		e.printStackTrace();
    	}
	}
	////////////////////////////////////////////////////////////////////////////
	public void sendEvent(String match_id,String description)
	{
		String sqlcmd = "";		
		String home_id = "";
		String guest_id = "";
		String league_id = "";
		String round_id = "";
		ArrayList arrSubs = new ArrayList();
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();	
			boolean fSend = false;
			//sqlcmd = "select * from score24.matches where match_id='"+match_id+"' and sysdate-match_date>=0 and sysdate-match_date<=1/8";
			sqlcmd = "select * from score24.matches where match_id='"+match_id+"'  and sysdate-match_date<=1/8";
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
				fSend=true;	
			rs.close();
			if(fSend)
			{
				this.sendSMS("919","841228333000",description);
				this.sendSMS("919","84936100234",description);
				int counter = 0;
				neo.score.business.Match match = new neo.score.business.Match();
				match.makeConnection();
				arrSubs = match.getRegSub(match_id,description);
				match.removeConnection();					
				if(arrSubs!=null && arrSubs.size()>0)
				{
					for(int i=0;i<arrSubs.size();i++)
					{
						counter++;					
						this.sendSMS("919",arrSubs.get(i).toString(),description);
						this.toEventLog("919",arrSubs.get(i).toString(),description,match_id);
						
					}
				}
				match.removeConnection();
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
	public void sendEventTest(String description)
	{
		String sqlcmd = "";
		java.sql.ResultSet rs = null;		
		String home_id = "";
		String guest_id = "";
		String league_id = "";
		String round_id = "";
		ArrayList arrSubs = new ArrayList();
		try
		{
		
			int count = 0;
			//Lay danh sach cac thue bao can gui su kien
			sqlcmd = "select * from score24.livescore_subscriber order by msisdn";
			rs = db.stmt.executeQuery(sqlcmd);
			while(rs.next())
			{
				count++;
				if(count<=1000)
					arrSubs.add("84906044701");
				else 
					break;
			}
			rs.close();					
		
			if(arrSubs!=null && arrSubs.size()>0)
			{
				for(int i=0;i<arrSubs.size();i++)
				{
					this.sendSMS("919",arrSubs.get(i).toString(),description);
					this.toEventLog("919",arrSubs.get(i).toString(),description,"567689");
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{
		try
		{
			
			//FootballClient con = new FootballClient("10.252.6.125:1099","cds/football");
	      	//con.initConnectionToAS(); 
			//System.out.println(con.updateResult(0,"PRE",1,"MU-MC","1-0","Ghi ban: W.Rooney"));
			livescoreClient client = new livescoreClient();
			client.init();
			client.sendSMS("919","841228333000","12546");
		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		
		}		
		
	}
}
