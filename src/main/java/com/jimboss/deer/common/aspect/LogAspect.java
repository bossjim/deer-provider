package com.jimboss.deer.common.aspect;

import com.jimboss.deer.common.properties.DeerProperties;
import com.jimboss.deer.common.utils.HttpContextUtil;
import com.jimboss.deer.common.utils.IPUtil;
import com.jimboss.deer.common.utils.JWTUtil;
import com.jimboss.deer.system.domain.SysLog;
import com.jimboss.deer.system.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName LogAspect
 * @Description AOP 记录用户操作日志
 * @Author jinyong
 * @DATE 2019/10/8 11:19
 * @Version 1.0
 **/
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private DeerProperties deerProperties;

    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.jimboss.deer.common.annotation.Log)")
    public void pointcut() {
        // do nothing
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 设置 IP 地址
        String ip = IPUtil.getIpAddr(request);
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (deerProperties.isOpenAopLog()) {
            // 保存日志
            String token = (String) SecurityUtils.getSubject().getPrincipal();
            String username = "";
            if (StringUtils.isNotBlank(token)) {
                username = JWTUtil.getUsername(token);
            }

            SysLog log = new SysLog();
            log.setUsername(username);
            log.setIp(ip);
            log.setTime(time);
            logService.saveLog(point, log);
        }
        return result;
    }
}
