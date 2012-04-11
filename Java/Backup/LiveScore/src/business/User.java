package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import neo.score.object.UserInfo;

public class User 
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public String sqlcmd = "";
	public User()
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
	public int insertUser(UserInfo user)
	{	
		String username,password,fullname,donvi,tendonvi,chucdanh,email;
		int roll,is_enable,is_livescore;	
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			username=user.getUsername();
			password=user.getPassword();
			fullname=user.getFullname();
			donvi=user.getDonvi();
			tendonvi=user.getTendonvi();
			chucdanh=user.getChucdanh();
			email=user.getEmail();
			roll=user.getRoll();
			is_enable=user.getIs_enable();
			is_livescore=user.getIs_livescore();
			
			if(username==null||username.length()==0)
				return response;
				
			if(password==null||password.length()==0)
				return response;
				
			if(fullname==null||fullname.length()==0)
				return response;
			
			if(donvi==null||donvi.length()==0)
				return response;
				
			if(tendonvi==null||tendonvi.length()==0)
				return response;
				System.out.println("Here OK");
			if(chucdanh==null||chucdanh.length()==0)
				return response;
			if(email==null)
				email="";
			sqlcmd="insert into score24.livescore_users values('"+username+"','"+password+"',"+roll+",'"+fullname+"','"+donvi+"','"+tendonvi+"','"+chucdanh+"','"+email+"',"+is_enable+","+is_livescore+",sysdate,score24.seq_user_id.nextval)";
			System.out.println(sqlcmd);
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
	public int updateUser(UserInfo user)
	{
		String user_id,username,password,fullname,donvi,tendonvi,chucdanh,email;
		int roll,is_enable,is_livescore;
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			user_id=user.getUserId();
			username=user.getUsername();
			password=user.getPassword();
			fullname=user.getFullname();
			donvi=user.getDonvi();
			tendonvi=user.getTendonvi();
			chucdanh=user.getChucdanh();
			email=user.getEmail();
			roll=user.getRoll();
			is_enable=user.getIs_enable();
			is_livescore=user.getIs_livescore();
			
			if(user_id==null||user_id.length()==0)
				return response;
			if(username==null||username.length()==0)
				return response;
			if(password==null||password.length()==0)
				return response;
			if(fullname==null||fullname.length()==0)
				return response;
			if(donvi==null||donvi.length()==0)
				return response;
			if(tendonvi==null||tendonvi.length()==0)
				return response;
			if(chucdanh==null||chucdanh.length()==0)
				return response;
			if(email==null)
				email="";
			sqlcmd="update score24.livescore_users set username='"+username+"',password='"+password+"', roll="+roll+", fullname='"+fullname+"',donvi='"+donvi+"',tendonvi='"+tendonvi+"',chucdanh='"+chucdanh+"',email='"+email+"',is_enable="+is_enable+",is_livescore="+is_livescore+",last_access=sysdate where is_livescore=1 and userid='"+user_id+"'";
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
	public int deleteUser(String userid)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			if(userid==null||userid.length()==0)
				return response;
			sqlcmd="delete from score24.livescore_users where userid='"+userid+"'";
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
	public int setEnable(String username, int is_livescore,int is_enable)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			if(username==null||username.length()==0)
				return response;
			sqlcmd="update score24.livescore_users set is_enable="+is_enable+" where userid='"+username+"' and is_livescore="+is_livescore;
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
	public int setRoll(String username, int is_livescore,int roll)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			if(username==null||username.length()==0)
				return response;
			sqlcmd="update score24.livescore_users set roll="+roll+" where username='"+username+"' adn is_livescore="+is_livescore;
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
	
	public static void main(String[] arg){
		User dao = new User();
		dao.makeConnection();
		UserInfo user = new UserInfo();
		user.setChucdanh("adad");
		user.setFullname("abc");
		user.setDonvi("donvi");
		user.setTendonvi("abc");
		user.setEmail("email");
		user.setIs_enable(1);
		user.setIs_livescore(1);
		user.setPassword("pass");
		user.setRoll(1);
		user.setUsername("username");
		
		System.out.println(dao.insertUser(user));
		dao.removeConnection();
	}

}
