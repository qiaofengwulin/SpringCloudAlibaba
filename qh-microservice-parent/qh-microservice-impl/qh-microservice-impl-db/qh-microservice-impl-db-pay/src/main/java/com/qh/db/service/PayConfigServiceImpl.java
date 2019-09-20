package com.qh.db.service;

import com.qh.api.db.PayConfigService;
import com.qh.db.dao.mapper.extend.QhConfigMapperExtends;
import com.qh.entity.QhConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付配置db实现
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RestController
public class PayConfigServiceImpl implements PayConfigService {
    @Autowired
    private QhConfigMapperExtends qhConfigMapperExtends;

    @Override
    public String queryPayConfig(@RequestBody Map hosIdhosKey) {
        if(hosIdhosKey.get("hosName")!=null&&hosIdhosKey.get("hosKey")!=null){
            QhConfig qhConfig = qhConfigMapperExtends.selectByqhConfig(hosIdhosKey.get("hosName").toString(), hosIdhosKey.get("hosKey").toString());
            if(qhConfig!=null)return qhConfig.getHosValue();
        }
        return null;
    }

    @Override
    public String queryPayConfigByMchId(@RequestBody QhConfig qhConfig) {
        QhConfig Config =qhConfigMapperExtends.selectByqhConfigByMuchId(qhConfig);
        if(Config==null){
            return null;
        }
        return Config.getHosValue();
    }
}
