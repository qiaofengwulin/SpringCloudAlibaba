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
public class StrategyUSERPAYINGImpl implements Strategy {
    @Override
    public String micropay(Map map,WXMicropayUtils wxMicropayUtils) {
        if (wxMicropayUtils.checkOrderState(map)) {
            return ResultsetUtil.returnSuccess();
        } else if (wxMicropayUtils.getResult().get("err_code_des") != null) {
            return ResultsetUtil.returnFail(wxMicropayUtils.getResult().get("err_code_des"));
        }
        return ResultsetUtil.returnFail(wxMicropayUtils.getResult().get("return_msg"));
    }
}
