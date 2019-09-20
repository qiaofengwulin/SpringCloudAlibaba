package com.qh.db.config;

import com.google.common.collect.Maps;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ShardingMasterSlaveConfig.class)
@Log4j2
// 读取ds_master主数据源和读写分离配�?
@ConditionalOnProperty({ "sharding.jdbc.data-sources.ds_master.jdbc-url",
		"sharding.jdbc.master-slave-rule.master-data-source-name" })
public class ShardingDataSourceConfig {

	@Autowired
	private ShardingMasterSlaveConfig shardingMasterSlaveConfig;

	@Bean
	public DataSource masterSlaveDataSource() throws SQLException {
		final Map<String, DataSource> dataSourceMap = Maps.newHashMap();
		dataSourceMap.putAll(shardingMasterSlaveConfig.getDataSources());
		final Map<String, Object> newHashMap = Maps.newHashMap();
		// 创建 MasterSlave数据�?
		DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,
				shardingMasterSlaveConfig.getMasterSlaveRule(), newHashMap);
		log.info("masterSlaveDataSource config complete");
		return dataSource;
	}

}
