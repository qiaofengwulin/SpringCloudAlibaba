package com.qh.utils;

import java.util.Base64;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/16
 * @Version 1.0
 */
public class Base64Util {


    /**
     * Base64解码
     * @param encodedText
     * @return
     */
    public static byte[] decode(String encodedText){
        final Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encodedText);
    }

    /**
     * Base64编码
     * @param data
     * @return
     */
    public static String encode(byte[] data){
        final Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            final Base64.Decoder decoder = Base64.getDecoder();
            final Base64.Encoder encoder = Base64.getEncoder();
            final String text = "字串文字";
            final byte[] textByte = text.getBytes("UTF-8");
            //编码
            final String encodedText = encoder.encodeToString(textByte);
            System.out.println(encodedText);
            //解码
            System.out.println(new String(decoder.decode(encodedText), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final Base64.Decoder decoder = Base64.getDecoder();
//        final Base64.Encoder encoder = Base64.getEncoder();
//        final String text = "字串文字";
//        final byte[] textByte = text.getBytes("UTF-8");
//        //编码
//        final String encodedText = encoder.encodeToString(textByte);
//        System.out.println(encodedText);
//        //解码
//        System.out.println(new String(decoder.decode(encodedText), "UTF-8"));

    }

}
