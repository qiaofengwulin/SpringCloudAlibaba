package com.qh.api.db;

import com.qh.api.db.impl.PayLogServiceFeignImpl;
import com.qh.entity.PayLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@FeignClient(value = "qh-jdbc-service" ,fallback = PayLogServiceFeignImpl.class )
public interface PayLogService {
    @PostMapping(value = "/pay/insertPayLog",produces = "application/json;charset=UTF-8")
    public String insertPayLog(@RequestBody PayLog payLog);

    @PostMapping(value = "/pay/queryPayLog",produces = "application/json;charset=UTF-8")
    public String queryPayLog(@RequestBody PayLog payLog);
}
