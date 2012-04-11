package com.mbf.mobilelist.objects;

public class objMobile implements java.io.Serializable{
	
	String groupId;
	String mobileNumber;
	
	public objMobile(){
	}
	
	public objMobile(String _groupId, String _mobileNumber){
		groupId 		= _groupId;
		mobileNumber	= _mobileNumber;
	}
	
	//setter methods
	public void setGroupId(String _groupId){
		groupId = _groupId;
	}
	
	public void setMobileNumber(String _mobileNumber){
		mobileNumber = _mobileNumber;
	}
	
	//getter methods
	public String getGroupId(){
		if(groupId==null) return "";
		else return groupId;
	}
	
	public String getMobileNumber(){
		if(mobileNumber==null) return "";
		else return mobileNumber;
	}
	
}
