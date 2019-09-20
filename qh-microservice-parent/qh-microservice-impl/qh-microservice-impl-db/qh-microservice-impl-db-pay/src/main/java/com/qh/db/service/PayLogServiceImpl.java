package com.qh.db.service;

import com.qh.api.db.PayLogService;
import com.qh.db.dao.mapper.extend.PayLogExtendMapper;
import com.qh.entity.PayLog;
import com.qh.utils.ResultsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RestController
public class PayLogServiceImpl implements PayLogService {

    @Autowired
    PayLogExtendMapper payLogExtendMapper;

    @Override
    public String insertPayLog(@RequestBody PayLog payLog) {
        int i=payLogExtendMapper.insert(payLog);
        System.out.println("操作数据库结果"+i);
        return i>=0? ResultsetUtil.returnSuccess():ResultsetUtil.returnFail();
    }

    @Override
    public String queryPayLog(@RequestBody PayLog payLog) {
        return ResultsetUtil.returnSuccess(payLogExtendMapper.queryPayLog(payLog));
    }
}
