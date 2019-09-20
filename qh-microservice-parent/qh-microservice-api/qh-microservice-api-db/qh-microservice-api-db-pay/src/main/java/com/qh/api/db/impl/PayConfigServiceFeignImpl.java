package com.qh.api.db.impl;

import com.qh.api.db.PayConfigService;
import com.qh.entity.QhConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@Component
public class PayConfigServiceFeignImpl implements PayConfigService {
    public String queryPayConfig(@RequestBody Map hosIdhosKey) {
        return "数据库错误！";
    }

    @Override
    public String queryPayConfigByMchId(QhConfig qhConfig) {
        return "数据库错误！";
    }
}
