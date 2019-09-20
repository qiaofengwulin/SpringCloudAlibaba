package com.qh.pay.api;

import com.alipay.api.AlipayApiException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RequestMapping(value = "/ali" , produces = "application/json;charset=UTF-8")
public interface Alimicropay {


    @PostMapping("/alimicropay")
    public String Alimicropay(HttpServletResponse response, HttpServletRequest request) throws AlipayApiException;

  /*  @PostMapping("/aliRefund")
    public void aliRefund(String auth_code) throws AlipayApiException;*/
}
