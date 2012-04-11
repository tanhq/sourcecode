package com.neo.wap;


import java.util.*;
import java.text.SimpleDateFormat;

public class CheckDBWAP3GConnection implements Runnable,java.io.Serializable{
	public Thread thread;
	public static DBWAP3GConnection db = null;
	private static long lastCheckTime = 0;
	private static int count=0;
	
	public CheckDBWAP3GConnection(){
	}

	public void run(){
		try {
			//while (true) {
				try {
					if (System.currentTimeMillis() - lastCheckTime>30000){// 30 giay
						lastCheckTime = System.currentTimeMillis();
						//System.out.println("DB Connection checking is running...");
						processData();
					}
				} catch (Exception ez){
					ez.printStackTrace();
				}
				//System.out.println("DB Connection checking is sleeping...");
				//Thread.sleep(1000);
			//}
		} catch (Exception e){
		}
		count--;
	}
	////////////////////////////////////////////////////////////////////////////
	private void processData(){
		try {
			if (db==null) db = new DBWAP3GConnection();
			int max=db.cons.length;
			for (int i=0; i<max; i++){
				if (db.consState[i]==1) {
					//Check xem neu thoi gian busy cua connection qua lau thi reset
					if (System.currentTimeMillis()-db.useTime[i]>10000){
						//System.out.println("Reset connection "+i+"-timeout-command:"+db.sqlcommands[i]);
						db.useTime[i] = System.currentTimeMillis();
						try {
							//System.out.println("Reseted1");
							if (db.cons[i]!=null){
								//System.out.println("Reseted2");
								if(db.stmts[i]!=null) db.stmts[i].close();
								//System.out.println("Reseted3");
								db.cons[i].close();//Dong connection
								//System.out.println("Reseted4");
								db.cons[i]=null;
								//System.out.println("CLOSE CONNECTION "+i+" COMPLETED RESET OK 1");
							}
							db.consState[i] = 2;//Danh dau la connection nay free;
							//System.out.println("CLOSE CONNECTION "+i+" COMPLETED RESET OK 2");
						} catch (Exception e){
							//e.printStackTrace();
						}
					}
				}
				if (db.consState[i]==2) {
					//Check xem neu thoi gian qua lau khong su dung connection thi close lai
					if (System.currentTimeMillis()-db.useTime[i]>300000){
						//System.out.println("Reset idle connection "+i);
						db.useTime[i] = System.currentTimeMillis();
						try {
							if (db.cons[i]!=null){
								if(db.stmts[i]!=null) db.stmts[i].close();
								db.cons[i].close();//Dong connection
								db.cons[i]=null;
								//System.out.println("CLOSE IDLE CONNECTION "+i+" COMPLETED 1");
							}
							db.consState[i] = 0;//Thiet lap lai trang thai khoi tao
							//System.out.println("CLOSE IDLE CONNECTION "+i+" COMPLETED 2");
						} catch (Exception e){
							//e.printStackTrace();
						}
					}
				}
			}
			
		} catch (Exception e){
			//e.printStackTrace();
		}
	}
	public void start(){		
		count++;
		if (count>50) System.out.println("Check Database's Connection is started."+count);
		thread= new Thread(this);
		thread.start();
	}
}

