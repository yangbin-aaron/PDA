
package com.toughegg.teorderpo.utils;

import android.util.Base64;

import java.security.MessageDigest;

public class TESHA2 {
	public static final String ALGORITHM = "SHA-256";

	public static String SHA256Encrypt(String orignal, String salt, String key) {
		MessageDigest md = null;
		if (salt.length() >= 36) {
			salt = salt.substring(0, key.length());
		} else {
			int count = salt.length();
			for (int i = 0; i < 36 - count; i++) {
				salt += "x";
			}
		}
		try {
			md = MessageDigest.getInstance(ALGORITHM);
			if (null != md) {
				byte[] origBytes = (orignal + salt + key).getBytes("US-ASCII");
				md.update(origBytes);
				byte[] digestRes = md.digest();
				return Base64.encodeToString(digestRes, Base64.DEFAULT).trim();
			}
		} catch (Exception e) {
		}
		return null;
	}
}
