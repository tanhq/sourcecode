package com.utils;

import java.io.File;
import java.io.FileWriter;
import java.text.*;
import java.util.Date;
import com.enterprisedt.net.ftp.*;

public class CDRWriter implements java.io.Serializable{
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
  public CDRWriter(String filePath1,String filePath2,String fileExtension, int time2Change_,int maxNumber) {
    this.filePath1=filePath1;
    this.filePath2=filePath2;
    this.fileExtension=fileExtension;
    this.time2Change=time2Change_;
    this.maxNumber=maxNumber;
    timer=new Date();
    checkFileToMove();//Check all file to move to cdr
  }
  private void checkFileToMove(){
        if (System.currentTimeMillis()-lastMoveFile>1000*60){// 1 phut thuc hien check 1 lan
                createFile();
                try {
                    File file=new File(this.filePath1);
                    File destFile;
                    if(file.isDirectory()){
                      file.listFiles();
                      for(int i=0;i<file.listFiles().length;i++){
                        try{
                          if(this.expireDate(file.listFiles()[i].getName()) && fMoveFile==false)
                          {
                            fMoveFile = true;
                            long time1 = System.currentTimeMillis();

                            /*while (true)
                            {
                                if (!destFile.exists())
                                {
                                        break;
                                }
                                else
                                {
                                        tmp = tmp+1;
                                        destFile=new File(this.filePath2+"/"+"mobilist"+tmp);
                                }
                            }   */

                            //Chuyen file cuoc sang thu muc cdr
                            if(file.listFiles()[i].setLastModified(System.currentTimeMillis()))
                            {
                              String seq = "0000";
                              String tmp = t.format(new Date());
                              com.mbf.mobilelist.db.DBConnection db = new
                                  com.mbf.mobilelist.db.DBConnection();
                              db.getConnectionAndVerify();
                              String sqlcmd = "select score24.MOBILIST_CDR_SEQ.nextval from dual";
                              java.sql.ResultSet rs = db.stmt.executeQuery(sqlcmd);
                              if (rs.next())
                                seq = rs.getString(1);
                              rs.close();
                              if (seq.length() < 4) {
                                while (seq.length() < 4) {
                                  seq = "0" + seq;
                                }
                              }
                              db.putConnection();
                              tmp = tmp + seq;
                              destFile=new File(this.filePath2+"/"+"mobilist"+tmp);
                              file.listFiles()[i].renameTo(destFile);
                              //Chuyen file cuoc len FPT Server
                              try {
                                com.enterprisedt.net.ftp.FTPClient ftp = new FTPClient("10.252.20.37", 21);
                                ftp.login("cditvms", "vmsportalfromcdit");
                                System.out.println("Ket noi FTP OK");
                                ftp.setType(FTPTransferType.BINARY);
                                ftp.setConnectMode(FTPConnectMode.PASV);
                                ftp.chdir("/mobilist");
                                ftp.put(this.filePath2+"/"+"mobilist"+tmp, "mobilist"+tmp);
                                ftp.quit();
                              }
                              catch (Exception e) {
                                e.printStackTrace();
                              }
                              long time2 = System.currentTimeMillis();
                              if(fMoveFile==true && (time2-time1)>=1000*60);
                                 fMoveFile = false;

                            }
                          }
                        } catch(Exception e){
                          e.printStackTrace();
                        }
                      }
                    }
                        lastMoveFile = System.currentTimeMillis();
            } catch(Exception e){
              e.printStackTrace();
            }
        }

  }

  private void createFile(){
    try
    {
      String oldFileName=fileName;
       long tmp = Long.parseLong(t2.format(new Date()));
       tmp = (tmp / 1000)*1000;
       fileName="mobilist"+tmp+".log";
       System.out.println("Ten file cuoc: "+fileName);

       if (!oldFileName.equals(fileName))
       {
                  if (writer!=null) writer.close();
           File file=new File(filePath1+fileName);
           writer=new FileWriter(file,true);
       }

    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  //Ghi CDR theo cac tham so default
  public String write(String sender,String receiver,String timeReceive,int type){
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
    String billingRecord;
    billingRecord="04918:mobilist:";
    billingRecord +=sender+":";
    billingRecord+=timeReceive+":";
    billingRecord+=type+":1:";
     billingRecord+=receiver;
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
    Date newTime=new Date();
    Date oldTime=new Date();
    oldTime.setYear(Integer.parseInt(fileName.substring(8,12))-1900);
    oldTime.setMonth(Integer.parseInt(fileName.substring(12,14))-1);
    oldTime.setDate(Integer.parseInt(fileName.substring(14,16)));
    oldTime.setHours(Integer.parseInt(fileName.substring(16,18)));
    oldTime.setMinutes(Integer.parseInt(fileName.substring(18,20)));
    oldTime.setSeconds(Integer.parseInt(fileName.substring(20,22)));
    if(newTime.getTime()-oldTime.getTime()>this.time2Change)
      return true;
    return false;
  }

  public static void main(String args[])
  {
    CDRWriter writer=new CDRWriter("d:/temp/t/","d:/temp/cdr","txt",100000,9999);
    writer.checkFileToMove();
  }
}
