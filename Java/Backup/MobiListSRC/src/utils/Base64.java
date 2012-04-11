package com.utils;

import java.io.UnsupportedEncodingException;

public class Base64 {

    private final static String LANGUAGE = "ISO8859_1";
    private int fillchar1 = '=';
    private int fillchar2 = '=';
    public String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	////////////////////////////////////////////////////////////////////////////
    public String encode(String s) {
    	try {
	    	byte[] data = stringToByteArray(s);
	        int c;
	        int len = data.length;
	        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
	        for (int i = 0; i < len; ++i) {
	            c = (data[i] >> 2) & 0x3f;
	            ret.append(cvt.charAt(c));
	            c = (data[i] << 4) & 0x3f;
	            if (++i < len)
	                c |= (data[i] >> 4) & 0x0f;
	
	            ret.append(cvt.charAt(c));
	            if (i < len) {
	                c = (data[i] << 2) & 0x3f;
	                if (++i < len)
	                    c |= (data[i] >> 6) & 0x03;
	
	                ret.append(cvt.charAt(c));
	            } else {
	                ++i;
	                ret.append((char) fillchar1);
	            }
	
	            if (i < len) {
	                c = data[i] & 0x3f;
	                ret.append(cvt.charAt(c));
	            } else {
	                ret.append((char) fillchar2);
	            }
	        }
	
	        return ret.toString();
		} catch (Exception e){
			return null;
		}
    }
	////////////////////////////////////////////////////////////////////////////
    public String decode(String s){
    	try {
	    	byte[] data = stringToByteArray(s);
	        int c;
	        int c1;
	        int len = data.length;
	        StringBuffer ret = new StringBuffer((len * 3) / 4);
	        for (int i = 0; i < len; ++i) {
	            c = cvt.indexOf(data[i]);
	            ++i;
	            c1 = cvt.indexOf(data[i]);
	            c = ((c << 2) | ((c1 >> 4) & 0x3));
	            ret.append((char) c);
	            if (++i < len){
	                c = data[i];
	                if (fillchar1 == c) break;
	
	                c = cvt.indexOf((char) c);
	                c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
	                ret.append((char) c1);
	            }
	
	            if (++i < len){
	                c1 = data[i];
	                if (fillchar2 == c1)
	                    break;
	
	                c1 = cvt.indexOf((char) c1);
	                c = ((c << 6) & 0xc0) | c1;
	                ret.append((char) c);
	            }
	        }	
	        return ret.toString();
	     } catch (Exception e){
	     	return null;
	     }
    }
	////////////////////////////////////////////////////////////////////////////
    public String encode(byte[] data) {
    	try {
	        int c;
	        int len = data.length;
	        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
	        for (int i = 0; i < len; ++i) {
	            c = (data[i] >> 2) & 0x3f;
	            ret.append(cvt.charAt(c));
	            c = (data[i] << 4) & 0x3f;
	            if (++i < len)
	                c |= (data[i] >> 4) & 0x0f;
	
	            ret.append(cvt.charAt(c));
	            if (i < len) {
	                c = (data[i] << 2) & 0x3f;
	                if (++i < len)
	                    c |= (data[i] >> 6) & 0x03;
	
	                ret.append(cvt.charAt(c));
	            } else {
	                ++i;
	                ret.append((char) fillchar1);
	            }
	
	            if (i < len) {
	                c = data[i] & 0x3f;
	                ret.append(cvt.charAt(c));
	            } else {
	                ret.append((char) fillchar2);
	            }
	        }
	
	        return ret.toString();
		} catch (Exception e){
			return null;
		}
    }
	////////////////////////////////////////////////////////////////////////////
    public String decode(byte[] data){
    	try {
	        int c;
	        int c1;
	        int len = data.length;
	        StringBuffer ret = new StringBuffer((len * 3) / 4);
	        for (int i = 0; i < len; ++i) {
	            c = cvt.indexOf(data[i]);
	            ++i;
	            c1 = cvt.indexOf(data[i]);
	            c = ((c << 2) | ((c1 >> 4) & 0x3));
	            ret.append((char) c);
	            if (++i < len){
	                c = data[i];
	                if (fillchar1 == c) break;
	
	                c = cvt.indexOf((char) c);
	                c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
	                ret.append((char) c1);
	            }
	
	            if (++i < len){
	                c1 = data[i];
	                if (fillchar2 == c1)
	                    break;
	
	                c1 = cvt.indexOf((char) c1);
	                c = ((c << 6) & 0xc0) | c1;
	                ret.append((char) c);
	            }
	        }	
	        return ret.toString();
	     } catch (Exception e){
	     	return null;
	     }
    }

    ////////////////////////////////////////////////////////////////////////////
  	public String byteArrayToString(byte[] input) throws UnsupportedEncodingException{
    	if (input != null) return new String(input, LANGUAGE);
    	else return null;
  	}
  
  	public byte[] stringToByteArray(String input) throws UnsupportedEncodingException{
    	if (input != null) return input.getBytes(LANGUAGE);
    	else return null;
  	}
    ////////////////////////////////////////////////////////////////////////////
    public String createRandomString(){
    	try{
    		String result="";
    		byte[] b = this.stringToByteArray(cvt);
    		java.util.Random rdm = new java.util.Random();
    		for (int i=64; i>0; i--){
    			int tmp = (java.lang.Math.abs(rdm.nextInt()))%i;
    			//System.out.println(tmp);
    			char c = (char) b[tmp];
    			b[tmp] = b[i-1];
    			result += c;
    		}
    		return result;
    	}catch(Exception e){
			System.out.println(e);
    		return null;
    	}
    }
    ////////////////////////////////////////////////////////////////////////////
    public static void main(String argv[]){
		Base64 vd = new Base64();
		System.out.println(vd.encode("Dang Ha Trung"));
		vd.cvt = vd.createRandomString();
		System.out.println(vd.cvt);
		
		System.out.println(vd.encode("Dang Ha Trung"));
		System.out.println(vd.decode(vd.encode("Dang Ha Trung")));
	}
}
