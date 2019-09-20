package com.qh.api.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qh.api.wxsdk.WXPayUtil;
import com.qh.entity.PayLog;
import com.qh.utils.DateUtil;
import com.qh.utils.PayLogUtils;
import com.qh.utils.ResultsetUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/10
 * @Version 1.0
 */
public class RequestWXData {
    public static final String SUCCESS = "SUCCESS";
    public static final String USERPAYING = "USERPAYING";
    public static final String TRADE_STATE = "trade_state";
    public static final String APP_ID = "appid";
    public static final String MCH_ID = "mch_id";
    public static final String BODY = "body";
    public static final String NONCE_STR = "nonce_str";
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String OUT_REFUND_NO = "out_refund_no";
    public static final String TOTAL_FEE = "total_fee";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String REFUND_FEE = "refund_fee";
    public static final String NOTIFY_URL = "notify_url";
    public static final String TRADE_TYPE = "trade_type";
    /**
     * 封装微信退款申请调用参数
     *
     * @param mapRequest
     * @param configMap
     * @return
     * @throws Exception
     */
    public static String packageParams(Map mapRequest, Map configMap) throws Exception {
        Map<String, String> requestWXData = new HashMap<String, String>();
        requestWXData.put(RequestWXData.APP_ID, configMap.get("appId").toString());
        requestWXData.put(RequestWXData.MCH_ID, configMap.get("mchId").toString());//随机字符串
        requestWXData.put(RequestWXData.NONCE_STR, WXPayUtil.generateNonceStr());//随机字符串
        requestWXData.put(RequestWXData.OUT_TRADE_NO, mapRequest.get(RequestWXData.OUT_TRADE_NO).toString());//订单号
        requestWXData.put(RequestWXData.OUT_REFUND_NO, mapRequest.get(RequestWXData.OUT_REFUND_NO).toString());//退款单号
        requestWXData.put(RequestWXData.TOTAL_FEE, mapRequest.get(RequestWXData.TOTAL_FEE).toString());//订单金额
        requestWXData.put(RequestWXData.REFUND_FEE, mapRequest.get(RequestWXData.REFUND_FEE).toString());//退款金额
        if (mapRequest.containsKey(RequestWXData.NOTIFY_URL))
            requestWXData.put(RequestWXData.NOTIFY_URL, mapRequest.get(RequestWXData.NOTIFY_URL).toString());//退款回调url
             /*------生成签名-------*/
        String sign = WXPayUtil.generateSignature(requestWXData, configMap.get("privateKey").toString());
        requestWXData.put("sign", sign);
             /*------生成需要提交的xml数据-------*/
        String xml = WXPayUtil.mapToXml(requestWXData);
        return xml;
    }

    /**
     * 封装微信支付与微信二维码调用参数
     *
     * @param mapRequest
     * @param configMap
     * @param ip
     * @return
     * @throws Exception
     */
    public static String packageParams(Map mapRequest, Map configMap, String ip) throws Exception {
        Map packageParams = new TreeMap<String, String>();
        packageParams.put(RequestWXData.APP_ID, configMap.get("appId").toString());//公众账号ID
        packageParams.put(RequestWXData.MCH_ID, configMap.get("mchId").toString());//商户号
        packageParams.put(RequestWXData.NONCE_STR, WXPayUtil.generateNonceStr());//随机字符串
        packageParams.put(RequestWXData.BODY, mapRequest.get(RequestWXData.BODY));//商品描述
        packageParams.put(RequestWXData.OUT_TRADE_NO, mapRequest.get(RequestWXData.OUT_TRADE_NO));//商户订单号
        packageParams.put(RequestWXData.TOTAL_FEE, mapRequest.get(RequestWXData.TOTAL_AMOUNT));//标价金额
        packageParams.put("spbill_create_ip", ip);//终端IP
        packageParams.put(RequestWXData.NOTIFY_URL, mapRequest.get(RequestWXData.NOTIFY_URL));//通知地址
        packageParams.put(RequestWXData.TRADE_TYPE, mapRequest.get(RequestWXData.TRADE_TYPE));//交易类型
        if (mapRequest.get(RequestWXData.TRADE_TYPE).toString().equals("JSAPI")) {
            packageParams.put("openid", mapRequest.get("openid"));//用户标识 trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
        }
        //生成签名
        String sign = WXPayUtil.generateSignature(packageParams, configMap.get("privateKey").toString());
        packageParams.put("sign", sign);//签名
        return WXPayUtil.mapToXml(packageParams);
    }

    /**
     * 处理微信支付返回参数用来调起收银台功能
     *
     * @param createOrderXML
     * @param configMap
     * @return
     * @throws Exception
     */
    public static String createOrderMap(String createOrderXML, Map configMap) throws Exception {
        Map<String, String> createOrderMap = WXPayUtil.xmlToMap(createOrderXML);
        if (isSUCCESSok(createOrderMap)) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("appId", configMap.get("appId").toString());
            resultMap.put("timeStamp", System.currentTimeMillis() + "");
            resultMap.put("nonceStr", WXPayUtil.generateNonceStr());
            resultMap.put("package", "prepay_id=" + createOrderMap.get("prepay_id"));
            String paySign = WXPayUtil.generateSignature(resultMap, configMap.get("privateKey").toString());
            resultMap.put("paySign", paySign);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trade_data", JSON.toJSONString(resultMap));
            return ResultsetUtil.returnSuccess(jsonObject);
        } else {
            if (!createOrderMap.get("return_code").toString().equals(RequestWXData.SUCCESS)) {
                return ResultsetUtil.returnFail(createOrderMap.get("return_msg"));
            }
            return ResultsetUtil.returnFail(createOrderMap.get("err_code_des"));
        }
    }

    /**
     * 封装 PayLog
     *
     * @param mapRequest
     * @param configMap
     * @param createOrderMap
     * @return
     */
    public static PayLog packagingPayLog(Map<String, String> mapRequest, Map configMap, Map createOrderMap, String payType) {
        //记录payLog内容
        PayLog payLog = new PayLog();
        payLog.setTradeNo(PayLogUtils.getTradeNoUUID());
        payLog.setFee(new BigDecimal(mapRequest.get(RequestWXData.TOTAL_AMOUNT).toString()));
        payLog.setCreateTime(null);
        payLog.setOutTradeNo(mapRequest.get(RequestWXData.OUT_TRADE_NO).toString());
        payLog.setPayType(payType);
        payLog.setMchId(configMap.get("mchId").toString());
        if (!createOrderMap.containsKey("outTradeRefundNo"))payLog.setTradeTime(DateUtil.parseDate(createOrderMap.get("time_end").toString(), "yyyyMMddHHmmss"));
        payLog.setPayNumber(createOrderMap.get("transaction_id").toString());
        if (createOrderMap.containsKey("outTradeRefundNo") && createOrderMap.get("outTradeRefundNo") != null) {
            payLog.setTradeTime(DateUtil.convert(createOrderMap.get("success_time").toString()));
            payLog.setOutTradeRefundNo(createOrderMap.get("outTradeRefundNo").toString());
        }
        return payLog;
    }

    /**
     * 业务成功判断
     */
    public static boolean isSUCCESSok(Map map) {
        if (map.get("return_code").toString().equals(RequestWXData.SUCCESS) && map.get("result_code").toString().equals(RequestWXData.SUCCESS)) {
            return true;
        }
        return false;
    }
}
