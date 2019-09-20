package com.qh.api.designmode;

import com.qh.api.util.WXMicropayUtils;

import java.util.Map;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/19
 * @Version 1.0
 */
public interface Strategy {
    String micropay(Map map,WXMicropayUtils wxMicropayUtils);
}
