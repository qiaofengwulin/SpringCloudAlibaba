package com.qh.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
/**
 * Created by qiaozhonghuai on 2019/8/26.
 */
@Component
@Slf4j
public class KafkaSender<T> {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * 需要在各自的微服务中的配置文件上加入spring.application.name的配置，如：（spring.application.name=member-server）
	 * 从而实现日志按照微服务模块进行划分，方便查询
	 **/
	@Value("${spring.application.name}")
	private String serverName;

	/**
	 * kafka 发送消息
	 *
	 * @param obj
	 *            消息对象
	 */
	public void send(T obj) {
		String jsonObj = JSON.toJSONString(obj);
		log.info("------------ message = {}", jsonObj);
		// 发送消息 实现可配置化 主题是可配置化
		log.info("serverName------------>"+serverName);
		if(serverName.toLowerCase().indexOf("pay") != -1){
			ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("qh-pay-service_log", jsonObj);
			callback(future);
		}else if(serverName.toLowerCase().indexOf("jdbc") != -1){
			ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("qh-pay-db-service_log", jsonObj);
			callback(future);
		}else{
			ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send("default_log", jsonObj);
			callback(future);
		}


	}

	private void callback(ListenableFuture<SendResult<String, Object>> future){
		future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
			@Override
			public void onFailure(Throwable throwable) {
				log.info("Produce: The message failed to be sent:" + throwable.getMessage());
			}

			@Override
			public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
				// TODO 业务处理
				log.info("Produce: The message was sent successfully:");
				log.info("Produce: _+_+_+_+_+_+_+ result: " + stringObjectSendResult.toString());
			}
		});
	}

}