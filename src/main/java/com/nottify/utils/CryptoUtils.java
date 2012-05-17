package com.nottify.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Attila Incze
 * Cryptographic utils
 */
public class CryptoUtils {

	/**
	 * HMAC SHA256 UTF-8
	 * @param request The data to sign
	 * @param secret The secret key to use
	 * @return
	 */
	public static String signRequest(String request, String secret) {
		String signed = "";
		try {
			byte[] bytesOfSecret = secret.getBytes("UTF-8");
			byte[] bytesOfMessage = request.getBytes("UTF-8");

			SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(bytesOfSecret, "HmacSHA256");
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			sha256_HMAC.init(secret_key);

			byte[] mac_data = sha256_HMAC.doFinal(bytesOfMessage);
			for (byte element : mac_data) {
				signed += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signed;
	}

}
