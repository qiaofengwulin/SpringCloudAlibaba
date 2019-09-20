package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.qh.annotation.CheckParams;
import com.qh.api.alisdk.AlipayConfig;
import com.qh.api.db.PayConfigService;
import com.qh.pay.api.AliQRCodePay;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RestController
public class AliQRCodePayImpl implements AliQRCodePay {
    @Autowired
    private PayConfigService payConfigService;
    @CheckParams
    @Override
    public String AliQRCodePay(HttpServletRequest request) {
        Map mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, configMap.get("appId").toString(), configMap.get("privateKey").toString(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, configMap.get("publicKey").toString(), AlipayConfig.SIGNTYPE);
        AlipayTradePrecreateRequest aliRequest = new AlipayTradePrecreateRequest();
        JSONObject json = new JSONObject();
        // 获取订单号
        json.put("out_trade_no", mapRequest.get("out_trade_no").toString());
        json.put("total_amount", mapRequest.get("total_amount").toString());
        json.put("subject", mapRequest.get("subject").toString());
        // 设置回调地址
        aliRequest.setNotifyUrl(mapRequest.get("notifyUrl").toString());
        aliRequest.setBizContent(json.toString());
        AlipayTradePrecreateResponse response = null;
        try {
            response = alipayClient.execute(aliRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            Map returnMap = new HashMap<>();
            returnMap.put("qr_code", response.getQrCode());
            returnMap.put("out_trade_no", response.getOutTradeNo());
            return ResultsetUtil.returnSuccess(returnMap);
        } else {
            return ResultsetUtil.returnFail(response.getMsg());
        }
    }

    @Override
    public String test(HttpServletRequest request) {
        return "";
    }
}
