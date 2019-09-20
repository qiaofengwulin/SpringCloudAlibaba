package com.qh.aop;

import com.alibaba.fastjson.JSONObject;
import com.qh.kafka.KafkaSender;
import com.qh.utils.IPUtils;
import com.qh.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName BaseApiService
 * @Description TODO
 * AOP拦截全局日志信息
 * @Author qiaozhonghuai
 * @Date 2019/7/18 0018 上午 9:21
 * @Version 1.0
 **/
@Aspect
@Component
@Slf4j
public class AopLogAspect {
	@Autowired
	private KafkaSender<JSONObject> kafkaSender;
	@Value("${server.port}")
	private String port;
	@Pointcut("execution(* com.qh.api.impl.*.*(..)) & execution(* com.qh.db.service.*.*(..))")
	private void serviceAspect() {
	}

	// 请求method前打印内容
	@Before(value = "serviceAspect()")
	public void methodBefore(JoinPoint joinPoint) {
		HttpServletRequest request = getRequest();
		Map<String, String> mapRequest = RequestUtil.getReques(request);
		// // 打印请求内容
		log.info("===============请求内容===============start");
		log.info("请求地址:" + request.getRequestURL().toString());
		log.info("请求方式:" + request.getMethod());
		log.info("请求类方法:" + joinPoint.getSignature());
		log.info("请求类方法参数:" + mapRequest.toString());
		log.info("===============请求内容===============end");

		JSONObject jsonObject = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		// 请求时间
		jsonObject.put("request_time", df.format(new Date()));
		// 加上ip和端口号
		setIpLog(jsonObject,request);
		// 请求URL
		jsonObject.put("request_url", request.getRequestURL().toString());
		// 请求的方法
		jsonObject.put("request_method", request.getMethod());
		// 请求类方法
		jsonObject.put("signature", joinPoint.getSignature());
		// 请求参数
		jsonObject.put("request_args", mapRequest.toString());
		JSONObject requestJsonObject = new JSONObject();
		requestJsonObject.put("request", jsonObject);
		kafkaSender.send(requestJsonObject);
	}

	// 在方法执行完结后打印返回内容
	@AfterReturning(returning = "o", pointcut = "serviceAspect()")
	public void methodAfterReturing(Object o) {
		HttpServletRequest request = getRequest();
		 log.info("--------------返回内容----------------");
		 log.info("Response内容:" + JSONObject.toJSONString(o));
		 log.info("--------------返回内容----------------");
		JSONObject respJSONObject = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		jsonObject.put("response_time", df.format(new Date()));
		setIpLog(jsonObject,request);
		jsonObject.put("response_content", JSONObject.toJSONString(o));

		respJSONObject.put("response", jsonObject);
		kafkaSender.send(respJSONObject);

	}

	private HttpServletRequest getRequest(){
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return request;
	}

	private void setIpLog(JSONObject jsonObject,HttpServletRequest request){
		// 加上本地服务ip和端口号
		InetAddress ia=null;
		try {
			ia=ia.getLocalHost();
			String localip=ia.getHostAddress();
			jsonObject.put("localhost_ipAddress", localip+":"+port);
		} catch (Exception e) {
			jsonObject.put("localhost_ipAddress", "");
		}
		//加上请求ip
		String ipAddress = IPUtils.getIpAddress(request);
		jsonObject.put("request_ipAddress", ipAddress);

	}
}
