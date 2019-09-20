package com.qh.utils;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * Created by Administrator on 2019/7/19.
 */
public class PropertiesUtil {

    private static final long serialVersionUID = 1L;


    private String path;

    private Properties prop = new Properties();

    /**
     * 指定Properties路径
     *
     * @param path
     */
    public PropertiesUtil(String path) {
        this.path = path;
    }

    /**
     * 读取Properties文件
     */
    public Map getProperties() {
        Map map = new HashMap<>();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            prop.load(in);     ///加载属性列表
            Iterator<String> it = prop.stringPropertyNames().iterator();
            while (it.hasNext()) {
                String key = it.next();
                map.put(key, prop.getProperty(key));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 写入Properties文件
     *
     * @param k
     * @param v
     */
    public void put(String k, String v) {
        try {
            FileOutputStream oFile = new FileOutputStream(path, false);//true表示追加打开
            prop.setProperty(k, v);
            prop.store(oFile, "The New properties file");
            oFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initServiceIP(String path) {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            PropertiesUtil propertiesUtil = new PropertiesUtil(path + "bootstrap.properties");
            propertiesUtil.getProperties();
            propertiesUtil.put("spring.cloud.nacos.discovery.ip", address.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}