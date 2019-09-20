package com.qh.db.service;


import com.alibaba.fastjson.JSON;
import com.qh.api.db.PayConfigOperationService;
import com.qh.api.db.PayConfigService;
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
public class PayConfigOperationServiceImpl implements PayConfigOperationService {
    @Autowired
    PayConfigService payConfigService;
    @Override
    public String payconfigoperation(HttpServletResponse response, HttpServletRequest request) {
        Map requestMap = new HashMap<>();
        requestMap.put("hosName","1");
        requestMap.put("hosKey","1");
        String qhConfigHosValue = payConfigService.queryPayConfig(requestMap);
        Map configMap = JSON.parseObject(qhConfigHosValue);
        return configMap.get("name").toString();
    }
}
