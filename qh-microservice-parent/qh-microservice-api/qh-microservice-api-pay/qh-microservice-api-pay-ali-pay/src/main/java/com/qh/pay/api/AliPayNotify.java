package com.qh.pay.api;

import com.alipay.api.AlipayApiException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/7/17.
 */
@RestController
@RequestMapping(value = "/ali", produces = "application/json;charset=UTF-8")
public interface AliPayNotify {

    @PostMapping(value = "/alipayNotify")
    public String alipayNotify(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException;


}
