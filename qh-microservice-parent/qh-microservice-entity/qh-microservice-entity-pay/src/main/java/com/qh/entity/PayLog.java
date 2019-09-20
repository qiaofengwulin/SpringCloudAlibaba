package com.qh.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class PayLog {
    //支付平台流水号
    private String tradeNo;
    //支付类型
    private String payType;
    //金额
    private BigDecimal fee;
    //微信、支付宝生成的流水号
    private String payNumber;
    //商户生成的订单号
    private String outTradeNo;
    //商户生成的退费订单号
    private String outTradeRefundNo;
    //商户号ID
    private String mchId;
    //创建时间
    private Date createTime;
    //交易时间
    private Date tradeTime;


}