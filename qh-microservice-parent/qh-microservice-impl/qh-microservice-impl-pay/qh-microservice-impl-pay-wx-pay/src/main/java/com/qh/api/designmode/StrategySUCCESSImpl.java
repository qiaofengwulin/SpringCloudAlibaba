package com.qh.api.designmode;

import com.qh.api.util.WXMicropayUtils;
import com.qh.utils.ResultsetUtil;

import java.util.Map;

/**
 * @ProjectName qh-microservice-parent
 * @Author qiaozhonghuai
 * @Date 2019/9/19
 * @Version 1.0
 */
public class StrategySUCCESSImpl implements Strategy {
    @Override
    public String micropay(Map map,WXMicropayUtils wxMicropayUtils) {
        return ResultsetUtil.returnSuccess();
    }
}
