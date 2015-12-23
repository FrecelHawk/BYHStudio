package com.xyt.ygcf.logic.my;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;

import com.xyt.ygcf.entity.my.RSABeen;

public class RSA {

	private Cipher encryptCipher = null;
	private PublicKey publicKey = null;

	private static RSA rsa = new RSA();

	private RSA(){}
	
	public static RSA getInstance() {
		if (rsa == null) {
			rsa = new RSA();
		}
		return rsa;
	}

	public String encryptText(String text) throws Exception {
		if (publicKey == null) {
			RSABeen rsaBeen = Cookies.getInstance().getRsaBeen();
			publicKey = rebuildRSAPublicKey(rsaBeen.modulus, rsaBeen.exponent);
			rsaBeen = null;
		}
		this.initializeEncrypt();
		final String encryText = this.bytesToHexString(this.encrypt(text.getBytes()));
		return encryText;
	}

	/**
	 * 描述:根据所提供的公钥系数和公钥指数,重新构建RSA公钥,关于指数信息，查看RSAPublicKeySpec
	 * 
	 * @param modulus
	 *            公钥系数
	 * @param publicExponent
	 *            公钥指数
	 * @return RSAPublicKey
	 * */
	private RSAPublicKey rebuildRSAPublicKey(String modulus, String publicExponent) {
		RSAPublicKey rpk = null;
		KeyFactory kf = null;
		try {
			kf = KeyFactory.getInstance("RSA");
			rpk = (RSAPublicKey) kf.generatePublic(new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(
					publicExponent, 16)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return rpk;
	}

	public PublicKey rebuildPublicKey(String keyString) throws Exception {
		byte[] keys = hexStringToBytes(keyString);
		KeySpec keySpec = new X509EncodedKeySpec(keys);
		KeyFactory factory = KeyFactory.getInstance("RSA");
		return factory.generatePublic(keySpec);
	}

	@SuppressLint("TrulyRandom")
	private void initializeEncrypt() throws Exception {
		encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
	}

	private byte[] encrypt(byte[] b) throws Exception {
		return encryptCipher.doFinal(b);
	}

	public void encryptFile(String sourceFileName, String diminationFileName) throws Exception {
		FileInputStream infile = new FileInputStream(sourceFileName);
		FileOutputStream outfile = new FileOutputStream(diminationFileName);
		byte[] b = new byte[117];
		byte[] newbyte;
		int a = infile.read(b);
		while (a != -1) {
			if (a == b.length) {
				newbyte = b;
			} else {
				newbyte = new byte[a];
				for (int i = 0; i < a; i++) {
					newbyte[i] = b[i];
				}
			}
			byte[] ee = encrypt(newbyte);
			outfile.write(ee);
			a = infile.read(b);
		}
		infile.close();
		outfile.close();
	}

	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		final int length = src.length;
		for (int i = 0; i < length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	@SuppressLint("DefaultLocale")
	public byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
}