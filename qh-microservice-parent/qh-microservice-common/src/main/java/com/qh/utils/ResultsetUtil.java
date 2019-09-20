package com.qh.utils;


import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/6/21.
 */
public class ResultsetUtil {

    public static String returnSuccess() {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 0);
        JSONObject.put("msg", "ok");
        return JSONObject.toString();
    }
    public static Map<String,String> returnSuccessMap() {
        Map<String,String> JSONObject = new HashMap<>();
        JSONObject.put("state", "0");
        JSONObject.put("msg", "ok");
        return JSONObject;
    }
    public static String returnSuccess(String msg) {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 0);
        JSONObject.put("msg", msg);
        return JSONObject.toString();
    }

    public static String returnSuccess(Map map) {
        map.put("state", 0);
        map.put("msg", "ok");
        return JSON.toJSONString(map);
    }

    public static String returnSuccess(List list) {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 0);
        JSONObject.put("msg", "ok");
        if (list.size() > 0) {
            JSONObject.put("rows", JSONArray.fromObject(list));
        } else {
            JSONObject.put("rows", "[]");
        }
        return JSONObject.toString();
    }

    public static String returnSuccess(int totalCount, List list) {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 0);
        JSONObject.put("msg", "ok");
        JSONObject.put("totalCount", totalCount);
        if (list.size() > 0) {
            JSONObject.put("rows", JSONArray.fromObject(list));
        } else {
            JSONObject.put("rows", "[]");
        }
        return JSONObject.toString();
    }

    public static String returnSuccess(String key, Object obj, List list) {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 0);
        JSONObject.put("msg", "ok");
        JSONObject.put(key, obj);
        if (list.size() > 0) {
            JSONObject.put("rows", JSONArray.fromObject(list));
        } else {
            JSONObject.put("rows", "[]");
        }
        return JSONObject.toString();
    }

    public static String returnFail() {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 1);
        JSONObject.put("msg", "fali");
        return JSONObject.toString();
    }

    public static String returnFail(String msg) {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 1);
        JSONObject.put("msg", msg);
        return JSONObject.toString();
    }
    public static  Map<String,String> returnFailMap(String msg) {
        Map<String,String> JSONObject = new HashMap<>();
        JSONObject.put("state", "1");
        JSONObject.put("msg", msg);
        return JSONObject;
    }
    public static String returnSuccess(JSONArray list) {
        JSONObject JSONObject = new JSONObject();
        JSONObject.put("state", 0);
        JSONObject.put("msg", "ok");
        if (list.size() > 0) {
            JSONObject.put("rows", JSONArray.fromObject(list));
        } else {
            JSONObject.put("rows", "[]");
        }
        return JSONObject.toString();
    }
}
