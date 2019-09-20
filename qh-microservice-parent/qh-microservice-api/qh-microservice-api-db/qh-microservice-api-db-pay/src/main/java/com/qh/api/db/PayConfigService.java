package com.qh.api.db;

import com.qh.api.db.impl.PayConfigServiceFeignImpl;
import com.qh.entity.QhConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 支付配置接口
 * Created by qiaozhonghuai on 2019/8/26.
 */
@FeignClient(value = "qh-jdbc-service", fallback = PayConfigServiceFeignImpl.class)
public interface PayConfigService {

    @PostMapping(value = "/pay/queryPayConfig", produces = "application/json;charset=UTF-8")
    public String queryPayConfig(@RequestBody Map hosIdhosKey);

    @PostMapping(value = "/pay/queryPayConfigByMchId",produces = "application/json;charset=UTF-8")
    public String queryPayConfigByMchId(@RequestBody QhConfig qhConfig);
}
