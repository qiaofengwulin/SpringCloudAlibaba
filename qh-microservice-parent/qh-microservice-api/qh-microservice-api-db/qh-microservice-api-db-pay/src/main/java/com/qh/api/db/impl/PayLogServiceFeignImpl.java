package com.qh.api.db.impl;

import com.qh.api.db.PayLogService;
import com.qh.entity.PayLog;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@Component
public class PayLogServiceFeignImpl implements PayLogService {


    @Override
    public String insertPayLog(@RequestBody PayLog payLog) {
        return "服务繁忙";
    }

    @Override
    public String queryPayLog(@RequestBody PayLog payLog) {
        return "服务繁忙";
    }
}
