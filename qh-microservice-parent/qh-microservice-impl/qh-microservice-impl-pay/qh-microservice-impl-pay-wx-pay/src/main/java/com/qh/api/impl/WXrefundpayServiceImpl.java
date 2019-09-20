package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.qh.annotation.CheckParams;
import com.qh.api.db.PayConfigService;
import com.qh.api.util.RequestWXData;
import com.qh.api.wxsdk.WXPayConfig;
import com.qh.api.wxsdk.WXPayUtil;
import com.qh.pay.api.WXrefundpayService;
import com.qh.utils.PostUtil;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 微信退款申请
 * Created by Administrator on 2019/8/20.
 */
@RestController
public class WXrefundpayServiceImpl implements WXrefundpayService {
    private final static Logger logger = LoggerFactory.getLogger(WXrefundpayServiceImpl.class);
    @Autowired
    private PayConfigService payConfigService;

    @Override
    @CheckParams
    public String WXrefundpay(HttpServletResponse response, HttpServletRequest request) {
        Map mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        try {
             /*------调用微信接口----------*/
            String createOrderData = PostUtil.payHttps(WXPayConfig.refund_url, RequestWXData.packageParams(mapRequest, configMap), configMap.get("mchId").toString());
            Map<String, String> createOrderMap = WXPayUtil.xmlToMap(createOrderData);
            if (RequestWXData.isSUCCESSok(createOrderMap)) {
                return ResultsetUtil.returnSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFail("退款申请失败");
    }
}