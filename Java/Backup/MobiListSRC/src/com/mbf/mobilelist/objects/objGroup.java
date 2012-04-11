package com.mbf.mobilelist.objects;


public class objGroup implements java.io.Serializable{
	
	public String groupName;
	public String groupDesc;
	public String groupId;
	public String groupMobile;
	
	public objGroup(){
	}
	
	public objGroup(String _groupName, String _groupDesc, String _groupId, String _groupMobile){
		groupName 	= _groupName;
		groupDesc 	= _groupDesc;
		groupId	  	= _groupId;
		groupMobile = _groupMobile;
	}
	
	//setter methods
	public void setGroupName(String _groupName){
		groupName = _groupName;
	}
	
	public void setGroupDesc(String _groupDesc){
		groupDesc = _groupDesc;
	}
	
	public void setGroupId(String _groupId){
		groupId	= _groupId;
	}
	
	public void setGroupMobile(String _groupMobile){
		groupMobile = _groupMobile;
	}
	
	//getter methods
	public String getGroupName(){
		if(groupName==null) return "";
		else return groupName;
	}
	
	public String getGroupDesc(){
		if(groupDesc==null) return "";
		else return groupDesc;
	}
	
	public String getGroupId(){
		if(groupId==null) return "";
		else return groupId;
	}
	
	public String getGroupMobile(){
		if(groupMobile==null) return "";
		else return groupMobile;
	}
}
