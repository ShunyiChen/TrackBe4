package com.maxtree.automotive.dashboard;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptionUtils {
	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

	private static final String UTF_8 = "UTF-8";

	/**
	 * 对给定的字符串进行base64解码操作
	 */
	public static String decodeData(String inputData) {
		try {
			if (null == inputData) {
				return null;
			}
			return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
		} catch (UnsupportedEncodingException e) {
			logger.error(inputData, e);
		}

		return null;
	}

	/**
	 * 对给定的字符串进行base64加密操作
	 */
	public static String encodeData(String inputData) {
		try {
			if (null == inputData) {
				return null;
			}
			return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
		} catch (UnsupportedEncodingException e) {
			logger.error(inputData, e);
		}

		return null;
	}

	public static void main(String[] args) {
		
		String encry = EncryptionUtils.encodeData("1/8090f21b-97ff-469c-80ae-a5a39874625bbbb/11111111112222222222333333333344444444445555555555666666.jpg");
		
		System.out.println(encry.length());
		
		String decrypted = EncryptionUtils.decodeData(encry);
		
		System.out.println(decrypted);
	}
}
