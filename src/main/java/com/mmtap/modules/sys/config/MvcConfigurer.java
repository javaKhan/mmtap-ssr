package com.mmtap.modules.sys.config;

import com.mmtap.modules.sys.interceptor.LogHandlerInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author mmtap.com
 * @date 2019/1/12
 **/
@Configuration
@AllArgsConstructor
public class MvcConfigurer implements WebMvcConfigurer {

    private LogHandlerInterceptor logHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logHandlerInterceptor).addPathPatterns("/api/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/page/login.html");
        registry.addViewController("/").setViewName("/page/index.html");
    }
}
