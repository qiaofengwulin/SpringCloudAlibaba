package com.qh.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/16
 * @Version 1.0
 */

public class AESUtil {
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    public static String getALGORITHM() {
        return ALGORITHM;
    }

    /**
     * AES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptData(String data, SecretKeySpec key) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64Util.encode(cipher.doFinal(data.getBytes()));
    }

    /**
     * AES解密
     *
     * @param base64Data
     * @return
     * @throws Exception
     */
    public static String decryptData(String base64Data, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64Util.decode(base64Data)));
    }


    public static void main(String[] args) throws Exception {
        SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode("2IBtBXdrqC3kCBs4gaceL7nl2nnFadQv").toLowerCase().getBytes(), AESUtil.getALGORITHM());
        String data = "<root>" +
                "<out_refund_no><![CDATA[2531340110812300]]></out_refund_no>" +
                "<out_trade_no><![CDATA[2531340110812100]]></out_trade_no>" +
                "<refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>" +
                "<refund_fee><![CDATA[1]]></refund_fee>" +
                "<refund_id><![CDATA[50000505542018011003064518841]]></refund_id>" +
                "<refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>" +
                "<refund_request_source><![CDATA[API]]></refund_request_source>" +
                "<refund_status><![CDATA[SUCCESS]]></refund_status>" +
                "<settlement_refund_fee><![CDATA[1]]></settlement_refund_fee>" +
                "<settlement_total_fee><![CDATA[1]]></settlement_total_fee>" +
                "<success_time><![CDATA[2018-01-10 10:31:24]]></success_time>" +
                "<total_fee><![CDATA[1]]></total_fee>" +
                "<transaction_id><![CDATA[4200000052201801101409025381]]></transaction_id>" +
                "</root>";
        //加密
        String ReqInfo = encryptData(data, key);
        System.out.println(ReqInfo);
        //解密
        String decryptReqInfo = AESUtil.decryptData(ReqInfo, key);
        System.out.println(decryptReqInfo);
        System.out.println(data.equals(decryptReqInfo));
    }

}

