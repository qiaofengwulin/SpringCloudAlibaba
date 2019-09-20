package com.qh.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/7/29.
 */
public class RequestUtil {
    public static Map getReques(HttpServletRequest request){
        StringBuilder stringBuilder = new StringBuilder();
        Map mapRequest=new HashMap<>();
        try {
            Enumeration params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String paramName = (String) params.nextElement();
                stringBuilder.append(paramName);
                break;
            }
            String json = stringBuilder.toString();
            //请求本内容
            mapRequest = JSON.parseObject(json, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mapRequest;
    }
}
