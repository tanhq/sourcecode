package neo.score24;

import com.enterprisedt.net.ftp.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.text.*;
import neo.score24.parseScore24;

class Score24
{
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
					FTPClient ftp = new FTPClient("10.252.20.65",21);
		            //ftp.debugResponses(true);
		            ftp.login("score24","score24");
		            System.out.println("Ket noi FTP ok!");
				    ftp.setType(FTPTransferType.BINARY);
		            ftp.setConnectMode(FTPConnectMode.PASV);					
		            ftp.chdir("/");	
		            FTPFile fileList[] = ftp.dirDetails("/");		           	 
		            int cnt=1; 
		            int cex=1;         
		            for (int i=0; i<fileList.length; i++)
		            {
		           		String filename = fileList[i].getName();		            
		            	File f = new File("D:/Utilities/Score24/resources/tmp/"+filename);
		            	if (!f.exists())
		            	{
		            		try
		            		{
				            	System.out.println("Copy file "+ filename+"...");
			            		ftp.get("D:/Utilities/Score24/resources/tmp/"+filename,filename);			            					            		
			            		//Phan tich file XML			            		
			            		System.out.println(cnt + " file(s) copied!\nImport to database...");
			            		cnt++;
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
					try 
					{
						ftp.quit();
					} 
					catch (Exception exp)
					{
						System.out.println("FTP Connection: "+exp.getMessage());
					}
	 			}catch(Exception e)
	 			{
					e.printStackTrace();					
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

