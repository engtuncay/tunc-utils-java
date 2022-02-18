package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class FiCrypto {

	public static void main(String[] args) {

		String input = "www.baeldung.com";
		String password = "baeldung";
		String salt = "12345678";
		String algorithm = "AES/CBC/PKCS5Padding";

		IvParameterSpec ivParameterSpec = generateIv();
		SecretKey key = null;
		try {
			key = FiCrypto.getKeyFromPassword(password, salt);
			String cipherText = FiCrypto.encrypt(algorithm, input, key, ivParameterSpec);
			String plainText = FiCrypto.decrypt(algorithm, cipherText, key, ivParameterSpec);
			System.out.println("Input:" + input);
			System.out.println("Cipher:" + cipherText);
			System.out.println("Decrypt:" + plainText);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(n);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}

	public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

		return secret;
	}

	public static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	// Source : https://www.baeldung.com/java-aes-encryption-decryption
	public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(plainText);
	}


	public static String getMd5(String stringToHash) {
		//public static void main(String args[]) throws NoSuchAlgorithmException
		try {
			//String stringToHash = "MyJavaCode";
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(stringToHash.getBytes());
			byte[] digiest = messageDigest.digest();
			String hashedOutput = DatatypeConverter.printHexBinary(digiest);
			//System.out.println(hashedOutput);
			return hashedOutput;
		} catch (NoSuchAlgorithmException ex) {
			Loghelper.debugException(FiCrypto.class,ex);
		}
		return null;
	}

}
