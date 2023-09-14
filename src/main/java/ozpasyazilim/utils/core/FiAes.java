package ozpasyazilim.utils.core;

import ozpasyazilim.utils.log.Loghelper;
import ozpasyazilim.utils.returntypes.Fdr;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class FiAes {

    public static void main(String[] args) {
        Loghelper.installLogger(true);
        String input = "www.baeldung.com";
        // Bu değerler değişirse, şifreleme değişmiş olur.
        String password = "baeldung";
        String salt = "entegre2023"; //generateRandomString(8);
        String hexTemp = "556315d7510d29e7cdeaeb8654f892c0";

        Fdr<SecretKey> fdrKey = getKeyFromPassword(password, salt);
        //String salt = generateRandomString(8);
        Fdr<String> cipherText = encryptSimple(input, hexTemp, fdrKey.getValue());
        System.out.println(cipherText.getValue());

        String cipherDemo = "V2x2DYYz4+VUmQFfTFbnNsx7MyvgugYjekNi7M6vQRY=";
        Fdr<String> plainText = decryptSimple(cipherDemo, hexTemp, fdrKey.getValue());
        System.out.println(plainText.getValue());

    }

    public static Fdr<String> encryptSimple(String input, String ivHexValue, SecretKey secretKey) {

        Fdr<String> fdrMain = new Fdr<>();
        String algorithm = "AES/CBC/PKCS5Padding";

        //IvParameterSpec ivParameterSpec = generateIv();
        IvParameterSpec ivParameterSpec1 = new IvParameterSpec(FiByte.hexStrToByteV1(ivHexValue));

        String cipherText = null;
        try {
            cipherText = encryptWex(algorithm, input, secretKey, ivParameterSpec1);
            fdrMain.setBoResult(true);
            fdrMain.setValue(cipherText);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            Loghelper.get(getClassi()).error(FiException.exTosMain(e));
            fdrMain.setBoResult(false, e);
        }

        return fdrMain;
    }

    public static Fdr<String> decryptSimple(String cipherText, String ivHexValue, SecretKey secretKey) {

        Fdr<String> fdrMain = new Fdr<>();
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec1 = new IvParameterSpec(FiByte.hexStrToByteV1(ivHexValue));

        String plainText = null;
        try {
            plainText = decryptWex(algorithm, cipherText, secretKey, ivParameterSpec1);
            fdrMain.setBoResult(true);
            fdrMain.setValue(plainText);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            Loghelper.get(getClassi()).error(FiException.exTosMain(e));
            fdrMain.setBoResult(false, e);
        }

        return fdrMain;
    }

    private static Class<FiAes> getClassi() {
        return FiAes.class;
    }

    /**
     * Wex with exception
     *
     * @param algorithm
     * @param input
     * @param key
     * @param iv
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encryptWex(String algorithm, String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decryptWex(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static SecretKey getKeyFromPasswordWex(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }

    public static Fdr<SecretKey> getKeyFromPassword(String password, String salt) {

        Fdr<SecretKey> secretKeyFdr = new Fdr<>();

        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = null;
        try {
            secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        secretKeyFdr.setValue(secret);
        return secretKeyFdr;
    }

    /**
     * an initialization vector (IV)
     *
     * @return
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

}