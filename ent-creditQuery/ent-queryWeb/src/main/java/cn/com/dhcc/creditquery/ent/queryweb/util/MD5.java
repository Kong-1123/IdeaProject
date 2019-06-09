package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5 {
	
	private final static Logger log=LoggerFactory.getLogger(MD5.class);

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getMd5String(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static String getMD5(FileInputStream fileInputStream) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				messageDigest.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(messageDigest.digest()));
		} catch (NoSuchAlgorithmException e) {
			log.error("MD5 error={}",e);
			return null;
		} catch (FileNotFoundException e) {
			log.error("MD5 error={}",e);
			return null;
		} catch (IOException e) {
			log.error("MD5 error={}",e);
			return null;
		} 
	}
	
	public static String getMD5(File file) {
		MessageDigest messageDigest = null;
		FileInputStream fileInputStream=null;
		try {
			fileInputStream=new FileInputStream(file);
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				messageDigest.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(messageDigest.digest()));
		} catch (NoSuchAlgorithmException e) {
			log.error("MD5 error={}",e);
			return null;
		} catch (FileNotFoundException e) {
			log.error("MD5 error={}",e);
			return null;
		} catch (IOException e) {
			log.error("MD5 error={}",e);
			return null;
		} finally{
			try {
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
