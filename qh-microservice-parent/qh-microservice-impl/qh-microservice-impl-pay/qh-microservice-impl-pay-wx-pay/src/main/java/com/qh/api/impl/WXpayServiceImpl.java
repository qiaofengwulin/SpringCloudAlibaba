package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.qh.annotation.CheckParams;
import com.qh.api.db.PayConfigService;
import com.qh.api.util.RequestWXData;
import com.qh.api.wxsdk.WXPayConfig;
import com.qh.pay.api.WXpayService;
import com.qh.utils.PostUtil;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信支付
 * Created by Administrator on 2019/7/17.
 */
@RestController
public class WXpayServiceImpl implements WXpayService {
    @Autowired
    private PayConfigService payConfigService;

    @CheckParams
    @Override
    public String WXpay(HttpServletResponse response, HttpServletRequest request) {
        Map<String, String> mapRequest = RequestUtil.getReques(request);
        //查医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        mapRequest.put("trade_type", "JSAPI");
        mapRequest.put("notify_url", "www.baidu.com");
        String ip = request.getLocalAddr().toString();
        try {
            //调用微信
            String createOrderXML = PostUtil.sendPost(WXPayConfig.unifiedorder_url, RequestWXData.packageParams(mapRequest, configMap, ip));
            return RequestWXData.createOrderMap(createOrderXML, configMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFail("请求失败");
    }
}
