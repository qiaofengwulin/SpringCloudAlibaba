package com.qh.pay.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/8/20.
 */
@RequestMapping(value = "/weixin", produces = "application/json;charset=UTF-8")
public interface WXpayService {
    @PostMapping(value = "/wxpay")
    public String WXpay(HttpServletResponse response, HttpServletRequest request);
}
