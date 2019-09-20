package com.qh.pay.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RequestMapping(value = "/weixin", produces = "application/json;charset=UTF-8")
public interface WXmicropayService {

    @PostMapping(value = "/wxmicropay")
    public String WXmicropay(HttpServletResponse response, HttpServletRequest request);

    @PostMapping(value = "/test")
    public String test(HttpServletResponse response, HttpServletRequest request);
}
