package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.qh.api.db.PayConfigService;
import com.qh.api.db.PayLogService;
import com.qh.api.util.RequestWXData;
import com.qh.api.wxsdk.WXPayUtil;
import com.qh.api.wxsdk.prevented;
import com.qh.entity.QhConfig;
import com.qh.pay.api.WXRefundNotificationService;
import com.qh.utils.AESUtil;
import com.qh.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信退款结果通知
 * Created by Administrator on 2019/7/31.
 */
@RestController
public class WXRefundNotificationServiceImpl implements WXRefundNotificationService {
    private final static Logger logger = LoggerFactory.getLogger(WXrefundpayServiceImpl.class);
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private PayConfigService payConfigService;

    @Override
    public String WXrefundnotification(HttpServletResponse response, HttpServletRequest request) {
        InputStream inStream = null;
        String WXresponse = "";
        Map returnWXData = new HashMap<>();
        returnWXData.put("return_code", "FAIL");
        returnWXData.put("return_msg", "解密失败");
        try {
            inStream = request.getInputStream();
            if (inStream == null) {
                Map<String, String> resultMap = prevented.getPreventedXML(inStream);
                System.out.println("微信缴费数据" + resultMap.toString());
                if (resultMap.get("return_code").equals("SUCCESS")) {
                    QhConfig qhConfig = new QhConfig();
                    qhConfig.setMchId(resultMap.get("mch_id").toString());
                    String qhConfigHosValue = payConfigService.queryPayConfigByMchId(qhConfig);
                    Map configMap = JSON.parseObject(qhConfigHosValue);
                    //微信私钥success_time
                    String privateKey = configMap.get("privateKey").toString();
                    String req_info = resultMap.get("req_info");
                    SecretKeySpec key = new SecretKeySpec(MD5Util.MD5Encode(privateKey).toLowerCase().getBytes(), AESUtil.getALGORITHM());
                    Map<String, String> reqInfoMap = WXPayUtil.xmlToMap(AESUtil.decryptData(req_info, key));
                    if ("SUCCESS".equals(reqInfoMap.get("refund_status").toString())) {
                        returnWXData.put("return_code", "SUCCESS");
                        returnWXData.put("return_msg", "OK");
                        reqInfoMap.put("total_amount", reqInfoMap.get("total_fee"));
                        reqInfoMap.put("mchId", resultMap.get("mch_id").toString());
                        reqInfoMap.put("outTradeRefundNo", reqInfoMap.get("out_refund_no").toString());
                        payLogService.insertPayLog(RequestWXData.packagingPayLog(reqInfoMap, reqInfoMap, reqInfoMap, "1000"));
                    }
                }
            }
            WXresponse = WXPayUtil.mapToXml(returnWXData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WXresponse;
    }
}
