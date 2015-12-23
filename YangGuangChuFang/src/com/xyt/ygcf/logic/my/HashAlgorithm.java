package com.xyt.ygcf.logic.my;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashAlgorithm {

	public static String ALGORITHM_MD5 = "md5";
	public static String ALGORITHM_SHA_1 = "sha-1";

	public static String getHashText(String inputText) throws NoSuchAlgorithmException {
		return getHashText(inputText, ALGORITHM_MD5);
	}

	public static String getHashText(String inputText, String algorithm) throws NoSuchAlgorithmException {
		if (!ALGORITHM_MD5.equals(algorithm) && !ALGORITHM_SHA_1.equals(algorithm)) {
			throw new NoSuchAlgorithmException("不支持" + algorithm + "算法");
		}
		MessageDigest digest = MessageDigest.getInstance(algorithm);
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			digest.update(inputText.getBytes("UTF-8"));
			byte[] data = digest.digest();
			int length = data.length;
			char str[] = new char[length << 1];
			int k = 0;
			for (int i = 0; i < length; i++) {
				byte byte0 = data[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xF];
				str[k++] = hexDigits[byte0 & 0xF];
			}
			return new String(str);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

}
