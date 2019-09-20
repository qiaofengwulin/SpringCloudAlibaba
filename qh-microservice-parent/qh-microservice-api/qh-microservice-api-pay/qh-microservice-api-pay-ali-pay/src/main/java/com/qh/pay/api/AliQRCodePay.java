package com.qh.pay.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RequestMapping(value = "/ali", produces = "application/json;charset=UTF-8")
public interface AliQRCodePay {
    @PostMapping("/aliqrcodepay")
    public String AliQRCodePay(HttpServletRequest request) ;

    @PostMapping("/test")
    public String test(HttpServletRequest request) ;
}
