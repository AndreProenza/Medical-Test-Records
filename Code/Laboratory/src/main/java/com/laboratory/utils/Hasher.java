package com.laboratory.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hasher {

	/**
	 * Creates the hash of a string given as input. It uses SHA-256.
	 *
	 * @param string the string to hash
	 * @return the hashed value of the string or an empty string in case of error
	 * 
	 *         based on
	 *         https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
	 */
	public static String hash(String string) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(string.getBytes("UTF-8"));
			return bytesToHex(hash);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Hasher.hash: algarithm not recognized");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("Hasher.hash: Encoding format not recognized");
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * translates a byte[] into a hexadecimal string.
	 * 
	 * @param hash the array of bytes to translate
	 * @return a string of the hex value of the byte array
	 * 
	 */
	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
