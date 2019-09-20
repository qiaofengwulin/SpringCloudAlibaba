package com.qh;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagerServer
/**
 * TM后台http://127.0.0.1:7970/admin/index.html#/task
 * 密码  codingapi
 */
public class TransactionManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagerApplication.class, args);
    }
}
