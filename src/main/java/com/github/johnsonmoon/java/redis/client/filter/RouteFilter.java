package com.github.johnsonmoon.java.redis.client.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by xuyh at 2019/5/9 11:59.
 */
@WebFilter
public class RouteFilter extends HttpFilter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getServletPath().equals("/")) {
            response.sendRedirect("/redis-cli/index.html");
            return;
        }
        if (request.getServletPath().equals("/swagger")) {
            response.sendRedirect("/redis-cli/swagger-ui.html");
            return;
        }
        chain.doFilter(req, res);
    }
}
