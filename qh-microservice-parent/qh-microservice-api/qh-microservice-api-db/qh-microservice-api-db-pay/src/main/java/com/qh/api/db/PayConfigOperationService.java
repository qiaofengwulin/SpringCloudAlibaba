package com.qh.api.db;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@RequestMapping(value = "/qh", produces = "application/json;charset=UTF-8")
public interface PayConfigOperationService {
    @PostMapping(value = "/payconfigoperation")
    public String payconfigoperation(HttpServletResponse response, HttpServletRequest request);
}
