package com.maxtree.automotive.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String encryptString(String normalStr) {
		char[] chs = normalStr.toCharArray();
		StringBuilder encryptedStr = new StringBuilder();
		for (char ch : chs) {
//			encryptedStr.append(ch+=1);
			encryptedStr.append(ch);
		}
		return encryptedStr.toString();
	}
	
	/**
	 * 
	 * @param encryptedStr
	 * @return
	 */
	public static String decryptString(String encryptedStr) {
		char[] chs = encryptedStr.toCharArray();
		StringBuilder decryptedStr = new StringBuilder();
		for (char ch : chs) {
//			decryptedStr.append(ch-=1);
			decryptedStr.append(ch);
		}
		return decryptedStr.toString();
	}

	public static void main(String[] args) {
		// 1/6caa4f23-c516-445e-941b-8c0c094b434a/1530325935206_0726_1 - Copy.jpg
		
		String old = "1/6caa4f23-c516-445e-941b-8c0c094b434a/1530325935206_0726_1 - Copy.jpg";
		
//		String encry = "MS82Y2FhNGYyMy1jNTE2LTQ0NWUtOTQxYi04YzBjMDk0YjQzNGEvMTUzMDMyNTkzNTIwNl8wNzI2XzEgLSBDb3B5LmpwZw==";//EncryptionUtils.encodeData("1/8090f21b-97ff-469c-80ae-a5a39874625bbbb/11111111112222222222333333333344444444445555555555666666.jpg");
		String encrypted = "B@CJJBFvDJ>HFHJ>EtrG>JIIG>FCwAsuwwEvDB@BFDADEFDIGFFEp佱妎?x";//encryptString(old);
		 
		System.out.println("Encrypted:");
		System.out.println(encrypted);
		
		
		System.out.println("Decrypted:");
		System.out.println(decryptString(encrypted));
		
		
//		String decrypted = EncryptionUtils.decodeData(encry);
//		
//		System.out.println(decrypted);
	}
}
