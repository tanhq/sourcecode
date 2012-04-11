package neo.score24.services;

import neo.score.db.DBConnection;
import neo.score24.object.*;

import java.io.File;
import org.w3c.dom.*;
import java.io.StringWriter;
import java.text.*;
import neo.score24.parseScore24;
import com.enterprisedt.net.ftp.*;
import java.util.*;


class roundStatus 
{
	public static java.sql.ResultSet rs =null;
	public static DBConnection db = null;
	
	
	public static void main(String[] args)
	{
		String sqlcmd = "";
		ArrayList arrRound = new ArrayList();
		ArrayList arrHint = new ArrayList();
		try 
		{	
			while (true)
			{
				try				
				{
					
		            db = new DBConnection(); 
		            db.getConnectionAndVerify();
		            sqlcmd = "select * from score24.rounds where status!=1500 and time+2<=sysdate order by time desc";
		            rs = db.stmt.executeQuery(sqlcmd);
		            int dem = 0;
		            while(rs.next())
		            {
		            	
		            	arrRound.add(rs.getString("round_id"));
		            	//System.out.println(dem+". Round ID = "+rs.getString("round_id")+" - "+rs.getString("round_name"));
		            }	
		            rs.close();	
		            for(int i=0;i<arrRound.size();i++)
		            {
		            	sqlcmd = "select * from score24.matches where parent_id='"+arrRound.get(i).toString()+"' and is_active=1000";
		            	rs = db.stmt.executeQuery(sqlcmd);
		            	if(!rs.next())
		            		arrHint.add(arrRound.get(i));
		            	rs.close();
		            }    
		            for(int i=0;i<arrHint.size();i++)
		            {
		            	dem++;
		            	System.out.println(dem+". Round ID = "+arrHint.get(i));
		            	sqlcmd = "update score24.rounds set status=1500 where round_id='"+arrHint.get(i).toString()+"'";
		            	db.stmt.executeUpdate(sqlcmd);
		            }       
		            db.putConnection();
	 			}
	 			catch(Exception e)
	 			{
					e.printStackTrace();					
				}
				finally
				{
					db.putConnection();
				}
				System.out.println((new Date())+"Completed, now sleep in 1 day...");
				System.out.println("......................................................................");		       
				Thread.sleep(1000*60*1440);//1 minute
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		
	}
}
