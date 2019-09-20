package com.qh.api.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.qh.api.alisdk.AliPayUtils;
import com.qh.api.alisdk.AlipayConfig;
import com.qh.code.QhPayCode;
import com.qh.entity.PayLog;
import com.qh.utils.DateUtil;
import com.qh.utils.PayLogUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/10
 * @Version 1.0
 */
public class PackagingParameters {
    public static Map result(Map<String, String> mapRequest,  AlipayClient client) throws Exception {
        AlipayTradePayRequest tradePayRequest = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setOutTradeNo(mapRequest.get("out_trade_no"));
        model.setSubject(mapRequest.get("subject"));
        model.setTotalAmount(mapRequest.get("total_amount"));
        model.setBody(mapRequest.get("body"));
        model.setScene("bar_code");
        model.setAuthCode(mapRequest.get("auth_code"));
        tradePayRequest.setBizModel(model);
        AlipayTradePayResponse res = client.execute(tradePayRequest);
        String result = res.getBody();
        return AliPayUtils.parseAliJson(result, "alipay_trade_pay_response");
    }

    public static boolean businessEstimate(Map micro_result) {
        if (micro_result.get("code").equals(QhPayCode.ALI_PAY_SUCCESS_CODE) && micro_result.get("msg").toString().toUpperCase().equals("SUCCESS")) {
            return true;
        }
        return false;
    }


    public static PayLog packagingPayLog(Map<String, String> mapRequest,Map configMap,Map micro_result,String payType ) {
        //记录payLog内容
        PayLog payLog = new PayLog();
        payLog.setTradeNo(PayLogUtils.getTradeNoUUID());
        payLog.setFee(new BigDecimal(mapRequest.get("total_amount").toString()));
        payLog.setCreateTime(new Date());
        payLog.setOutTradeNo(mapRequest.get("out_trade_no").toString());
        payLog.setPayType(payType);
        payLog.setMchId(configMap.get("appId").toString());
        payLog.setOutTradeRefundNo(payType);
        payLog.setPayNumber(micro_result.get("trade_no").toString());
        payLog.setTradeTime(DateUtil.convert(micro_result.get("gmt_payment").toString()));
        return payLog;
    }
    public static PayLog packagingPayLog(Map<String, String> mapRequest,AlipayTradeRefundResponse alipayTradeRefundResponse,String payType ) {
        //记录payLog内容
        PayLog payLog = new PayLog();
        payLog.setTradeNo(PayLogUtils.getTradeNoUUID());
        payLog.setFee(new BigDecimal(alipayTradeRefundResponse.getRefundFee()));
        payLog.setCreateTime(new Date());
        payLog.setOutTradeNo(mapRequest.get("out_trade_no").toString());
        payLog.setPayType(payType);
        payLog.setMchId(mapRequest.get("appId").toString());
        payLog.setOutTradeRefundNo(alipayTradeRefundResponse.getOutTradeNo());
        payLog.setPayNumber(alipayTradeRefundResponse.getTradeNo());
        payLog.setTradeTime(alipayTradeRefundResponse.getGmtRefundPay());
        return payLog;
    }
}
