package com.mmtap.modules.sys.interceptor;

import com.mmtap.modules.sys.service.LogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mmtap.com
 * @date 2019/1/11
 **/
@Slf4j
@Component
@AllArgsConstructor
public class LogHandlerInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> START_TIME_THREAD_LOCAL = new ThreadLocal<>();

    private LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        START_TIME_THREAD_LOCAL.set(System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("{}",handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Long startTime = START_TIME_THREAD_LOCAL.get();
        Long endTime = System.currentTimeMillis();
        START_TIME_THREAD_LOCAL.remove();
        Long executeUseMillisecond = endTime - startTime;
        log.debug("请求{}处理用时 {}ms", request.getRequestURI(), executeUseMillisecond);
        logService.saveLog(request, response, handler, ex, executeUseMillisecond);
    }
}
