package neo.score.utils;


import java.io.File;
import java.io.FileWriter;
import java.text.*;
import java.util.Date;
import com.enterprisedt.net.ftp.*;
public class CDRWriter implements java.io.Serializable
{
  static boolean fMoveFile = false;
  String filePath1;
  String filePath2;
  String fileExtension;
  Date timer;
  int maxNumber;
  int time2Change;
  private SimpleDateFormat t = new SimpleDateFormat("yyyyMMdd");
  private SimpleDateFormat t2 = new SimpleDateFormat("yyyyMMddHHmmss");
  FileWriter writer;
  String fileName="";  
  String[] serviceName={"FreeMessage","Sms","Ringtone","Logo","PMessage","GroupLogo","Mms","Ota","MailNotification","ErrorCommand","GroupSMS","Reserved","RingtoneMobile","LogoMobile","PMessageMobile","GroupLogoMobile","Calendar","MobifunLiveSMS","MobiList","LiveScore"};
  private static long lastMoveFile = 0;
  public CDRWriter(String filePath1,String filePath2,String fileExtension, int time2Change_,int maxNumber) 
  {
    this.filePath1=filePath1;
    this.filePath2=filePath2;
    this.fileExtension=fileExtension;
    this.time2Change=time2Change_;
    this.maxNumber=maxNumber;
    timer=new Date();
    checkFileToMove();//Check all file to move to cdr
  }
  private void checkFileToMove()
  {
        if (System.currentTimeMillis()-lastMoveFile>1000*60)
        {// 1 phut thuc hien check 1 lan
                createFile();
                try 
                {	  	
                    File file=new File(this.filePath1);
                    File destFile;	
                    if(file.isDirectory())
                    {
                      file.listFiles();
                      
                      for(int i=0;i<file.listFiles().length;i++)
                      {
                        try
                        {                         
                          if(this.expireDate(file.listFiles()[i].getName()) && fMoveFile==false)
                          {
                          	fMoveFile = true;
                            long time1 = System.currentTimeMillis();
                            System.out.println("File need to change: "+file.listFiles()[i].getName());                            
                            
                            /*while (true)
                            {
                                if (!destFile.exists()) 
                                {
                                    break;
                                } 
                                else 
                                {
                                    tmp = tmp+1;
                                    destFile=new File(this.filePath2+"/"+"livescore"+tmp);
                            	}
                            }*/
                            if(file.listFiles()[i].setLastModified(System.currentTimeMillis())) 
                            {   
                            	String seq = "0000";
	                            String tmp = t.format(new Date());
	                            neo.score.db.DBConnection db = new
	                            neo.score.db.DBConnection();
	                            db.getConnectionAndVerify();
	                            String sqlcmd = "select score24.LIVESCORE_CDR_SEQ.nextval from dual";
	                            System.out.println(sqlcmd);
	                            java.sql.ResultSet rs = db.stmt.executeQuery(sqlcmd);
	                            if (rs.next())
	                              seq = rs.getString(1);
	                            rs.close();
	                            if (seq.length() < 4) 
	                            {
	                              while (seq.length() < 4) 
	                              {
	                                seq = "0" + seq;
	                              }
	                            }
	                            db.putConnection();
	                            tmp = tmp + seq;
	                            
	                            destFile=new File(this.filePath2+"/"+"livescore"+tmp);                                   
                                file.listFiles()[i].renameTo(destFile); 
                                long time2 = System.currentTimeMillis();
                              	if(fMoveFile==true && (time2-time1)>=1000*60);
                                 	fMoveFile = false;
                            }   
                          }
                        } 
                        catch(Exception e)
                        {
                        	e.printStackTrace();
                          System.out.println("Exception in setFileName1: "+e);
                        }
                      }
                    }
                    lastMoveFile = System.currentTimeMillis();
            } 
            catch(Exception e)
            {
              System.out.println("Exception in setFileName2: "+e);
            }
        }
  }

  private void createFile()
  {
    try
    {
        String oldFileName=fileName;
        long tmp = Long.parseLong(t2.format(new Date()));
        tmp = (tmp / 1000)*1000;
        fileName="livescore"+tmp+".log";
        if (!oldFileName.equals(fileName))
        {
        	//System.out.println("Change file name................"+this.fileName);
            if (writer!=null) writer.close();
            File file=new File(filePath1+fileName);
            writer=new FileWriter(file,true);
        }

    } 
    catch(Exception e)
    {
      System.out.println("Exception in write billing record to file CDR: "+e);
    }
  }

  //Ghi CDR theo cac tham so default
  public String write(String receiver,String timeReceive,String volumCharge){
    if(receiver.startsWith("090")||
       receiver.startsWith("091")||
       receiver.startsWith("092")||
       receiver.startsWith("093")||
       receiver.startsWith("094")||
       receiver.startsWith("095")||
       receiver.startsWith("096")||
       receiver.startsWith("097")||
       receiver.startsWith("098")||
       receiver.startsWith("012")||
       receiver.startsWith("016")
      )
      receiver="84"+receiver.substring(1);
    String billingRecord="";
    
    billingRecord+="04919:livescore:";    
    billingRecord+=receiver+":";
    billingRecord+=timeReceive+":";    
    billingRecord+=volumCharge+":1";    
    this.writeToFile(billingRecord);
    return fileName;
  }

  private void writeToFile(String billingRecord){
    this.checkFileToMove();
    try{
        this.createFile();
        this.writer.write(billingRecord+"\r\n");
        this.writer.flush();
    } catch(Exception e){
        e.printStackTrace();
        System.out.println("Co loi khi ghi ban ghi cuoc vao file!");
    }
  }


  public void close(){
    try{
      this.writer.close();
    }
    catch(Exception e){
      System.out.println("Exeption in close: "+e);
    }
  }

  public boolean expireDate(String fileName){
    System.out.println("Check expireDate filename: "+fileName);
    Date newTime=new Date();
    Date oldTime=new Date();    
    oldTime.setYear(Integer.parseInt(fileName.substring(9,13))-1900);
    oldTime.setMonth(Integer.parseInt(fileName.substring(13,15))-1);
    oldTime.setDate(Integer.parseInt(fileName.substring(15,17)));
    oldTime.setHours(Integer.parseInt(fileName.substring(17,19)));
    oldTime.setMinutes(Integer.parseInt(fileName.substring(19,21)));
    oldTime.setSeconds(Integer.parseInt(fileName.substring(21,23)));
    if(newTime.getTime()-oldTime.getTime()>this.time2Change)
      return true;
    return false;
  }

  public static void main(String args[])
  {
  	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
    Date receive,send;
    receive=new Date();
    send=new Date();
    CDRWriter writer=new CDRWriter("G:/CDR/livescore_tmp/","G:/CDR/livescore/","txt",100000,9999);
    writer.write("84906044701",sdf.format(receive),"1");
  }
}