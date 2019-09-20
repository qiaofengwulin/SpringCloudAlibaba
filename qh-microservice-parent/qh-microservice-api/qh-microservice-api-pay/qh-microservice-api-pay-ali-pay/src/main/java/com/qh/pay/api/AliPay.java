package com.qh.pay.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RequestMapping(value = "/ali", produces = "application/json;charset=UTF-8")
public interface AliPay {
    @PostMapping("/alipay")
    public String alipay(HttpServletResponse response, HttpServletRequest request);
}
