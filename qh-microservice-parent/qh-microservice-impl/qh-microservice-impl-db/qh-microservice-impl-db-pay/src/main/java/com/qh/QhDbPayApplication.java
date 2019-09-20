package com.qh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by qiaozhonghuai on 2019/8/20.
 */
@SpringBootApplication
@MapperScan("com.qh.db.dao.mapper")
@EnableDiscoveryClient
@EnableFeignClients
//@EnableDistributedTransaction
public class QhDbPayApplication {
    public static void main(String[] args) {
      /* try {
            PropertiesUtil.initServiceIP(ResourceUtils.getURL("classpath:").getPath());
       }catch (Exception e){
            e.printStackTrace();
        }*/
        SpringApplication.run(QhDbPayApplication.class, args);
    }
}
