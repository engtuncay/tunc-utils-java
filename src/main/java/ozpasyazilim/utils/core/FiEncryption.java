package ozpasyazilim.utils.core;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
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
			messageDigest.update(txValue.getBytes(Charset.forName("UTF8")));
			final byte[] resultByte = messageDigest.digest();
			final String result = new String(Hex.encodeHex(resultByte)).toUpperCase();
			//System.out.println("MD5:"+result.toUpperCase());
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static synchronized String decrypt(String str) {
		// byte[] dec = decoder.decodeBuffer(str);
		// byte[] utf8 = decrypter.doFinal(dec);
		// return new String(utf8, "UTF-8");
		// return UtilSimpleEncryption.decrypt(str);
		//System.out.println("str dec:"+str);
//		byte[] dashed = str.getBytes(StandardCharsets.UTF_8);
////		byte[] decode = Base64.getUrlDecoder().decode(dashed);
////		return new String(decode);

		try {
			byte[] decodedString = Base64.getMimeDecoder().decode(str.getBytes("UTF-8"));
			return new String(decodedString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

//		byte[] decodedBytes = Base64.getDecoder().decode(str);
//		return new String(decodedBytes);

		//return new String(Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8)));

//		byte[] decodedImg = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
//
//		return new String(decodedImg,"UTF-8");

	}

	public static synchronized String encrypt(String str) throws Exception {
		// byte[] utf8 = str.getBytes("UTF-8");
		// byte[] enc = encrypter.doFinal(utf8);
		// return encoder.encode(enc);
		// return UtilSimpleEncryption.encrypt(str);
		String originalInput = str;
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		return encodedString;
	}
}
