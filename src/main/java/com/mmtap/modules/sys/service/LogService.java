package com.mmtap.modules.sys.service;

import com.mmtap.modules.sys.model.Log;
import com.mmtap.modules.sys.repository.LogRepository;
import com.mmtap.modules.sys.utils.HttpServletRequestUtils;
import lombok.AllArgsConstructor;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mmtap.com
 * @date 2019/1/11
 **/
@AllArgsConstructor
@Service
public class LogService {

    private LogRepository logRepository;

    static ExecutorService service = Executors.newFixedThreadPool(3);

    public void saveLog(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex, Long executeUseMillisecond) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Log log = new Log();
            log.setTitle("");
            log.setReqUri(request.getRequestURI());
            log.setReqMethod(request.getMethod());
            log.setController(handlerMethod.getBeanType().getName());
            log.setMethodName(handlerMethod.getMethod().getName());
            log.setUsername(currentUsername);
            log.setAddressIp(HttpServletRequestUtils.getIpAddr(request));
            //获取浏览器信息
            String ua = request.getHeader("User-Agent");
            //转成UserAgent对象
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            log.setBrowserName(userAgent.getBrowser().getName());
            log.setDriverName(userAgent.getOperatingSystem().getName());
            if (Objects.nonNull(ex)) {
                log.setExceptionInfo(ex.getClass() + ex.getMessage());
            }
            log.setExecuteUseMillisecond(executeUseMillisecond);
            service.execute(() -> logRepository.save(log));
        }
    }
}
