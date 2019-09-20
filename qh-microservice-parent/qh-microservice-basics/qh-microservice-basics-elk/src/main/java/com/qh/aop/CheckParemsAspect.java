package com.qh.aop;


import com.qh.api.db.PayConfigService;
import com.qh.utils.RequestUtil;
import com.qh.utils.ResultsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
public class CheckParemsAspect {
	@Autowired
	private PayConfigService payConfigService;
	@Pointcut("@annotation(com.qh.annotation.CheckParams)")
	private void serviceAspect() {
	}

	// 请求method前打印内容
	@Around(value = "serviceAspect()")
	public Object methodBefore(ProceedingJoinPoint point) throws Throwable {
		Map<String, String> mapRequest = RequestUtil.getReques(getRequest());
		if(mapRequest==null){
			return ResultsetUtil.returnFail("缺少参数！");
		}
		//查询医院配置信息
		String qhConfigHosValue = payConfigService.queryPayConfig(mapRequest);
		if(qhConfigHosValue==null){
			return ResultsetUtil.returnFail("商户号有误！");
		}
		return point.proceed();
	}


	private HttpServletRequest getRequest(){
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		return request;
	}


}
