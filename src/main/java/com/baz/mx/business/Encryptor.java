/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.baz.mx.business;

import com.baz.mx.enums.CIPHER_MODE;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

/**
 *
 * @author Temporal
 */
public class Encryptor {
    
    private static final String ENCODEMODE = "UTF-8";
    private static final String CIPHER_TRANSFORM_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM_ENCRYPT_METHOD = "AES";
    private static String KEY_CIPHER_ALNOVA = "";
    private static String IV_PARAMETER_ALNOVA = "";
    private static String KEY_CIPHER_ACLARACIONES = "";
    private static String IV_PARAMETER_ACLARACIONES = "";
    
    static{
        KEY_CIPHER_ALNOVA = "B2A0A0R2N1Q8C0U7O2I0A1T6ZETCETCU";
        IV_PARAMETER_ALNOVA = "02 A6 81 F1 19 8B 87 60 E6 56 81 C1 65 88 5D 34";
        KEY_CIPHER_ACLARACIONES = "NWAIHXOMSRDIRIGDUBPJSBXJNKGVVTRZ";
        IV_PARAMETER_ACLARACIONES = "D5 34 30 4B 29 A6 85 1B 4E 30 F0 A8 12 DE C6 07";
    }

    public Encryptor() {
    }
    
    private static byte[] xeh(String in) {
        in = StringUtils.leftPad(in.replaceAll(" ", ""), 32, "0");
        int len = in.length() / 2;
        byte[] out = new byte[len];
        for (int i = 0; i < len; i++) {
            out[i] = (byte) Integer.parseInt(in.substring(i * 2, i * 2 + 2), 16);
        }
        return out;
    }
    
    public static String encrypt(String value, CIPHER_MODE mode) throws Exception{
        String keyCipher = getKeyFromMode(mode);
        String ivCipher = getIVFromMode(mode);
        try {
            keyCipher = (keyCipher.length() > 32 ? keyCipher.substring(0, 32) : keyCipher);
            IvParameterSpec iv = new IvParameterSpec(xeh(ivCipher), 0, 16);
            SecretKeySpec skeySpec = new SecretKeySpec(StringUtils.leftPad(keyCipher, 32, "0").getBytes(ENCODEMODE), ALGORITHM_ENCRYPT_METHOD);
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORM_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted) + " tipo:" + mode.toString());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static String decrypt(String encrypted, CIPHER_MODE mode) throws Exception{
        String keyCipher = getKeyFromMode(mode);
        String ivCipher = getIVFromMode(mode);
        try {
            IvParameterSpec iv = new IvParameterSpec(xeh(ivCipher), 0, 16);
            SecretKeySpec skeySpec = new SecretKeySpec(keyCipher.getBytes(ENCODEMODE), ALGORITHM_ENCRYPT_METHOD);
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORM_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public static String encryptString(String text, CIPHER_MODE mode) throws Exception{
        String crypt = encrypt(text, mode);
        crypt = crypt.replace("=", "");
        crypt = crypt.replace("+", "-");
        crypt = crypt.replace("/", "_");
        return crypt;
    }

    public static String decryptString(String text, CIPHER_MODE mode) throws Exception{
        String crypt = text.replace("-", "+");
        crypt = crypt.replace("_", "/");
        crypt = crypt.replace("==", "");
        crypt = decrypt(crypt, mode);
        return crypt;
    }
    
    private static synchronized String getKeyFromMode(CIPHER_MODE mode){
        if(mode.equals(CIPHER_MODE.ALNOVA)){
            return KEY_CIPHER_ALNOVA;
        }
        else{
            return KEY_CIPHER_ACLARACIONES;
        }
    }
    
    private static synchronized String getIVFromMode(CIPHER_MODE mode){
        if(mode.equals(CIPHER_MODE.ALNOVA)){
            return IV_PARAMETER_ALNOVA;
        }
        else{
            return IV_PARAMETER_ACLARACIONES;
        }
    }
    
}
