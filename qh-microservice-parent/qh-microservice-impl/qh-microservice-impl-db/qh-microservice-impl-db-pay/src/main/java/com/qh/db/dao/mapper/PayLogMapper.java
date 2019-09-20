package com.qh.db.dao.mapper;

import com.qh.entity.PayLog;

public interface PayLogMapper {

    int insert(PayLog record);

    int insertSelective(PayLog record);



}