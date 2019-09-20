package com.qh.api.util;

import com.alipay.api.AlipayClient;
import com.qh.api.alisdk.AliPayUtils;
import com.qh.utils.ResultsetUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019-07-31.
 */
public class AliMicropayUtils {
    public static Map<String, String> checkOrderState(Map<String, String> map, AlipayClient client) {
        Map<String, String> result = new HashMap<>();
        Map<String, String> Aliresult = new HashMap<>();
        Map<String, String> WXreverse = new HashMap<>();
        int time = 5000;
        int jiange = 5000;
        while (time <= 60000) {
            try {
                Thread.sleep(jiange);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("扫码等待：" + map.toString());
            Aliresult = AliPayUtils.orderQuery(map,client);
            if (Aliresult.get("state").equals("0")) {
                return ResultsetUtil.returnSuccessMap();
            }else if(Aliresult.get("msg").equals("40004")){
                return ResultsetUtil.returnFailMap("订单不存在");
            }
            time = jiange + time;
        }
        // 撤销订单
        Aliresult= AliPayUtils.orderCancel(map,client);
        if (Aliresult.get("state").equals("0")) {
            return ResultsetUtil.returnFailMap("订单已撤销");
        }
        return ResultsetUtil.returnFailMap("fail");
    }
}
