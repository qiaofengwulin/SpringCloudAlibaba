package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.qh.annotation.CheckParams;
import com.qh.api.alisdk.AlipayConfig;
import com.qh.api.db.PayConfigService;
import com.qh.api.db.PayLogService;
import com.qh.api.util.PackagingParameters;
import com.qh.pay.api.Alirefundpay;
import com.qh.utils.DateUtil;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RestController
public class AlirefundpayImpl implements Alirefundpay {
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private PayConfigService payConfigService;

    @CheckParams
    @Override
    public String aliRefundpay(HttpServletResponse response, HttpServletRequest request) {
        Map mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);

        Map configMap = JSON.parseObject(qhConfigHosValue);
        mapRequest.put("appId", configMap.get("appId").toString());
        //初始化ali客户端
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, configMap.get("appId").toString(), configMap.get("privateKey").toString(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, configMap.get("publicKey").toString(), AlipayConfig.SIGNTYPE);

        AlipayTradeRefundRequest aliRequest = new AlipayTradeRefundRequest();
        //设置请求参数
        JSONObject json = new JSONObject();
        json.put("out_trade_no", mapRequest.get("out_trade_no").toString());  // 获取订单号
        json.put("refund_amount", mapRequest.get("refund_amount").toString());  // 获取退款金额
        aliRequest.setBizContent(json.toString());
        AlipayTradeRefundResponse alipayTradeRefundResponse = null;
        try {
            //访问ali
            alipayTradeRefundResponse = client.execute(aliRequest);
            //验签
            if (alipayTradeRefundResponse.isSuccess()) {
                payLogService.insertPayLog(PackagingParameters.packagingPayLog(mapRequest, alipayTradeRefundResponse, "2"));
                Map returnMap = new HashMap<>();
                returnMap.put("trade_no", alipayTradeRefundResponse.getTradeNo());
                returnMap.put("out_trade_no", alipayTradeRefundResponse.getOutTradeNo());
                returnMap.put("buyer_logon_id", alipayTradeRefundResponse.getBuyerLogonId());
                returnMap.put("fund_change", alipayTradeRefundResponse.getFundChange());
                returnMap.put("buyer_user_id", alipayTradeRefundResponse.getBuyerUserId());
                returnMap.put("gmt_refund_pay", DateUtil.DateToStr(alipayTradeRefundResponse.getGmtRefundPay()));
                returnMap.put("refund_fee", alipayTradeRefundResponse.getRefundFee());
                return ResultsetUtil.returnSuccess(returnMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFail(alipayTradeRefundResponse.getSubMsg());
    }
}
