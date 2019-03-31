package com.mmtap.modules.sys.config.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mmtap.com
 * @date 2019/1/17
 **/
public class ResponseUtils {

    /**
     */
    public static void print(HttpServletResponse response, Object... object) throws IOException, ServletException {
        PrintWriter writer = utf8AndJson(response).getWriter();
        for (Object o : object) {
            writer.print(o);
        }
        writer.flush();
        writer.close();
    }

    private static HttpServletResponse utf8AndJson(HttpServletResponse response) {
        response.setContentType("text/json;charset=utf-8");
        return response;
    }
}
