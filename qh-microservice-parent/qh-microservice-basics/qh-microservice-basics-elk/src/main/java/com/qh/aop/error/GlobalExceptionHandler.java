package com.qh.aop.error;

import com.alibaba.fastjson.JSONObject;
import com.qh.kafka.KafkaSender;
import com.qh.utils.IPUtils;
import com.qh.utils.ResultsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName BaseApiService
 * @Description TODO
 * 全局异常捕获
 * @Author qiaozhonghuai
 * @Date 2019/7/18 0018 上午 9:21
 * @Version 1.0
 **/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@Autowired
	private KafkaSender<JSONObject> kafkaSender;
	@Value("${server.port}")
	private String port;
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public String exceptionHandler(Exception e) {
		log.info("###全局捕获异常###,error:{}", e);
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		// 1.封装异常日志信息
		JSONObject errorJson = new JSONObject();
		JSONObject logJson = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		logJson.put("request_time", df.format(new Date()));
		logJson.put("error_info", e);
		{
			// 加上本地服务ip和端口号
			InetAddress ia=null;
			try {
				ia=ia.getLocalHost();
				String localip=ia.getHostAddress();
				logJson.put("error_localhost_ipAddress", localip+":"+port);
			} catch (Exception e1) {
				logJson.put("error_localhost_ipAddress", "");
			}
			//加上请求ip
			String ipAddress = IPUtils.getIpAddress(request);
			logJson.put("error_ipAddress", ipAddress);

		}
		errorJson.put("request_error", logJson);
		kafkaSender.send(errorJson);
		return ResultsetUtil.returnFail("系统错误");
	}
}
