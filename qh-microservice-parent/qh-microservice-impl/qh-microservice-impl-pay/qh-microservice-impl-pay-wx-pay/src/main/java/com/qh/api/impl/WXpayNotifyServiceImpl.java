package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.qh.api.db.PayConfigService;
import com.qh.api.db.PayLogService;
import com.qh.api.util.RequestWXData;
import com.qh.api.wxsdk.WXPayUtil;
import com.qh.api.wxsdk.prevented;
import com.qh.entity.QhConfig;
import com.qh.pay.api.WXpayNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付回调通知
 * Created by Administrator on 2019/8/20.
 */
@RestController
public class WXpayNotifyServiceImpl implements WXpayNotifyService {
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private PayConfigService payConfigService;

    @Override
    public String WXpayNotify(HttpServletResponse response, HttpServletRequest request) {
        InputStream inStream = null;
        String WXresponse = "";
        Map returnWXData = new HashMap<>();
        returnWXData.put("return_code", "FAIL");
        returnWXData.put("return_msg", "验签失败");
        try {
            inStream = request.getInputStream();
            if (inStream != null) {
                Map<String, String> resultMap = prevented.getPreventedXML(inStream);
                System.out.println("微信缴费数据" + resultMap.toString());
                //查询医院配置信息
                QhConfig qhConfig = new QhConfig();
                qhConfig.setMchId(resultMap.get("mch_id").toString());
                String qhConfigHosValue = payConfigService.queryPayConfigByMchId(qhConfig);
                Map configMap = JSON.parseObject(qhConfigHosValue);
                String privateKey = configMap.get("privateKey").toString();
                if (WXPayUtil.isSignatureValid(resultMap, privateKey)) {
                    returnWXData.put("return_code", "SUCCESS");
                    returnWXData.put("return_msg", "OK");
                    resultMap.put("total_amount", resultMap.get("total_fee"));
                    resultMap.put("mchId", resultMap.get("mch_id").toString());
                    payLogService.insertPayLog(RequestWXData.packagingPayLog(resultMap, resultMap, resultMap, "1000"));
                }
            }
            WXresponse = WXPayUtil.mapToXml(returnWXData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WXresponse;
    }
}
