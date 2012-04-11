package com.mbf.mobilelist.client;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import java.util.Properties;
import com.mbf.mobilelist.*;
import com.mbf.mobilelist.objects.*;
import java.util.*;
import com.mbf.mobilelist.db.DBConnection;
import java.text.SimpleDateFormat;

public class mobiListClient {
	
	public MobiList mobiList;
	
	public int createGroup(String groupName, String mobileGroup){
		try{
			return mobiList.createGroup(groupName, mobileGroup);
		}catch(Exception ex){
			System.out.println("Exception from createGroup() function: "+ex.toString());
			return -1;
		}
	}
	
	public int createGroup(String groupName, String mobileGroup, String mobileNumber){
		try{
			return mobiList.createGroup(groupName, mobileGroup,mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from createGroup() function: "+ex.toString());
			return -1;
		}		
	}
	
	public int createGroup(String groupName, String mobileGroup, String mobileNumber, String groupDesc){
		try{
			return mobiList.createGroup(groupName, mobileGroup,mobileNumber, groupDesc);
		}catch(Exception ex){
			System.out.println("Exception from createGroup() function: "+ex.toString());
			return -1;
		}		
	}
	
	public ArrayList getMobileOfGroup(String groupName, String mobileNumber){
		try{
			return mobiList.getMobileOfGroup(groupName, mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from getMobileOfGroup() function: "+ex.toString());
			return null;
		}
	}
	
	public String addMobileToGroup(String mobileNumber, String groupName, String mobileGroup){
		try{
			return mobiList.addMobileToGroup(mobileNumber,groupName,mobileGroup);
		}catch(Exception ex){
			System.out.println("Exception from addMobileToGroup() function: "+ex.toString());
			return "Co loi xay ra trong qua trinh them so dien thoai! Xin quy khach vui long thu lai.";
		}
	}
	
	public int addMobileToGroup(String groupId, String mobileNumbers){
		try{
			return mobiList.addMobileToGroup(groupId,mobileNumbers);
		}catch(Exception ex){
			System.out.println("Exception from addMobileToGroup() function: "+ex.toString());
			return 0;
		}		
	}
		
	public int removeMobileFromGroup(String groupName, String mobileGroup, String mobileNumber){
		try{
			return mobiList.removeMobileFromGroup(groupName,mobileGroup,mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from removeMobileFromGroup() function: "+ex.toString());
			return 0;
		}		
	}
	
	public int deleteGroup(String groupName, String mobileGroup){
		try{
			return mobiList.deleteGroup(groupName,mobileGroup);
		}catch(Exception ex){
			System.out.println("Exception from deleteGroup() function: "+ex.toString());
			return 0;
		}				
	}
	
	public String deleteGroup(String groupName, String mobileGroup, String mobileNumber){
		try{
			return mobiList.deleteGroup(groupName, mobileGroup, mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from deleteGroup() function: "+ex.toString());
			return "Co loi xay ra trong qua trinh xoa nhom! Xin Quy khach vui long thu lai.";
		}						
	}
	
	public boolean customerIsExist(String mobileNumber){
		try{
			return mobiList.customerIsExist(mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from customerIsExist() function: "+ex.toString());
			return false;
		}				
		
	}
	
	public boolean mobileIsExist(String mobileGroup, String mobileNumber){
		try{
			return mobiList.mobileIsExist(mobileGroup, mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from mobileIsExist() function: "+ex.toString());
			return false;
		}						
	}
	
	public boolean groupIsExist(String groupName, String mobileGroup){
		try{
			return mobiList.groupIsExist(groupName, mobileGroup);
		}catch(Exception ex){
			System.out.println("Exception from groupIsExist() function: "+ex.toString());
			ex.printStackTrace();
			return false;
		}						
	}
	
	public boolean groupNameIsValid(String groupName){
		try{
			return mobiList.groupNameIsValid(groupName);
		}catch(Exception ex){
			System.out.println("Exception from groupNameIsValid() function: "+ex.toString());
			return false;
		}						
			
	}

	
	public ArrayList getAllGroup(String mobileGroup){
		try{
			return mobiList.getAllGroup(mobileGroup);
		}catch(Exception ex){
			System.out.println("Exception from getAllGroup() function: "+ex.toString());
			ex.printStackTrace();
			return null;
		}						
	}
	
	public ArrayList getAllMobileFromGroup(String mobileGroup, String groupName){
		try{
			return mobiList.getAllMobileFromGroup(mobileGroup, groupName);
		}catch(Exception ex){
			System.out.println("Exception from getAllMobileFromGroup() function: "+ex.toString());
			return null;
		}								
	}
	
	
	public objGroup getGroupInfor(int groupId){
		try{
			return mobiList.getGroupInfor(groupId);
		}catch(Exception ex){
			System.out.println("Exception from getGroupInfor() function: "+ex.toString());
			return null;
		}										
	}
	
	
	public int deleteGroupById(String groupId){
		try{
			return mobiList.deleteGroupById(groupId);
		}catch(Exception ex){
			System.out.println("Exception from deleteGroupById() function: "+ex.toString());
			return -1;
		}										
	}
	
	public int deleteMobiles(int groupId, String mobileNumber){
		try{
			return mobiList.deleteMobiles(groupId,mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from deleteMobiles() function: "+ex.toString());
			return -1;
		}												
	}
	
	public int updateMobile(int groupId, String oldMobileNumber, String newMobileNumber){
		try{
			return mobiList.updateMobile(groupId, oldMobileNumber, newMobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from updateMobile() function: "+ex.toString());
			return -1;
		}												
	}
	
	public int updateGroup(int groupId, String groupName, String groupDesc){
		try{
			return mobiList.updateGroup(groupId, groupName, groupDesc);
		}catch(Exception ex){
			System.out.println("Exception from updateGroup() function: "+ex.toString());
			return -1;
		}												
	}
	
	
	public int deleteMobileFromGroup(String groupName, String mobileGroup, String mobileNumber){
		try{
			return mobiList.deleteMobileFromGroup(groupName, mobileGroup, mobileNumber);
		}catch(Exception ex){
			System.out.println("Exception from deleteMobileFromGroup() function: "+ex.toString());
			return 0;
		}														
		
	}
	
	public int updateGroup(int groupId, String groupName, String groupDesc, String mobileNumbers){
		try{
			return mobiList.updateGroup(groupId,groupName,groupDesc,mobileNumbers);
		}catch(Exception ex){
			System.out.println("Exception from updateGroup() function: "+ex.toString());
			return 0;
		}														
		
	}
	
	public ArrayList getAllMobileFromGroup(int groupId){
		try{
			return mobiList.getAllMobileFromGroup(groupId);
		}catch(Exception ex){
			System.out.println("Exception from getAllMobileFromGroup() function: "+ex.toString());
			return null;
		}														
	}
	
	public void sendSMS(String sender,String receiver,String content){
		try
		{
			mobiList.sendSMS(sender, receiver, content);
    	}
    	catch(Exception e)
    	{
      		e.printStackTrace();
    	}
	}
	
	public void sendSMS(String sender,String receiver,String content, String host, int port){
		try
		{
			mobiList.sendSMS(sender, receiver, content, host, port);
    	}
    	catch(Exception e)
    	{
      		e.printStackTrace();
    	}
	}

	/*
	 *	Write log to MobiList
	 *	sender: 8026
	 */
	public void toLogMobiList(String sender, String receiver, String content){		
        try{
        	mobiList.toLogMobiList(sender, receiver, content);        	
        }catch(Exception ex){
        	ex.printStackTrace();
        }        		
	}
	
	//Ham ghi file CDR
    public void toCDR(String sender,String receiver,int type)
    {
      try
      {
        	mobiList.toCDR(sender,receiver,type);        	
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }
	
	
	public void init(){
		InitialContext 	jndiContext;
		Object 			ref;
		MobiListHome 	home;		
		
		try{
			Properties jndiProps = new Properties();
			jndiContext = new InitialContext(jndiProps);
			
      		jndiProps.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
      		//jndiProps.setProperty("java.naming.provider.url", "10.252.2.25:1099");
      		jndiProps.setProperty("java.naming.provider.url", "10.252.20.66:1099");
      		jndiProps.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");      		
      		jndiContext = new InitialContext(jndiProps);
			
			ref = jndiContext.lookup("mobilist/ejb");
			home = (MobiListHome)PortableRemoteObject.narrow(ref,MobiListHome.class);
			mobiList = home.create();
		}catch(Exception ex){
			System.out.println("Exception from initialcontext: "+ex.toString());
		}
		
		System.out.println("end initial context!");
		
	}
	
	public void init(String host, String port){
		InitialContext 	context;
		Object 			ref;
		MobiListHome 	home;	
		
		try{
			Properties pros = new Properties()	;
			context = new InitialContext(pros);

      		pros.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
      		pros.setProperty("java.naming.provider.url", host+":"+port);
      		pros.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");      		
			
			context = new InitialContext(pros);
			
			ref 	= context.lookup("mobilist/ejb");
			home 	= (MobiListHome)PortableRemoteObject.narrow(ref,MobiListHome.class);
			mobiList= home.create();
			
		}catch(Exception ex){
			System.out.println("Exception from initialcontext: "+ex.toString());
		}
	}
	
	public void toMobileNumber(String mobileNumber){
		String validRange = "0123456789";
		for(int i=0;i<mobileNumber.length();i++){
			char at = mobileNumber.charAt(i);
			if(validRange.indexOf(at)==-1)
				System.out.println("not valid character: "+at);
			
		}
	}
	
	
	
	public static void main(String[] args){
		try
		{		
			mobiListClient client = new mobiListClient();
			client.init();	
			client.sendSMS("918","841228333000","sfsf");
			//System.out.println("sfsfs");
			/*boolean test = client.customerIsExist("84986143389");
			boolean test = client.groupIsExist("test from ejb aa","84986143385");
			boolean test = client.mobileIsExist("84986143385","09");
			
			if(test)
				System.out.println("row effect: OK");
			else
				System.out.println("row effect: FAIL");
			int i = client.createGroup("Single Group","0986143385");
			System.out.println("result: "+i);*/
			
			//client.toMobileNumber("8496143str385");
			//removeMobileFromGroup
			
			//int i = client.removeMobileFromGroup("groupname","mobilegroup","mobile");
			//ArrayList arr = client.getAllGroup("84906044701");
			//System.out.println("status from web: "+i);
			int row=client.deleteGroupById("204766");
			System.out.println(row);
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
