package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.qh.annotation.CheckParams;
import com.qh.api.db.PayConfigService;
import com.qh.api.util.RequestWXData;
import com.qh.api.wxsdk.WXPayConfig;
import com.qh.api.wxsdk.WXPayUtil;
import com.qh.pay.api.WXQRCodePayService;
import com.qh.utils.PostUtil;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信二维码支付
 * Created by Administrator on 2019/8/20.
 */
@RestController
public class WXQRCodePayServiceImpl implements WXQRCodePayService {
    @Autowired
    private PayConfigService payConfigService;

    @CheckParams
    @Override
    public String WXpayQRCode(HttpServletResponse response, HttpServletRequest request) {
        Map mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        mapRequest.put("trade_type","NATIVE");
        mapRequest.put("notify_url", "www.baidu.com");//通知地址
        String ip = request.getLocalAddr().toString();
        String returndata =  "";
        try {
            //生成需要提交的xml数据
            String xml = RequestWXData.packageParams(mapRequest, configMap, ip);
            //调用微信
            String createOrderData = PostUtil.sendPost(WXPayConfig.unifiedorder_url, xml);
            Map<String, String> createOrderMap = WXPayUtil.xmlToMap(createOrderData);
            returndata = createOrderMap.get("code_url");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultsetUtil.returnFail(e.getMessage());
        }
        Map returnMap = new HashMap<>();
        returnMap.put("qr_code", returndata.trim());
        return ResultsetUtil.returnSuccess(returnMap);
    }
}
