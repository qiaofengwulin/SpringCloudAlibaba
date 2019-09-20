package com.qh.db.service;

import com.qh.api.db.PayLogService;
import com.qh.api.db.PayOrderInquiryService;
import com.qh.entity.PayLog;
import com.qh.utils.ResultsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2019/8/20.
 */
@RestController
public class PayOrderInquiryServiceImpl implements PayOrderInquiryService {
    @Autowired
    private PayLogService payLogService;
    @Override
    public String payOrderInquiry(HttpServletResponse response, HttpServletRequest request) {
        PayLog payLog = new PayLog();
        return ResultsetUtil.returnSuccess(payLogService.queryPayLog(payLog));
    }
}
