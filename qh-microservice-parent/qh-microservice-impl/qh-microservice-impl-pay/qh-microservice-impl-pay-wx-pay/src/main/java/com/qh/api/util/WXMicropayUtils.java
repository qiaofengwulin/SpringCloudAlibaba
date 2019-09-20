package com.qh.api.util;


import com.qh.api.wxsdk.WXPayUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2019-07-31.
 */
public class WXMicropayUtils {
    private Map<String, String> result = new HashMap<>();

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }

    public boolean checkOrderState(Map<String, String> map) {
        Map<String, String> mapOrder = new HashMap<>();
        mapOrder.put(RequestWXData.APP_ID, map.get("appId").toString());//公众账号ID
        mapOrder.put(RequestWXData.MCH_ID, map.get("mchId").toString());//商户号
        mapOrder.put(RequestWXData.OUT_TRADE_NO, map.get(RequestWXData.OUT_TRADE_NO));//商户订单号
        mapOrder.put(RequestWXData.NONCE_STR, WXPayUtil.generateNonceStr());
        Map<String, String> WXresult = new HashMap<>();
        int time = 0;
        int jiange = 2000;
        while (time <= 30000) {
            try {
                Thread.sleep(jiange);//阻塞时间（毫秒）
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("扫码等待：" + mapOrder.toString());
            //查询订单
            WXresult = WXPayUtil.orderQuery(mapOrder, map.get("privateKey").toString());
            System.out.println(WXresult.toString());
            if (WXresult.get(RequestWXData.TRADE_STATE) == null) {
                this.setResult(WXresult);
                return false;
            } else if (WXresult.get(RequestWXData.TRADE_STATE).toString().equals(RequestWXData.SUCCESS)) {
                this.setResult(WXresult);
                return true;
            } else if (!WXresult.get(RequestWXData.TRADE_STATE).toString().equals(RequestWXData.USERPAYING)) {
                // 当状态不是等待用户输入密码时并且不是成功状态 ，撤销订单
                this.setResult(WXresult);
                return revocationOrder(mapOrder, map.get("privateKey").toString());
            }
            time = jiange + time;
        }
        // 撤销订单
        this.setResult(WXresult);
        return revocationOrder(mapOrder, map.get("privateKey").toString());
    }

    /**
     * 封装撤销订单
     *
     * @param map
     * @param privateKey
     * @return
     */
    private static boolean revocationOrder(Map<String, String> map, String privateKey) {
        WXPayUtil.wxReverse(map, privateKey);
        return false;
    }

    /**
     * 微信WXMicropay支付参数
     *
     * @param mapRequest
     * @param configMap
     * @param ip
     * @return
     * @throws Exception
     */
    public static String packageParams(Map mapRequest, Map configMap, String ip) throws Exception {
        Map requestWXData = new TreeMap<String, String>();
        requestWXData.put(RequestWXData.APP_ID, configMap.get("appId").toString());//公众账号ID
        requestWXData.put(RequestWXData.MCH_ID, configMap.get("mchId").toString());//商户号
        requestWXData.put(RequestWXData.NONCE_STR, WXPayUtil.generateNonceStr());//随机字符串
        requestWXData.put(RequestWXData.BODY, mapRequest.get(RequestWXData.BODY));//商品描述
        requestWXData.put(RequestWXData.OUT_TRADE_NO, mapRequest.get(RequestWXData.OUT_TRADE_NO));//商户订单号//自生成返回
        requestWXData.put(RequestWXData.TOTAL_FEE, mapRequest.get(RequestWXData.TOTAL_AMOUNT));//标价金额
        requestWXData.put("spbill_create_ip", ip);//终端IP
        requestWXData.put("auth_code", mapRequest.get("auth_code"));//授权码
        //生成签名
        String sign = WXPayUtil.generateSignature(requestWXData, configMap.get("privateKey").toString());
        requestWXData.put("sign", sign);//签名
        return WXPayUtil.mapToXml(requestWXData);
    }
}
