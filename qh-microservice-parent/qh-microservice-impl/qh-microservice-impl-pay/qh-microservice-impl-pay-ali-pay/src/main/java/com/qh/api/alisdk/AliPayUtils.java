package com.qh.api.alisdk;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.qh.utils.ResultsetUtil;

import java.util.Map;

/**
 * Created by Administrator on 2019-08-01.
 *
 * @author lgc
 */
public class AliPayUtils {

    /**
     * 查询订单
     *
     * @return
     */
    public static Map<String, String> orderQuery(Map<String, String> map, AlipayClient client) {
        AlipayTradeQueryRequest query = new AlipayTradeQueryRequest();
        query.setBizContent("{\"out_trade_no\":\"" + map.get("out_trade_no") + "\"" + "}");
        AlipayTradeQueryResponse response = null;
        try {
            response = client.execute(query);
            String json=response.getBody();
            Map micro_result=parseAliJson(json,"alipay_trade_query_response");
            System.out.println("查询订单接口：" + micro_result);
            if (micro_result.get("code").toString().equals("10000") && micro_result.get("msg").toString().toUpperCase().equals("SUCCESS")) {
                if(micro_result.get("trade_status").toString().equals("TRADE_SUCCESS")){
                    return ResultsetUtil.returnSuccessMap();
                }else if(micro_result.get("trade_status").toString().equals("WAIT_BUYER_PAY")){
                    return ResultsetUtil.returnFailMap("等待付款");
                }else{
                    return ResultsetUtil.returnFailMap(micro_result.get("trade_status").toString());
                }
            }else if(micro_result.get("code").toString().equals("40004")){
                return ResultsetUtil.returnFailMap(micro_result.get("code").toString());
            }
            else{
                return ResultsetUtil.returnFailMap(micro_result.get("sub_msg").toString());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFailMap("fail");
    }

    /**
     * 撤销订单
     *
     * @return
     */
    public static Map<String, String> orderCancel(Map<String, String> map,AlipayClient client) {
        AlipayTradeCancelRequest cancel = new AlipayTradeCancelRequest();
        cancel.setBizContent("{\"out_trade_no\":\"" + map.get("out_trade_no") + "\"" + "}");
        AlipayTradeCancelResponse response = null;
        try {
            response = client.execute(cancel);
            String json=response.getBody();
            Map micro_result=parseAliJson(json,"alipay_trade_cancel_response");
            System.out.println("撤销订单接口：" + micro_result);
            if (micro_result.get("code").toString().equals("10000") && micro_result.get("msg").toString().toUpperCase().equals("SUCCESS")) {
                return ResultsetUtil.returnSuccessMap();
            }else{
                return ResultsetUtil.returnFailMap(micro_result.get("sub_msg").toString());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return ResultsetUtil.returnFailMap("fail");
    }
    public static  Map parseAliJson(String json,String key){
        Map ail=JSON.parseObject(json);
        ail=JSON.parseObject(ail.get(key).toString());
        return  ail;
    }
  /* public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no","1564640380757");
       orderQuery(map);
    }*/
}
