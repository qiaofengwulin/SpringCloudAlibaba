package com.qh.db.dao.mapper.extend;

import com.qh.db.dao.mapper.PayLogMapper;
import com.qh.entity.PayLog;

import java.util.List;
import java.util.Map;

public interface PayLogExtendMapper extends PayLogMapper {

       List<Map<String,Object>> queryPayLog(PayLog payLog);
}