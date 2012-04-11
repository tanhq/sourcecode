package com.utils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;


public class AES {
	
	private static final String ALGORITHM = "AES";
	private Base64 base64 = new Base64();
    private SecretKey key = null;
	////////////////////////////////////////////////////////////////////////////
    public void generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
		key = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
		
		//cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		//byte[] outText = cipher.doFinal(fromHexString("input"));
		//System.out.println(new String(outText).trim()); 
				
    }
	////////////////////////////////////////////////////////////////////////////
    public String getCipherKey() {
    	try {
    		
			//SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
			//AESKeySpec keyspec = (DESedeKeySpec)keyfactory.getKeySpec(key, DESedeKeySpec.class);
			return base64.encode(key.getEncoded());
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
    }
	////////////////////////////////////////////////////////////////////////////
    public String encrypt(String s){
    	try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] enc = cipher.doFinal(base64.stringToByteArray(s));
			return base64.encode(enc);
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public String decrypt(String s) {
    	try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dec = base64.stringToByteArray(base64.decode(s));
            byte[] result = cipher.doFinal(dec);

            return new String(result);
 		} catch (Exception e){
			System.out.println(e);
			return null;
		}
    }
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		try {
		    AES des = new AES();
			
		    // Generate a key
			des.generateKey();
			System.out.println("Key: "+des.getCipherKey());
			//System.out.println("Now key is: "+ des.getCipherKey());
			
				String s = "1";
				System.out.println("Clear text: "+s);
				
				s = des.encrypt(s);
				System.out.println("Cipher text: "+s);
					
				s = des.decrypt("9iVdL3QpiTBz2cSX/Cxqcw==");
				System.out.println("After decrypt: "+s);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

