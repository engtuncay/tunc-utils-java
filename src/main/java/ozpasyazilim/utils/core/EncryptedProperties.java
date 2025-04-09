package ozpasyazilim.utils.core;

import java.util.Base64;
import java.util.Properties;

import ozpasyazilim.utils.log.Loghelper;

public class EncryptedProperties extends Properties {
	// private static Base64.Decoder decoder = Base64.getDecoder();
	// private static Base64.Encoder encoder = Base64.getEncoder();
	// private Cipher encrypter, decrypter;
	// private static byte[] salt = {
	// (byte) 0xde,
	// (byte) 0x33,
	// (byte) 0x10,
	// (byte) 0x12,
	// (byte) 0xde,
	// (byte) 0x33,
	// (byte) 0x10,
	// (byte) 0x12, };
	String password = null;

	public EncryptedProperties() throws Exception {
		// parametre silindi String password
		// UtilSimpleEncryption.setPassword(password);
		// PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);
		// SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES"); //
		// SecretKey k = kf.generateSecret(new
		// javax.crypto.spec.PBEKeySpec(password.toCharArray()));
		// encrypter = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
		// decrypter = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
		// encrypter.init(Cipher.ENCRYPT_MODE, k, ps);
		// decrypter.init(Cipher.DECRYPT_MODE, k, ps);
	}

	public EncryptedProperties(String password) {
		// password kullanılmıyor daha sonra implemente ediliebilir
		this.password = password;
	}

	@Override
	public String getProperty(String key) {
		try {
			return decrypt(super.getProperty(key));
		} catch (Exception e) {
			Loghelper.get(EncryptedProperties.class).error(FiException.exToErrorLog(e));
			//throw new RuntimeException("Couldn't decrypt property");
		}
		return null;
	}

	@Override
	public synchronized Object setProperty(String key, String value) {
		try {
			return super.setProperty(key, encrypt(value));
		} catch (Exception e) {
			Loghelper.get(EncryptedProperties.class).error(FiException.exToErrorLog(e));
//			Loghelper.logexceptionOnlyMail(FiException.exceptionStackTraceStringFull(e));
//			throw new RuntimeException("Couldn't encrypt property");
		}
		return null;
	}

	private synchronized String decrypt(String str) throws Exception {
		// byte[] dec = decoder.decodeBuffer(str);
		// byte[] utf8 = decrypter.doFinal(dec);
		// return new String(utf8, "UTF-8");
		// return UtilSimpleEncryption.decrypt(str);
		byte[] decodedBytes = Base64.getDecoder().decode(str);
		return new String(decodedBytes);

	}

	private synchronized String encrypt(String str) throws Exception {
		// byte[] utf8 = str.getBytes("UTF-8");
		// byte[] enc = encrypter.doFinal(utf8);
		// return encoder.encode(enc);
		// return UtilSimpleEncryption.encrypt(str);
		String originalInput = str;
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		return encodedString;
	}
}