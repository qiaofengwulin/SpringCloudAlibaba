package com.qh.db.dao.mapper.extend;

import com.qh.db.dao.mapper.QhConfigMapper;
import com.qh.entity.QhConfig;
import org.apache.ibatis.annotations.Param;

/**
 * @ProjectName payconfig
 * @Author qiaozhonghuai
 * @Date 2019/8/28
 * @Version 1.0
 */
public interface QhConfigMapperExtends extends QhConfigMapper {
    QhConfig selectByqhConfig(@Param("hosName") String hosName,@Param("hosKey")String hosKey);
    QhConfig selectByqhConfigByMuchId(QhConfig qhConfig);
}
