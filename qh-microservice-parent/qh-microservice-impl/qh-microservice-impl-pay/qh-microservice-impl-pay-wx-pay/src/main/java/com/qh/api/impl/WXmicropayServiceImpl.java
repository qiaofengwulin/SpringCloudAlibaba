package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.qh.annotation.CheckParams;
import com.qh.api.db.PayConfigService;
import com.qh.api.db.PayLogService;
import com.qh.api.designmode.MicropayContext;
import com.qh.api.designmode.StrategyErrCodeDesImpl;
import com.qh.api.designmode.StrategySUCCESSImpl;
import com.qh.api.designmode.StrategyUSERPAYINGImpl;
import com.qh.api.util.RequestWXData;
import com.qh.api.util.WXMicropayUtils;
import com.qh.api.wxsdk.WXPayConfig;
import com.qh.api.wxsdk.WXPayUtil;
import com.qh.entity.PayLog;
import com.qh.pay.api.WXmicropayService;
import com.qh.utils.PostUtil;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 营业员扫码付
 */
@RestController
public class WXmicropayServiceImpl implements WXmicropayService {
    @Autowired
    private PayLogService payLogService;
    @Autowired
    private PayConfigService payConfigService;

    @CheckParams
    @Override
    public String WXmicropay(HttpServletResponse response, HttpServletRequest request) {
        Map<String, String> mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        String ip = request.getLocalAddr().toString();
        try {
            //生成需要提交的xml数据
            String xml = WXMicropayUtils.packageParams(mapRequest, configMap, ip);
            //调用微信接口
            String createOrderData = PostUtil.sendPost(WXPayConfig.micropay_url, xml);
            //xml转成Map
            Map<String, String> createOrderMap = WXPayUtil.xmlToMap(createOrderData);
            MicropayContext micropayContext = null;
            //支付成功
            if (RequestWXData.isSUCCESSok(createOrderMap)) {
                payLogService.insertPayLog(RequestWXData.packagingPayLog(mapRequest, configMap, createOrderMap, "1"));
                micropayContext = new MicropayContext(new StrategySUCCESSImpl());
                return micropayContext.executeStrategy(createOrderMap, null);
            }
            //当支付发生错误返回字段会有err_code，当err_code值为USERPAYING时为用户需要输入密码支付，此时需要查订单状态判断是否支付成功
            if (createOrderMap.get("err_code") != null && createOrderMap.get("err_code").toString().equals(RequestWXData.USERPAYING)) {
                WXMicropayUtils wxMicropayUtils = new WXMicropayUtils();
                configMap.put(RequestWXData.OUT_TRADE_NO, mapRequest.get(RequestWXData.OUT_TRADE_NO));
                micropayContext = new MicropayContext(new StrategyUSERPAYINGImpl());
                String searchResult = micropayContext.executeStrategy(configMap, wxMicropayUtils);
                Map returnObjMap = JSON.parseObject(searchResult, Map.class);
                if (returnObjMap.get("msg").toString().equals("ok"))
                    payLogService.insertPayLog(RequestWXData.packagingPayLog(mapRequest, configMap, wxMicropayUtils.getResult(), "1"));
                return searchResult;
            } else {
                micropayContext = new MicropayContext(new StrategyErrCodeDesImpl());
                return micropayContext.executeStrategy(createOrderMap, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFail("请求失败");
    }

    @Override
    public String test(HttpServletResponse response, HttpServletRequest request) {
        PayLog payLog = new PayLog();
        return ResultsetUtil.returnSuccess(payLogService.queryPayLog(payLog));
    }
}
