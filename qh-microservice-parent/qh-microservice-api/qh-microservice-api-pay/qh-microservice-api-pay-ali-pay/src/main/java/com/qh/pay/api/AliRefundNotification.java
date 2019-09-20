package com.qh.pay.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/8/1.
 */

@RequestMapping(value = "/ali", produces = "application/json;charset=UTF-8")
public interface AliRefundNotification {

    @PostMapping(value = "/refundnotification")
    public String Alirefundnotification(HttpServletResponse response, HttpServletRequest request) ;
}
