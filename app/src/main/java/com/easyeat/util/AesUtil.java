package com.easyeat.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {

    private static final String A1 = "AES";
    private static final String A2 = "AES/CBC/PKCS5Padding";
    private static final String EV = "A2";
    private static byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 加密(AES+BASE64)
     *
     * @param content  需要加密的内容
     * @param password 秘钥
     * @return 加密后String字符串
     */
    public static String encrypt(String content, String password) {
        return encrypt(content, password, EV);
    }

    public static String encrypt(String content, String password, String version) {
        try {
            byte[] enCodeFormat = getKeyByteFromPassword(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(version.equalsIgnoreCase(EV) ? A2 : A1);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            if (version.equalsIgnoreCase(EV)) {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            }
            byte[] result = cipher.doFinal(byteContent);// ASE加密
            String base64Result = Base64.encodeToString(result, Base64.DEFAULT);// BASE64加密
            return base64Result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密(AES+BASE64)
     *
     * @param content  需要解密的内容
     * @param password 秘钥
     * @return 解密后String字符串
     */
    public static String decrypt(String content, String password) {
        if (content.contains("\\n")) {
            content = content.replaceAll("\\\\n", " ");
        }
        byte[] b = Base64.decode(content, Base64.DEFAULT);
        byte[] result = decrypt(b, password, EV);
        return result != null ? new String(result) : null;
    }

    public static String decrypt(String content, String password, String version) {
        if (content.contains("\\n")) {
            content = content.replaceAll("\\\\n", " ");
        }
        byte[] b = Base64.decode(content, Base64.DEFAULT);
        byte[] result = decrypt(b, password, version);
        return result != null ? new String(result) : null;
    }

    private static byte[] decrypt(byte[] content, String password, String version) {
        try {
            byte[] enCodeFormat = getKeyByteFromPassword(password);
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(version.equalsIgnoreCase(EV) ? A2 : A1);// 创建密码器
            if (version.equalsIgnoreCase(EV)) {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            }
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getKeyByteFromPassword(String password) {
        String hexString = Md5Util.encrypt(password);
        return hexStringToByteArray(hexString);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
