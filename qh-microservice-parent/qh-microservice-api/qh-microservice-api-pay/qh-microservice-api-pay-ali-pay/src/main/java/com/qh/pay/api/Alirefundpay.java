package com.qh.pay.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付宝退款
 * Created by Administrator on 2019/7/17.
 */
@RequestMapping(value = "/ali", produces = "application/json;charset=UTF-8")
public interface Alirefundpay {
    @PostMapping(value = "/refundpay")
    public String aliRefundpay(HttpServletResponse response, HttpServletRequest request) ;
}
