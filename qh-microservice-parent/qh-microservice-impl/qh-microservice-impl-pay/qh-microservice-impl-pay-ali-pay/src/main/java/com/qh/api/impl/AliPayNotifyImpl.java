package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.qh.api.alisdk.AlipayConfig;
import com.qh.api.db.PayConfigService;
import com.qh.api.db.PayLogService;
import com.qh.entity.PayLog;
import com.qh.entity.QhConfig;
import com.qh.pay.api.AliPayNotify;
import com.qh.utils.DateUtil;
import com.qh.utils.PayLogUtils;
import com.qh.utils.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/17.
 */
@RestController
@RequestMapping(value = "/ali", produces = "application/json;charset=UTF-8")
public class AliPayNotifyImpl implements AliPayNotify {
    @Autowired
    PayLogService payLogService;
    @Autowired
    private PayConfigService payConfigService;

    @Override
    public String alipayNotify(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException {
        {
            Map<String, String> mapRequest = RequestUtil.getReques(request);
            Map map = JSON.parseObject(JSON.toJSONString(mapRequest));
            Map<String, String> aliResult = new HashMap<>();
            for (Object o : map.keySet()) {
                System.out.println("key:" + o.toString() + ",values:" + map.get(o).toString());
                aliResult.put(o.toString(), JSON.parseArray(map.get(o).toString()).get(0).toString());
            }

            QhConfig qhConfig = new QhConfig();
            qhConfig.setMchId(aliResult.get("app_id"));
            String json = payConfigService.queryPayConfigByMchId(qhConfig);
            if (json == null) {
                return "fail";
            }

            JSONObject jsonObject = (JSONObject) JSON.parse(json);
            //验签
            boolean signVerified = AlipaySignature.rsaCheckV1(aliResult, jsonObject.get("publicKey").toString(), AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            if (!signVerified) {
                System.out.println("验签失败-----------");
                return "fail";
            }
            PayLog payLog = new PayLog();
            payLog.setTradeNo(PayLogUtils.getTradeNoUUID());
            payLog.setFee(new BigDecimal(aliResult.get("total_amount").toString()));
            payLog.setCreateTime(new Date());
            payLog.setOutTradeNo(aliResult.get("out_trade_no").toString());
            payLog.setPayType("2");
            payLog.setPayNumber(aliResult.get("trade_no").toString());
            payLog.setMchId(aliResult.get("app_id"));
            payLog.setTradeTime(DateUtil.StringToDate(aliResult.get("gmt_create").toString(), "yyyy-MM-dd HH:mm:ss"));
            String msg = payLogService.insertPayLog(payLog);
            JSONObject result = (JSONObject) JSON.parse(msg);
            if (result.get("state").toString().equals("0"))
                return "success";
            return "fail";
        }
    }
}
