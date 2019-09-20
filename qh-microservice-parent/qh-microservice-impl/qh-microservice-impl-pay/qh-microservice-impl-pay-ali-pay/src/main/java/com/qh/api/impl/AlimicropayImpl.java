package com.qh.api.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.qh.annotation.CheckParams;
import com.qh.api.alisdk.AlipayConfig;
import com.qh.api.db.PayConfigService;
import com.qh.api.db.PayLogService;
import com.qh.api.util.AliMicropayUtils;
import com.qh.api.util.PackagingParameters;
import com.qh.code.QhPayCode;
import com.qh.pay.api.Alimicropay;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RestController
public class AlimicropayImpl implements Alimicropay {

    @Autowired
    private PayLogService payLogService;
    @Autowired
    private PayConfigService payConfigService;

    @Override
    @CheckParams
    public String Alimicropay(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException {
        Map<String, String> mapRequest = RequestUtil.getReques(request);
        //查询医院配置信息
        String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
        //配置信息
        Map configMap = JSON.parseObject(qhConfigHosValue);
        try {
            AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, configMap.get("appId").toString(), configMap.get("privateKey").toString(), AlipayConfig.FORMAT, AlipayConfig.CHARSET, configMap.get("publicKey").toString(), AlipayConfig.SIGNTYPE);

            Map micro_result = PackagingParameters.result(mapRequest, client);
            if (PackagingParameters.businessEstimate(micro_result)) {
                payLogService.insertPayLog(PackagingParameters.packagingPayLog(mapRequest, configMap, micro_result, "22"));
                return ResultsetUtil.returnSuccess();
            } else if (!micro_result.get("code").equals(QhPayCode.ALI_PAY_REPETITION_CODE)) {
                return ResultsetUtil.returnFail(micro_result.get("msg").toString());
            } else if (micro_result.get("sub_code") != null) {
                if (micro_result.get("sub_code").equals(QhPayCode.ALI_PAY_SUB_CODE))
                    return ResultsetUtil.returnFail(micro_result.get("sub_msg").toString());
            }
            Map<String, String> AliMap = AliMicropayUtils.checkOrderState(mapRequest, client);
            if (AliMap.get("state").equals("0")) {
                payLogService.insertPayLog(PackagingParameters.packagingPayLog(mapRequest, configMap, micro_result, "22"));
                return ResultsetUtil.returnSuccess();
            } else {
                return ResultsetUtil.returnFail(AliMap.get("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFail();

    }

  /*  @Override
    public void aliRefund(String auth_code) throws AlipayApiException {
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent("{" +
                "\"out_trade_no\":\"1563523481121\"," +
                "\"refund_amount\":0.01" +
                "  }");
        AlipayTradeRefundResponse response = client.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

    }*/
}
