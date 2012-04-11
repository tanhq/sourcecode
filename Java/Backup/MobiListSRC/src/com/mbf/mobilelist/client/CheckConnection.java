package com.mbf.mobilelist.client;

import java.util.*;
import java.text.SimpleDateFormat;

public class CheckConnection implements Runnable,java.io.Serializable{
	public Thread thread;
	public static Pool db = null;
	private static long lastCheckTime = 0;
	private static int count=0;
	
	public CheckConnection(){
	}

	public void run(){
		try {
			try {
				if (System.currentTimeMillis() - lastCheckTime>30000){// 30 giay
					lastCheckTime = System.currentTimeMillis();
					System.out.println("DB Connection checking is running...");
					processData();
				}
			} catch (Exception ez){
				ez.printStackTrace();
			}
		} catch (Exception e){
		}
		count--;
	}
	////////////////////////////////////////////////////////////////////////////
	private void processData(){
		try {
			if (db==null) db = new Pool();
			int max=db.cons.length;
			for (int i=0; i<max; i++){
				if (db.consState[i]==1) {
					//Check xem neu thoi gian busy cua connection qua lau thi reset
					if (System.currentTimeMillis()-db.useTime[i]>60000){
						db.consState[i] = 2;//Trang thai ilde(free)
						System.out.println("Reset connection "+i+"-timeout-command:"+db.commands[i]);
						db.useTime[i] = System.currentTimeMillis();
					}
				}
				if (db.consState[i]==2) {
					//Check xem neu thoi gian qua lau khong su dung connection thi close lai
					if (System.currentTimeMillis()-db.useTime[i]>300000){
						db.consState[i] = 0;//Trang thai dang dong
						System.out.println("Reset idle connection "+i);
						db.useTime[i] = System.currentTimeMillis();
					}
				}
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void start(){		
		count++;
		if (count>50) System.out.println("Check RTC's Connection is started."+count);
		thread= new Thread(this);
		thread.start();
	}
}
