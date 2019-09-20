package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.qh.annotation.CheckParams;
import com.qh.api.alisdk.AlipayConfig;
import com.qh.api.db.PayConfigService;
import com.qh.pay.api.AliPay;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
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
public class AliPayImpl implements AliPay {
    @Autowired
    private PayConfigService payConfigService;

    @CheckParams
    @Override
    public String alipay(HttpServletResponse response, HttpServletRequest request) {
        AlipayTradeCreateRequest alipayTradeCreateRequest = new AlipayTradeCreateRequest();
        Map<String, String> mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, configMap.get("appId").toString(), configMap.get("privateKey").toString(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, configMap.get("publicKey").toString(), AlipayConfig.SIGNTYPE);
        // 封装请求支付信息
        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(mapRequest.get("out_trade_no"));
        model.setBuyerId(mapRequest.get("buyerId"));
        model.setBuyerLogonId(mapRequest.get("buyerLogonId"));
        model.setSubject(mapRequest.get("subject"));
        model.setTotalAmount(mapRequest.get("total_amount"));
        alipayTradeCreateRequest.setBizModel(model);
        // 设置异步通知地址
        alipayTradeCreateRequest.setNotifyUrl(mapRequest.get("notifyUrl"));
        // 设置同步地址
        alipayTradeCreateRequest.setReturnUrl(mapRequest.get("returnUrl"));
        try {
            AlipayTradeCreateResponse res = client.execute(alipayTradeCreateRequest);
            if (res.isSuccess()) {
                Map map = new HashMap<>();
                map.put("trade_data", res.getBody());
                return ResultsetUtil.returnSuccess(map);
            } else {
                Map map = JSON.parseObject(res.getBody(), Map.class);
                map = JSON.parseObject(map.get("alipay_trade_create_response").toString(), Map.class);
                return ResultsetUtil.returnFail(map.get("sub_msg").toString());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFail();
    }
}
