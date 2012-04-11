package neo.score24.services;

import com.neo.wap.utils.*;
import java.util.Date;


class publicService 
{
	public static java.sql.ResultSet rs =null;
	public static com.neo.wap.utils.DBConnection db = null;
	public static String sqlcmd = "";
	public static java.util.ArrayList arrID = new java.util.ArrayList();
	
	public static void main(String[] args)
	{
		try
		{
			while (true)
			{
				try
				{
					db = new com.neo.wap.utils.DBConnection();
					db.getConnectionAndVerify();
					db.stmt=db.con.createStatement();
					sqlcmd = "select * from my_wap.news_content where is_delay=1 and sysdate-delay_time>=0";
					rs = db.stmt.executeQuery(sqlcmd);
					while(rs.next())
					{
						arrID.add(rs.getString("id"));
					}
					rs.close();
					if(arrID!=null && arrID.size()>0)
					{
						//Dat lai trang thai xuat ban
						for(int i=0;i<arrID.size();i++)
						{						
							sqlcmd = "update my_wap.news_relation set show_homepage=1 where ict_id='"+arrID.get(i).toString()+"'";
							int n = db.stmt.executeUpdate(sqlcmd);
							if(n>0)
							{
								//Dat lai trang thai duyet va hen gio
								sqlcmd = "update my_wap.news_content set is_delay=0, is_verify=1 where id='"+arrID.get(i).toString()+"'";
								db.stmt.executeUpdate(sqlcmd);
							}
						}
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
