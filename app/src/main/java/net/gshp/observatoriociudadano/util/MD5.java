package net.gshp.observatoriociudadano.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	public static String md5(String s) {
		Log.e("MD5","MD5"+s);
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String CheckSum(String path) {
		String md5 = "";
		File file = new File(path);
		FileInputStream fis;
		char[] hexDigits = "0123456789abcdef".toCharArray();

		try {
			fis = new FileInputStream(file);
			byte[] bytes = new byte[4096];
			int read = 0;
			MessageDigest digest = MessageDigest.getInstance("MD5");

			while ((read = fis.read(bytes)) != -1) {
				digest.update(bytes, 0, read);
			}
			fis.close();

			byte[] messageDigest = digest.digest();
			StringBuilder sb = new StringBuilder(32);

			for (byte b : messageDigest) {
				sb.append(hexDigits[(b >> 4) & 0x0f]);
				sb.append(hexDigits[b & 0x0f]);
			}
			md5 = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5;
	}

	public static String fileToMD5(String filePath) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			MessageDigest digest = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while (numRead != -1) {
				numRead = inputStream.read(buffer);
				if (numRead > 0)
					digest.update(buffer, 0, numRead);
			}
			byte [] md5Bytes = digest.digest();
			return convertHashToString(md5Bytes);
		} catch (Exception e) {
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) { }
			}
		}
	}

	private static String convertHashToString(byte[] md5Bytes) {
		String returnVal = "";
		for (int i = 0; i < md5Bytes.length; i++) {
			returnVal += Integer.toString(( md5Bytes[i] & 0xff ) + 0x100, 16).substring(1);
		}
		return returnVal.toUpperCase();
	}
}
