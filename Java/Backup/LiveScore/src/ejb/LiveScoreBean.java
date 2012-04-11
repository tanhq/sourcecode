package neo.score.ejb;

import java.sql.*;
import java.util.*;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import neo.score.db.DBConnection;
import neo.score24.object.*;
import neo.score.business.*;


public class LiveScoreBean implements SessionBean
{
	public DBConnection db = null;
	public ResultSet rs = null;
	
	public void ejbCreate(){
	}
	
	public void ejbRemove(){
	}
	
	public void ejbActivate(){
	}
	
	public void ejbPassivate(){
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
	public void setSessionContext(SessionContext context){
	}
	/*
	 *	Phuong thuc lay tat ca cac giai dau dang va sap dien ra
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac trang dau duoc tra lai trong mot ArrayList
	 */
	 public ArrayList getLeagues()
	 {
	 	ArrayList arrLeagues = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	String league_name = "";
	 	String league_code = "";
	 	java.sql.ResultSet rs = null;
	 	try
	 	{	 	
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd="select * from score24.leagues where status <> '1500'";
	 		rs = db.stmt.executeQuery(sqlcmd);	 		
	 		while(rs.next())
	 		{
	 			LeagueInfo league = new LeagueInfo();
	 			league.setLeagueID(rs.getString("league_id"));	 	
	 			league.setLeagueName(rs.getString("league_name"));		
	 			league.setLeagueNameVN(rs.getString("league_name_vn"));
	 			league.setLeagueCode(rs.getString("league_code"));
	 			arrLeagues.add(league);
	 		}	 		
	 		rs.close();
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrLeagues;
	 }
	 /*
	 *	Phuong thuc kiem tra su ton tai cua mot giai dau
	 *		return 0	: Gai dau khong ton tai
	 *		return >0 	: Gai dau ton tai
	 */
	 public int isLeagueExit(String league_code)
	 {
	 	int response = 0;
	 	String sqlcmd="";
	 	league_code = league_code.toUpperCase().trim();
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd = "select * from score24.leagues where UPPER(league_code)='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			response = 1;
	 		rs.close();
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return response;
	 }
	 /*
	 *	Phuong thuc lay tat ca cac doi bong thuoc 1 giai dau
	 *		return null		: Khong co doi bong bao
	 *		return khac 	: Danh sach cac doi bong thuoc giai dau
	 */
	 public ArrayList getListofTeams(String league_code)
	 {
	 	ArrayList arrTeams = new ArrayList();
	 	String sqlcmd="";
	 	String match_id = "";
	 	String match_name = "";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd = "select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();
	 		if(league_id.length()>0)
	 		{
	 			sqlcmd="select a.team_id,b.full_name from score24.teams a, score24.team_info b where a.team_id=b.team_id and a.league_id='"+league_id+"' order by b.full_name";
	 			rs = db.stmt.executeQuery(sqlcmd);
	 			while(rs.next())
	 			{
	 				CommonInfo com = new CommonInfo();
	 				com.setId(rs.getString("team_id"));
	 				com.setName(rs.getString("full_name"));
	 				arrTeams.add(com);
	 			}
	 		}
	 		db.putConnection();
	 	}	
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrTeams;
	 }
	  /*
	 *	Phuong thuc lay tat ca cac doi bong thuoc 1 giai dau
	 *		return null		: Khong co doi bong bao
	 *		return khac 	: Danh sach cac doi bong thuoc giai dau
	 */
	 public ArrayList getListofTeams(String league_code,String msisdn,int show)
	 {
	 	league_code = league_code.toUpperCase();
	 	ArrayList arrTeams = new ArrayList();
	 	String sqlcmd="";
	 	String match_id = "";
	 	String match_name = "";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd = "select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();
	 		if(league_id.length()>0)
	 		{
	 			sqlcmd="select rownum,team_id,full_name from (select a.team_id,b.full_name from score24.teams a, score24.team_info b where a.team_id=b.team_id and a.league_id='"+league_id+"' and a.team_id NOT IN (select match_id from score24.livescore_subscriber where league_id="+league_id+" and type='1' and msisdn="+msisdn+") order by b.full_name) where rownum<="+show;
	 			rs = db.stmt.executeQuery(sqlcmd);
	 			while(rs.next())
	 			{
	 				CommonInfo com = new CommonInfo();
	 				com.setId(rs.getString("team_id"));
	 				com.setName(rs.getString("full_name"));
	 				arrTeams.add(com);
	 			}
	 		}
	 		db.putConnection();
	 	}	
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrTeams;
	 }
	 /*
	 *	Phuong thuc lay tat ca cac vong dau sap dien ra thuoc mot giai dau
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac doi bong thuoc giai dau
	 */
	 public ArrayList getListofRounds(String league_code)
	 {
	 	ArrayList arrRounds = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";
	 	String round_name = "";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd = "select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();
	 		sqlcmd="select rownum,round_id,round_name,round_name_vn from (select round_id,round_name,round_name_vn from score24.rounds  where  status <> '1500' and league_id='"+league_id+"' order by time) where rownum<=10";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		while(rs.next())
	 		{
	 			CommonInfo com = new CommonInfo();
	 			com.setId(rs.getString("round_id"));
	 			com.setName(rs.getString("round_name_vn"));
	 			arrRounds.add(com);
	 		}
	 		rs.close();
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrRounds;
	 }
	 /*
	 *	Phuong thuc lay tat ca cac vong dau sap dien ra thuoc mot giai dau
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac doi bong thuoc giai dau
	 */
	 public ArrayList getListofRounds(String league_code,String msisdn,int show)
	 {
	 	league_code = league_code.toUpperCase();
	 	ArrayList arrRounds = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";
	 	String round_name = "";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd = "select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();
	 		sqlcmd="select rownum,round_id,round_name,round_name_vn from (select round_id,round_name,round_name_vn from score24.rounds  where  status <> '1500' and league_id='"+league_id+"' and round_id NOT IN (select match_id from score24.livescore_subscriber where league_id="+league_id+" and type='2' and msisdn="+msisdn+") order by time) where rownum<="+show;
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		while(rs.next())
	 		{
	 			CommonInfo com = new CommonInfo();
	 			com.setId(rs.getString("round_id"));
	 			com.setName(rs.getString("round_name"));
	 			arrRounds.add(com);
	 		}
	 		rs.close();
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrRounds;
	 }
	/*
	 *	Phuong thuc lay tat ca cac trang dau sap dien ra cua mot giai dau
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac trang dau duoc tra lai trong mot ArrayList
	 */
	 public ArrayList getListofMatchs(String league_code)
	 {
	 	ArrayList arrMatchs = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";
	 	String league_id = "";
	 	league_code = league_code.toUpperCase();
	 	java.sql.ResultSet rs = null;
	 	try
	 	{	 
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();		
	 		sqlcmd="select league_id from score24.leagues where league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();	 		
	 		if(league_id!=null && league_id.length()>0)
	 		{	 			
				sqlcmd = "select rownum,match_id,match_name,home_id,guest_id,to_char(match_date,'dd/MM hh24:mi') as thoigian,parent_id,eventprimary_id from (select a.match_id,a.match_name,a.home_id,a.guest_id,a.match_date,a.parent_id,a.eventprimary_id from score24.matches a,score24.livescore_status b where a.eventprimary_id='"+league_id+"' and a.is_active=b.status_id and b.status_type='match' and b.status_name='Not started' order by match_date) where rownum<=10";
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	 					
					MatchInfo match = new MatchInfo();
					match.setMatchID(rs.getString("match_id"));	 				
					match.setMatchName(rs.getString("match_name"));
					match.setHomeID(rs.getString("home_id"));
					match.setGuestID(rs.getString("guest_id"));	 				
					match.setMatchDate(rs.getString("thoigian"));					
					match.setParentID(rs.getString("parent_id"));
					match.setEventPrimaryID(rs.getString("eventprimary_id"));
					arrMatchs.add(match);					
	
				}
				rs.close();	 		
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	finally
	 	{
	 		db.putConnection();
	 	}
	 	if(arrMatchs.size()==0)
	 		arrMatchs = null;
	 	return arrMatchs;
	 }
	 /*
	 *	Phuong thuc lay tat ca cac trang dau sap dien ra cua mot giai dau
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac trang dau duoc tra lai trong mot ArrayList
	 */
	 public ArrayList getListofMatchs(String league_code, int show)
	 {
	 	ArrayList arrMatchs = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";
	 	String league_id = "";
	 	league_code = league_code.toUpperCase();
	 	java.sql.ResultSet rs = null;
	 	try
	 	{	 		
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd="select league_id from score24.leagues where league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();	 		
	 		if(league_id!=null && league_id.length()>0)
	 		{	 			
				sqlcmd = "select rownum,match_id,match_name,home_id,guest_id,to_char(match_date,'dd/MM hh24:mi') as thoigian,parent_id,eventprimary_id from (select a.match_id,a.match_name,a.home_id,a.guest_id,a.match_date,a.parent_id,a.eventprimary_id from score24.matches a,score24.livescore_status b where a.eventprimary_id='"+league_id+"' and a.is_active=b.status_id and b.status_type='match' and b.status_name='Not started' order by match_date) where rownum<="+show;
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	 					
					MatchInfo match = new MatchInfo();
					match.setMatchID(rs.getString("match_id"));	 				
					match.setMatchName(rs.getString("match_name"));
					match.setHomeID(rs.getString("home_id"));
					match.setGuestID(rs.getString("guest_id"));	 				
					match.setMatchDate(rs.getString("thoigian"));					
					match.setParentID(rs.getString("parent_id"));
					match.setEventPrimaryID(rs.getString("eventprimary_id"));
					arrMatchs.add(match);					
	
				}
				rs.close();	 		
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	if(arrMatchs.size()==0)
	 		arrMatchs = null;
	 	return arrMatchs;
	 }
	 /*
	 *	Phuong thuc lay tat ca cac trang dau vua dien ra cua mot giai dau
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac trang dau duoc tra lai trong mot ArrayList
	 */
	 public ArrayList getListofStartedMatchs(String league_code, int show)
	 {
	 	ArrayList arrMatchs = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";
	 	String league_id = "";
	 	league_code = league_code.toUpperCase();
	 	java.sql.ResultSet rs = null;
	 	try
	 	{	 		
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		sqlcmd="select league_id from score24.leagues where league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();	 		
	 		if(league_id!=null && league_id.length()>0)
	 		{	 			
				sqlcmd = "select rownum,match_id,match_name,home_id,guest_id,to_char(match_date,'dd/MM hh24:mi') as thoigian,parent_id,eventprimary_id,result from (select a.match_id,a.match_name,a.home_id,a.guest_id,a.match_date,a.parent_id,a.eventprimary_id,a.result from score24.matches a,score24.livescore_status b where a.result!='Unknown' and a.eventprimary_id='"+league_id+"' and a.is_active=b.status_id and b.status_type='match' and (b.status_name='Finished' or b.status_name='Active') order by match_date desc) where rownum<="+show;
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	 					
					MatchInfo match = new MatchInfo();
					match.setMatchID(rs.getString("match_id"));	 				
					match.setMatchName(rs.getString("match_name"));
					match.setHomeID(rs.getString("home_id"));
					match.setGuestID(rs.getString("guest_id"));	 				
					match.setMatchDate(rs.getString("thoigian"));					
					match.setParentID(rs.getString("parent_id"));
					match.setEventPrimaryID(rs.getString("eventprimary_id"));
					match.setResult(rs.getString("result"));
					arrMatchs.add(match);					
	
				}
				rs.close();	 		
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	if(arrMatchs.size()==0)
	 		arrMatchs = null;
	 	return arrMatchs;
	 }
	 /*
	 *	Phuong thuc danh sach cac trang dau sap dien ra cua mot giai dau theo tung so thue bao
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac trang dau duoc tra lai trong mot ArrayList
	 */
	 public ArrayList getListofMatchs(String league_code,String msisdn,int show)
	 {
	 	ArrayList arrMatchs = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";
	 	String league_id = "";
	 	league_code = league_code.toUpperCase();
	 	java.sql.ResultSet rs = null;
	 	try
	 	{	 	
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();	
	 		sqlcmd="select league_id from score24.leagues where league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();	 		
	 		if(league_id!=null && league_id.length()>0)
	 		{	 			
				sqlcmd = "select rownum,match_id,match_name,home_id,guest_id,to_char(match_date,'dd/MM hh24:mi') as thoigian,parent_id,eventprimary_id from (select a.match_id,a.match_name,a.home_id,a.guest_id,a.match_date,a.parent_id,a.eventprimary_id from score24.matches a,score24.livescore_status b where a.eventprimary_id='"+league_id+"' and a.is_active=b.status_id and b.status_type='match' and b.status_name='Not started' and a.match_id NOT IN (select match_id from score24.livescore_subscriber where league_id="+league_id+" and msisdn="+msisdn+") order by match_date) where rownum<="+show;
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	 					
					MatchInfo match = new MatchInfo();
					match.setMatchID(rs.getString("match_id"));	 				
					match.setMatchName(rs.getString("match_name"));
					match.setHomeID(rs.getString("home_id"));
					match.setGuestID(rs.getString("guest_id"));	 				
					match.setMatchDate(rs.getString("thoigian"));					
					match.setParentID(rs.getString("parent_id"));
					match.setEventPrimaryID(rs.getString("eventprimary_id"));
					arrMatchs.add(match);					
	
				}
				rs.close();	 		
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	if(arrMatchs.size()==0)
	 		arrMatchs = null;
	 	return arrMatchs;
	 }
	 /*
	 *	Phuong thuc lay tat ca cac trang dau sap dien ra cua mot doi bong trong mot giai dau
	 *		return null		: Khong co tran dau nao sap dien ra
	 *		return khac 	: Danh sach cac trang dau duoc tra lai trong mot ArrayList
	 */
	 public ArrayList getListMatchsofTeam(String team_id,String league_id,String status)
	 {
	 	ArrayList arrMatchs = new ArrayList();
	 	String sqlcmd="";
	 	String round_id = "";	 
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		if(league_id!=null && league_id.length()>0)
	 		{
	 			//sqlcmd = "select a.match_id,a.match_name,a.home_id,a.guest_id,to_char(match_date,'dd/MM hh24:mi') as thoigian,a.parent_id,a.eventprimary_id,a.result from score24.matches a,score24.livescore_status b where a.eventprimary_id='"+league_id+"' and (a.home_id='"+team_id+"' or a.guest_id='"+team_id+"') and a.is_active=b.status_id and b.status_type='match' and b.status_name='"+status+"' order by match_date";
	 			sqlcmd = "select a.match_id,a.match_name,a.home_id,a.guest_id,to_char(match_date,'dd/MM hh24:mi') as thoigian,a.parent_id,a.eventprimary_id,a.result from score24.matches a,score24.livescore_status b where a.eventprimary_id='"+league_id+"' and (a.home_id='"+team_id+"' or a.guest_id='"+team_id+"') and a.is_active=b.status_id and b.status_type='match' and b.status_name='"+status+"' ";
	 			if(status.equals("Finished"))
	 				sqlcmd += "order by match_date desc";
	 			else if(status.equals("Not started"))
	 				sqlcmd += "order by match_date";
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	 					
					MatchInfo match = new MatchInfo();
					match.setMatchID(rs.getString("match_id"));	 				
					match.setMatchName(rs.getString("match_name"));
					match.setHomeID(rs.getString("home_id"));
					match.setGuestID(rs.getString("guest_id"));	 				
					match.setMatchDate(rs.getString("thoigian"));					
					match.setParentID(rs.getString("parent_id"));
					match.setEventPrimaryID(rs.getString("eventprimary_id"));
					match.setResult(rs.getString("result"));
					arrMatchs.add(match);
				}
				rs.close();
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrMatchs;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Phuong thuc dang ky tran dau cho mot so thue bao qua SMS
	 *		return 0	: Co loi trong qua trinh dang ky
	 *		return 1 	: Dang ky thanh cong
	 */
	 public int regMatchs(String league_code,String msisdn,String matchs,String type)
	 {
	 	int response = 0;
	 	String sqlcmd = "";
	 	java.sql.ResultSet rs =null;
	 	int tmp=0;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		//Lay danh sach so thu tu cac tran dau can dang ky
	 		matchs = matchs.trim();
	 		String[] arrRegs = null;
	 		if(matchs.indexOf(" ")>=0)
	 			arrRegs = matchs.trim().split(" ");
	 		else if(matchs.indexOf(",")>=0)
	 			arrRegs = matchs.trim().split(",");
	 		else
	 		{
	 			arrRegs = new String[1];
	 			arrRegs[0]=matchs;
	 			
	 		}
	 		System.out.println("Dang ky tran dau 2: "+matchs);
	 		System.out.println("Do dai: "+arrRegs.length);
	 		//Lay danh sach cac tran dau sap dien ra thuoc giai dau quan tam
	 		league_code = league_code.toUpperCase();
	 		ArrayList arrMatchs = this.getListofMatchs(league_code,msisdn,10);
	 		if(arrMatchs!=null && arrMatchs.size()>0)
	 		{
	 			for(int i=0;i<arrMatchs.size();i++)
	 			{
	 				MatchInfo match = (MatchInfo)arrMatchs.get(i);
	 				tmp=i+1;
	 				for(int j=0;j<arrRegs.length;j++)
	 				{
	 					if(arrRegs[j]!=null && arrRegs[j].length()>0)
	 					{	 
	 						try
	 						{
	 							if(tmp==Integer.parseInt(arrRegs[j].toString()))
			 					{
			 						SubscriberInfo sub = new SubscriberInfo();		 						
			 						sub.setUserStatus("0");
			 						sub.setMatchID(match.getMatchID());
			 						sub.setMsisdn(msisdn);
			 						sub.setLeagueID(match.getEventPrimaryID());
			 						sub.setRoundID(match.getParentID());
			 						sub.setType(type);
			 						Subscriber client = new Subscriber();
			 						client.makeConnection();
			 						response+=client.insertSubscriber(sub);
			 						client.removeConnection();
			 					}	
	 						}
	 						catch(Exception e)
	 						{
	 							System.out.println("Ma so tran dau khong hop le (Tran dau: "+arrRegs[j].toString()+")");
	 						}					
		 					
		 				}
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
	 	return response;	 	
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Phuong thuc dang ky theo doi bong cho thue bao qua SMS
	 *		return 0	: Co loi trong qua trinh dang ky
	 *		return 1 	: Dang ky thanh cong
	 */
	 public int regTeams(String league_code,String msisdn,String teams,String type)
	 {
	 	int response = 0;
	 	String sqlcmd = "";
	 	java.sql.ResultSet rs =null;
	 	int tmp=0;
	 	String league_id="";
	 	league_code = league_code.toUpperCase();
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		//Lay ma so giai dau
	 		sqlcmd = "select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();
	 		System.out.println("Dang ky tran dau: "+teams);
	 		//Lay danh sach so thu tu cac tran dau can dang ky
	 		teams = teams.trim();
	 		String[] arrRegs = null;
	 		if(teams.indexOf(" ")>=0)
	 			arrRegs = teams.trim().split(" ");
	 		else if(teams.indexOf(",")>=0)
	 			arrRegs = teams.trim().split(",");
	 		//Lay danh sach cac doi bong thuoc giai dau quan tam
	 		league_code = league_code.toUpperCase();
	 		ArrayList arrTeams = this.getListofTeams(league_code,msisdn,10);
	 		if(arrTeams!=null && arrTeams.size()>0)
	 		{
	 			for(int i=0;i<arrTeams.size();i++)
	 			{
	 				CommonInfo team = (CommonInfo)arrTeams.get(i);
	 				tmp=i+1;
	 				for(int j=0;j<arrRegs.length;j++)
	 				{
	 					if(tmp==Integer.parseInt(arrRegs[j].toString()))
	 					{
	 						SubscriberInfo sub = new SubscriberInfo();
	 						sub.setUserStatus("0");
	 						sub.setMatchID(team.getId());
	 						sub.setMsisdn(msisdn);
	 						sub.setLeagueID(league_id);
	 						sub.setRoundID(team.getId());
	 						sub.setType(type);
	 						Subscriber client = new Subscriber();
	 						client.makeConnection();
	 						response+=client.insertSubscriber(sub);	 						
	 						client.removeConnection();
	 					}
	 				}
	 			}
	 		}
	 		db.putConnection();
	 		
	 	}
	 	catch(Exception ex)
	 	{
	 		System.out.println("Loi khi dang ky tran dau");
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return response;	 	
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Phuong thuc dang ky theo vong dau cho thue bao qua SMS
	 *		return 0	: Co loi trong qua trinh dang ky
	 *		return 1 	: Dang ky thanh cong
	 */
	 public int regRounds(String league_code,String msisdn,String rounds,String type)
	 {
	 	int response = 0;
	 	league_code = league_code.toUpperCase();
	 	String sqlcmd = "";
	 	java.sql.ResultSet rs =null;
	 	int tmp=0;
	 	String league_id="";
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		//Lay ma so giai dau
	 		sqlcmd = "select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();
	 		System.out.println("League ID: "+league_id);	 				
	 		//Lay danh sach so thu tu cac tran dau can dang ky
	 		rounds = rounds.trim();
	 		String[] arrRegs = null;
	 		if(rounds.indexOf(" ")>=0)
	 			arrRegs = rounds.trim().split(" ");
	 		else if(rounds.indexOf(",")>=0)
	 			arrRegs = rounds.trim().split(",");
	 		//Lay danh sach cac doi bong thuoc giai dau quan tam	 		
	 		ArrayList arrRounds = this.getListofRounds(league_code,msisdn,10);
	 		if(arrRounds!=null && arrRounds.size()>0)
	 		{
	 			for(int i=0;i<arrRounds.size();i++)
	 			{
	 				CommonInfo round = (CommonInfo)arrRounds.get(i);
	 				tmp=i+1;
	 				for(int j=0;j<arrRegs.length;j++)
	 				{
	 					if(tmp==Integer.parseInt(arrRegs[j].toString()))
	 					{
	 						SubscriberInfo sub = new SubscriberInfo();
	 						sub.setUserStatus("0");
	 						sub.setMatchID(round.getId());
	 						sub.setMsisdn(msisdn);
	 						sub.setLeagueID(league_id);
	 						sub.setRoundID(round.getId());
	 						sub.setType(type);
	 						Subscriber client = new Subscriber();
	 						client.makeConnection();
	 						response+=client.insertSubscriber(sub);
	 						client.removeConnection();
	 					}
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
	 	return response;	 	
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Lay danh sach cac tran dau da dang ky
	 *		return null	: Co loi trong qua trinh dang ky
	 *		return khac	: Danh sach cac tran dau da dang ky
	 */
	 public ArrayList getListofMatchsReg(String league_code,String msisdn,String type)
	 {
	 	ArrayList arrMatchs = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		league_code = league_code.toUpperCase();
	 		sqlcmd="select league_id from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 			league_id = rs.getString("league_id");
	 		rs.close();	
	 		if(league_id!=null && league_id.length()>0)
			{
				
				//Lay thong tin ve tran dau da dang ky	
				sqlcmd="select a.match_id,a.register_time,a.round_id,a.league_id,a.type,b.match_name,to_char(b.match_date,'dd/MM hh24:mi') as thoigian,b.is_active from score24.livescore_subscriber a,score24.matches b where a.type='0' and a.match_id=b.match_id and a.league_id='"+league_id+"' and b.is_active = '1000' and a.msisdn='"+msisdn+"' order by b.match_date";
				rs = db.stmt.executeQuery(sqlcmd);
				System.out.println(sqlcmd);
				while(rs.next())
				{	
					MatchInfo match = new MatchInfo();	
					match.setEventPrimaryID(rs.getString("league_id"));
					match.setParentID(rs.getString("round_id"));
					match.setMatchID(rs.getString("match_id"));
					match.setMatchDate(rs.getString("thoigian"));
					match.setMatchName(rs.getString("match_name"));
					match.setIsActive(rs.getString("is_active"));
					arrMatchs.add(match);
				}
				rs.close();	
										
			}
			db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrMatchs;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Lay danh sach cac doi bong da dang ky
	 *		return null	: Co loi trong qua trinh dang ky
	 *		return khac	: Danh sach cac tran dau da dang ky
	 */
	 public ArrayList getListofTeamsReg(String league_code,String msisdn,String type)
	 {
	 	ArrayList arrTeams = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	String league_name_vn = "";
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		league_code = league_code.toUpperCase();
	 		sqlcmd="select league_id,league_name_vn from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 		{
	 			league_name_vn = rs.getString("league_name_vn");
	 			league_id = rs.getString("league_id");
	 		}
	 		rs.close();	
	 		if(league_id!=null && league_id.length()>0)
	 		{
	 			sqlcmd="select a.match_id,a.register_time,a.round_id,b.full_name from score24.livescore_subscriber a,score24.team_info b where a.type='1' and a.match_id=b.team_id and a.league_id='"+league_id+"' and a.msisdn='"+msisdn+"' order by b.full_name";
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	
					MatchInfo match = new MatchInfo();	
					match.setEventPrimaryID(league_id);
					match.setParentID(rs.getString("round_id"));
					match.setMatchID(rs.getString("match_id"));					
					match.setMatchName(rs.getString("full_name"));
					match.setMatchDate(rs.getString("register_time"));
					arrTeams.add(match);
				
				}
				rs.close();	
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();	
	 		db.putConnection();
	 	}
	 	return arrTeams;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Lay danh sach cac vong dau da dang ky
	 *		return null	: Co loi trong qua trinh dang ky
	 *		return khac	: Danh sach cac tran dau da dang ky
	 */
	 public ArrayList getListofRoundsReg(String league_code,String msisdn,String type)
	 {
	 	ArrayList arrRounds = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	String league_name_vn = "";
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		league_code = league_code.toUpperCase();
	 		sqlcmd="select league_id,league_name_vn from score24.leagues where status <> '1500' and league_code='"+league_code+"'";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		if(rs.next())
	 		{
	 			league_name_vn = rs.getString("league_name_vn");
	 			league_id = rs.getString("league_id");
	 		}
	 		rs.close();
	 		if(league_id!=null && league_id.length()>0)
	 		{
	 			sqlcmd="select a.match_id,a.register_time,a.round_id,b.round_name_vn from score24.livescore_subscriber a,score24.rounds b where a.type='2' and a.match_id=b.round_id and a.league_id='"+league_id+"' and a.msisdn='"+msisdn+"' and b.status <> '1500' order by b.time";
				rs = db.stmt.executeQuery(sqlcmd);
				while(rs.next())
				{	
					MatchInfo match = new MatchInfo();	
					match.setEventPrimaryID(league_id);
					match.setParentID(rs.getString("round_id"));
					match.setMatchID(rs.getString("match_id"));					
					match.setMatchName(rs.getString("round_name_vn"));
					match.setMatchDate(rs.getString("register_time"));
					arrRounds.add(match);
				}
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return arrRounds;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Huy dang ky tran dau kenh SMS
	 *		return 0	: Co loi trong qua trinh huy dang ky
	 *		return >0	: Qua trinh huy tran dau thanh cong
	 */
	 public int unRegMatchs(String league_code,String msisdn,String matchs,String type)
	 {
	 	int response=0;
	 	ArrayList arr = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	int tmp = 0;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		String[] arrMatchs = matchs.trim().split(" ");
	 		league_code = league_code.toUpperCase();
	 		sqlcmd = "select a.match_id from score24.livescore_subscriber a,score24.matches b where a.msisdn='"+msisdn+"' and a.match_id=b.match_id and a.type='0' and b.is_active='1000' order by b.match_date";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		while(rs.next())
	 		{
	 			arr.add(rs.getString("match_id"));
	 		}
	 		rs.close();
	 		if(arr.size()>0)
	 		{
	 			Subscriber sub = new Subscriber();
	 			sub.makeConnection();
	 			for(int i=0;i<arr.size();i++)
	 			{
	 				tmp = i+1;
	 				for(int j=0;j<arrMatchs.length;j++)
	 				{
	 					if(tmp==Integer.parseInt(arrMatchs[j].toString()))
	 					{
	 						response+=sub.deleteSubscriber(msisdn,arr.get(i).toString());
	 					}
	 				}
	 			}
	 			sub.removeConnection();
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return response;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Huy dang ky doi bong kenh SMS
	 *		return 0	: Co loi trong qua trinh huy dang ky
	 *		return >0	: Qua trinh huy doi bong thanh cong
	 */
	 public int unRegTeams(String league_code,String msisdn,String teams,String type)
	 {
	 	int response=0;
	 	ArrayList arr = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	int tmp = 0;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		league_code = league_code.toUpperCase();
	 		String[] arrTeams = null;
	 		if(teams.indexOf(" ")>=0)
	 			arrTeams = teams.trim().split(" ");	
	 		else if(teams.indexOf(",")>=0)
	 			arrTeams = teams.split(","); 		
	 		sqlcmd = "select a.match_id from score24.livescore_subscriber a,score24.team_info b where a.msisdn='"+msisdn+"' and a.match_id=b.team_id and a.type='1' order by b.full_name";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		while(rs.next())
	 		{
	 			arr.add(rs.getString("match_id"));
	 		}
	 		rs.close();
	 		if(arr.size()>0)
	 		{
	 			Subscriber sub = new Subscriber();
	 			sub.makeConnection();
	 			for(int i=0;i<arr.size();i++)
	 			{
	 				tmp = i+1;
	 				for(int j=0;j<arrTeams.length;j++)
	 				{
	 					if(tmp==Integer.parseInt(arrTeams[j].toString()))
	 					{
	 						response+=sub.deleteSubscriber(msisdn,arr.get(i).toString());
	 					}
	 				}
	 			}
	 			sub.removeConnection();
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.getConnectionAndVerify();
	 	}
	 	return response;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Huy dang ky vong dau kenh SMS
	 *		return 0	: Co loi trong qua trinh huy dang ky
	 *		return >0	: Qua trinh huy vong dau thanh cong
	 */
	 public int unRegRounds(String league_code,String msisdn,String rounds,String type)
	 {
	 	int response=0;
	 	ArrayList arr = new ArrayList();
	 	String sqlcmd="";
	 	String league_id = "";
	 	java.sql.ResultSet rs = null;
	 	int tmp = 0;
	 	try
	 	{
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		league_code = league_code.toUpperCase();
	 		String[] arrRounds = null;
	 		if(rounds.indexOf(" ")>=0)
	 			arrRounds = rounds.trim().split(" ");	
	 		else if(rounds.indexOf(",")>=0)
	 			arrRounds = rounds.split(","); 		
	 		sqlcmd = "select a.match_id from score24.livescore_subscriber a,score24.rounds b where a.msisdn='"+msisdn+"' and a.match_id=b.round_id and a.type='2' and b.status='1000' order by b.time";
	 		rs = db.stmt.executeQuery(sqlcmd);
	 		while(rs.next())
	 		{
	 			arr.add(rs.getString("match_id"));
	 		}
	 		rs.close();
	 		if(arr.size()>0)
	 		{
	 			Subscriber sub = new Subscriber();
	 			sub.makeConnection();
	 			for(int i=0;i<arr.size();i++)
	 			{
	 				tmp = i+1;
	 				for(int j=0;j<arrRounds.length;j++)
	 				{
	 					if(tmp==Integer.parseInt(arrRounds[j].toString()))
	 					{
	 						response+=sub.deleteSubscriber(msisdn,arr.get(i).toString());
	 					}
	 				}
	 			}
	 			sub.removeConnection();
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return response;
	 }
	 ///////////////////////////////////////////////////////////////////////////	 
	 public int regMatchsWeb(String league_code,String msisdn,String match_id,String type)
	 {
	 	int response = 0;
	 	String sqlcmd = "";
	 	java.sql.ResultSet rs =null;
	 	int tmp=0;
	 	league_code = league_code.toUpperCase();
	 	try
	 	{ 	
	 		db.getConnectionAndVerify();
	 		db.stmt = db.con.createStatement();
	 		if(type.equals("0"))
	 		{
	 			Match matchClient = new Match();
	 			matchClient.makeConnection();
		 		MatchInfo match = matchClient.getMatch(match_id);
		 		if(match!=null)
		 		{
		 			SubscriberInfo sub = new SubscriberInfo();
		 			sub.setLeagueID(match.getEventPrimaryID());
		 			sub.setRoundID(match.getParentID());
		 			sub.setMatchID(match.getMatchID());
		 			sub.setUserStatus("0");
		 			sub.setMsisdn(msisdn);
		 			sub.setType(type);
		 			Subscriber client = new Subscriber();
		 			client.makeConnection();
		 			response+=client.insertSubscriber(sub);
		 			client.removeConnection();
		 		}
		 		matchClient.removeConnection();
	 		}		 	
	 		else
	 		{
	 			String league_id = "";	 			
	 			try
	 			{	 				
	 				sqlcmd="select * from score24.leagues where league_code='"+league_code+"'";
	 				rs = db.stmt.executeQuery(sqlcmd);
	 				if(rs.next())
	 					league_id = rs.getString("league_id");
	 				rs.close();
	 				if(league_id.length()>0)
	 				{
	 					SubscriberInfo sub = new SubscriberInfo();
	 					sub.setLeagueID(league_id);
	 					sub.setRoundID(match_id);
	 					sub.setMatchID(match_id);
	 					sub.setUserStatus("0");
	 					sub.setMsisdn(msisdn);
	 					sub.setType(type);
	 					Subscriber client = new Subscriber();
	 					client.makeConnection();
		 				response+=client.insertSubscriber(sub);
		 				client.removeConnection();
	 				}
	 			}
	 			catch(Exception e)
	 			{
	 				e.printStackTrace();
	 			}
	 		}
	 		db.putConnection();
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 		db.putConnection();
	 	}
	 	return response;	 	
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 public int unRegMatchsWeb(String msisdn,String match_id,String type)
	 {
	 	int response=0;
	 	String sqlcmd = "";
	 	java.sql.ResultSet rs =null;
	 	try
	 	{
	 		Subscriber sub = new Subscriber();
	 		sub.makeConnection();
	 		response+= sub.deleteSubscriber(msisdn,match_id);
	 		sub.removeConnection();	 		
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 	}
	 	return response;
	 }
	 ///////////////////////////////////////////////////////////////////////////
	 /*
	 *	Phuong thuc lay danh sach cac tran dau da dang ky
	 *		return 0	: Co loi trong qua trinh dang ky
	 *		return 1 	: Dang ky thanh cong
	 */	 ///////////////////////////////////////////////////////////////////////////
	 public static void main(String[] args)
	 {
	 	try
	 	{
	 		long t1 = System.currentTimeMillis();
	 		LiveScoreBean pool = new LiveScoreBean();
	 		pool.makeConnection();
	 		java.util.ArrayList arr = pool.getListofStartedMatchs("PRE",10);
	 		for(int i=0;i<arr.size();i++)
	 		{
	 			neo.score24.object.MatchInfo match = (neo.score24.object.MatchInfo)arr.get(i);
	 			System.out.println((i+1)+" "+match.getMatchDate()+" - "+match.getMatchName()+" - "+match.getResult());
	 		}
	 		pool.removeConnection();
	 		long t2 = System.currentTimeMillis();
		 		System.out.println("Thoi gian thuc hien: "+(t2-t1)+" mini giay");
	 		
	 	}
	 	catch(Exception ex)
	 	{
	 		ex.printStackTrace();
	 	}
	 }
}
