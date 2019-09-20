package com.qh.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
public class Qhcrypt {
    public Qhcrypt() {
    }

    private static byte[] encrypt(byte[] byteContent, String AppKey) {
        byte[] crypted = null;

        try {
            SecretKeySpec base64Encoder = new SecretKeySpec(AppKey.getBytes(), "AES");
            Cipher str = Cipher.getInstance("AES/ECB/PKCS5Padding");
            str.init(1, base64Encoder);
            crypted = str.doFinal(byteContent);
        } catch (Exception var5) {
            System.out.println(var5.toString());
        }

        BASE64Encoder base64Encoder1 = new BASE64Encoder();
        String str1 = base64Encoder1.encode(crypted);
        return str1.getBytes();
    }

    private static String decrypt(byte[] b, String AppKey) throws UnsupportedEncodingException {
        byte[] output = null;

        try {
            SecretKeySpec e = new SecretKeySpec(AppKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, e);
            BASE64Decoder base64Encoder = new BASE64Decoder();
            output = cipher.doFinal(base64Encoder.decodeBuffer(new String(b)));
        } catch (Exception var6) {
            System.out.println(var6.toString());
        }

        return new String(output, "utf-8");
    }

    /**
     * 加密
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encrypt(String content) throws UnsupportedEncodingException {
        byte[] b_ret = encrypt(content.getBytes(), "2019201920192019");
        return new String(b_ret, "utf-8");
    }

    /**
     * 解密
     * @param content
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decrypt(String content) throws UnsupportedEncodingException {
        String s_ret = decrypt(content.getBytes(), "2019201920192019");
        return s_ret;
    }
}
