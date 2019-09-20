package com.qh.api.db;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/8/20.
 */
@RequestMapping(value = "/qh", produces = "application/json;charset=UTF-8")
public interface PayOrderInquiryService {
    @PostMapping(value = "/payorderinquiry")
    public String payOrderInquiry(HttpServletResponse response, HttpServletRequest request);
}
