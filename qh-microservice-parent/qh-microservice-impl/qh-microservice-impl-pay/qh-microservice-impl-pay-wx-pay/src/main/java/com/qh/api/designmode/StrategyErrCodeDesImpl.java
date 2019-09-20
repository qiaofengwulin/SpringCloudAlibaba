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
public class StrategyErrCodeDesImpl implements Strategy {
    @Override
    public String micropay(Map map,WXMicropayUtils wxMicropayUtils) {
        return ResultsetUtil.returnFail(map.get("err_code_des").toString());
    }
}
