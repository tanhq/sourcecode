import java.io.*;
import java.util.*;

public class testFile{
	
	
	public testFile(){		
	}
	
	public String getMobileNumber(String mobileNumber){
		
		String result = "0";
		String validRange = "0123456789";
		for(int i=0;i<mobileNumber.length();i++){
			char at = mobileNumber.charAt(i);
			if(validRange.indexOf(at)==-1) return "0";
		}
				
		if((mobileNumber.indexOf("090")==0) || (mobileNumber.indexOf("8490")==0) || (mobileNumber.indexOf("+8490")==0) || (mobileNumber.indexOf("093")==0) || (mobileNumber.indexOf("8493")==0) || (mobileNumber.indexOf("+8493")==0) || (mobileNumber.indexOf("0122")==0) || (mobileNumber.indexOf("84122")==0) || (mobileNumber.indexOf("+84122")==0) || (mobileNumber.indexOf("0126")==0) || (mobileNumber.indexOf("84126")==0) || (mobileNumber.indexOf("+84126")==0)){			
			
		}else{
			return "0";
		}
					
		//Truong hop dau so bat dau la so 0 (09, 0122, 0126)
		if((mobileNumber.startsWith("09") && mobileNumber.length()==10) || (mobileNumber.startsWith("0122") && mobileNumber.length()==11) || (mobileNumber.startsWith("0126") && mobileNumber.length()==11)){
			result = "84"+mobileNumber.substring(1,mobileNumber.length());
		}
		
		//Truong hop dau so bat dau la 9 hoac 122 hoac 126
		if((mobileNumber.startsWith("9") && mobileNumber.length()==9) || (mobileNumber.startsWith("122") && mobileNumber.length()==10)){
			result = "84"+mobileNumber;
		}
		
		//Truong hop dau so bat dau la 849 hoac 84122 hoac 84126
		if((mobileNumber.startsWith("849") && mobileNumber.length()==11) || (mobileNumber.startsWith("84122") && mobileNumber.length()==12) || (mobileNumber.startsWith("84126") && mobileNumber.length()==12)){
			result = mobileNumber;			
		}
		
		return result;
	}
		
	public static void main(String args[]){		
						
		com.mbf.mobilelist.client.mobiListClient client = new com.mbf.mobilelist.client.mobiListClient();
		
		System.out.println("group is exist: "+client.groupIsExist("NEO","01228333000"));
		
	}
	
}


