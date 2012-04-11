package com.mbf.mobilelist.client;

import oasis.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import java.util.Properties;
import java.util.*;

public class RTCClient {	
	
	private PrepaidAccountService pas=null;
	private long lastUseTime = 0;
	public int currentId = -1;
	
	public static void main(String[] arg){
		try {
			RTCClient cl = new RTCClient();
			cl.connect();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

  	////////////////////////////////////////////////////////////////////////////
  	public void connect() throws Exception{  		
		Properties properties = new Properties();
		properties.put("org.omg.CORBA.ORBInitialPort", "20000");
		properties.put("org.omg.CORBA.ORBInitialHost", "10.151.9.50");
		ORB orb = ORB.init((String[])null, properties);		
		org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);		
		String name = "redknee/product/s2100/oasis/UserAuth";
        UserAuth ua = UserAuthHelper.narrow(ncRef.resolve_str(name));
        short rt = (short)-1;
        try {
			pas = ua.loginUser("mobilist","mobilist",rt);
        } catch (Exception ex){
        	ex.printStackTrace();
        	throw ex;
        }

      	(new Thread(new Runnable() {
		  	////////////////////////////////////////////////////////////////////////////
		  	public void run() {
		  		try {
			  		while (true){
		  				if (System.currentTimeMillis()-lastUseTime>300*1000){//5 phut ko lam gi se dong connection
		  					System.out.println("Close Idle Connection.");
		  					disconnect();
		  					break;
		  				}
		  				Thread.sleep(500);
			  		}
				} catch (Exception e){
					e.printStackTrace();
				}
		  	}
		  	////////////////////////////////////////////////////////////////////////////
      	})).start();	    
  	}
  	
  	public void disconnect() throws Exception{
  		try {
  		} catch (Exception e){
  		}
  	}

  	public int debit(String msisdn, int amount){
  		int rs = -1;
		String currencyType = "VND";
		boolean balFlag = true;
		String erReference = "www.mobifone.com.vn";		
  		try {
			//String msisdn="841219089654";
			oasis.param.Parameter[] inputParamSet = new oasis.param.Parameter[1];
			inputParamSet[0] = new oasis.param.Parameter();
			inputParamSet[0].parameterID = (short)0;
			inputParamSet[0].value = new oasis.param.ParameterValue();
			inputParamSet[0].value.intValue(0);
			
			org.omg.CORBA.StringHolder expiry = new org.omg.CORBA.StringHolder();
			org.omg.CORBA.IntHolder balance = new org.omg.CORBA.IntHolder();
			org.omg.CORBA.IntHolder shortfall = new org.omg.CORBA.IntHolder();
			oasis.param.ParameterSetHolder outputParamSet = new oasis.param.ParameterSetHolder();			
	
			rs = pas.requestDebit(msisdn,amount,currencyType,balFlag,
				inputParamSet,erReference,balance,shortfall,outputParamSet
			);			
		} catch (Exception e){
			System.out.println("Try to debit failed: "+e);
			System.out.println("Retry...");
			try {
				this.connect();
				oasis.param.Parameter[] inputParamSet = new oasis.param.Parameter[1];
				inputParamSet[0] = new oasis.param.Parameter();
				inputParamSet[0].parameterID = (short)0;
				inputParamSet[0].value = new oasis.param.ParameterValue();
				inputParamSet[0].value.intValue(0);
				
				org.omg.CORBA.StringHolder expiry = new org.omg.CORBA.StringHolder();
				org.omg.CORBA.IntHolder balance = new org.omg.CORBA.IntHolder();
				org.omg.CORBA.IntHolder shortfall = new org.omg.CORBA.IntHolder();
				oasis.param.ParameterSetHolder outputParamSet = new oasis.param.ParameterSetHolder();			
		
				rs = pas.requestDebit(msisdn,amount,currencyType,balFlag,
					inputParamSet,erReference,balance,shortfall,outputParamSet
				);
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
		return rs;
  	}
  
}
