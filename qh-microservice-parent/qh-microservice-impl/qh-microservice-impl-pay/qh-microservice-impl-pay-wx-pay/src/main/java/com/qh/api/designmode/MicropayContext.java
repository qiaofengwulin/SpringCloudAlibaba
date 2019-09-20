package com.qh.api.designmode;

import com.qh.api.util.WXMicropayUtils;

import java.util.Map;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/19
 * @Version 1.0
 */
public class MicropayContext {
    private Strategy strategy;
    public MicropayContext(Strategy strategy){
        this.strategy = strategy;
    }
    public String executeStrategy(Map map,WXMicropayUtils wxMicropayUtils){
        return strategy.micropay(map,wxMicropayUtils);
    }
}
