package com.mmtap.modules.sys.config.security;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mmtap.com
 * @date 2019/1/17
 **/
public class RequestUtils {
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
