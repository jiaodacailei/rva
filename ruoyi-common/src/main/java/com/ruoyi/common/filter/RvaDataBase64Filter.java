package com.ruoyi.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Repeatable 过滤器
 *
 * @author ruoyi
 */
public class RvaDataBase64Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
            RvaBase64ServletRequestWrapper base64ServletRequestWrapper = new RvaBase64ServletRequestWrapper((HttpServletRequest) request);
            chain.doFilter(base64ServletRequestWrapper, response);
    }

    @Override
    public void destroy() {

    }
}
