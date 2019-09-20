package com.qh;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
      /*  try {
            PropertiesUtil.initServiceIP(ResourceUtils.getURL("classpath:").getPath());
        }catch (Exception e){
            e.printStackTrace();
        }*/
        SpringApplication.run(GatewayApplication.class, args);
    }
}