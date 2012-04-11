package com.mbf.mobilelist.objects;


public class resultInfo implements java.io.Serializable{
	
	int 	resultCode  = 0;
	String 	note		= "";
	Object  resultObj   = null;
	
	//setter method
	public void setResultCode(int _resultCode){
		resultCode = _resultCode;
	}
	
	public void setResultNote(String _note){
		note = _note;
	}
	
	public void setResultObject(Object _resultObj){
		resultObj = _resultObj;
	}
	
	
	//getter method
	public int getResultCode(){
		return resultCode;
	}
	
	public String getResultNote(){
		return note;
	}
	
	public Object getResultObject(){
		return resultObj;
	}
}
