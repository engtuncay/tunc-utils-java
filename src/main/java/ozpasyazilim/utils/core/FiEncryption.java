package ozpasyazilim.utils.core;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class FiEncryption {

	public static String strToMd5(String txValue){

		final MessageDigest messageDigest;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(txValue.getBytes(StandardCharsets.UTF_8));
			final byte[] resultByte = messageDigest.digest();
			final String result = new String(Hex.encodeHex(resultByte)).toUpperCase();
			//System.out.println("MD5:"+result.toUpperCase());
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static synchronized String decryptBase64(String str) {
        byte[] decodedString = Base64.getMimeDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        return new String(decodedString);
	}

	public static synchronized String encryptBase64(String txValue) {
		String encrytedValue = Base64.getEncoder().encodeToString(txValue.getBytes());
		return encrytedValue;
	}
}
