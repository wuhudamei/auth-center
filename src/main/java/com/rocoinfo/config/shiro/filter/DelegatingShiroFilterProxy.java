package com.rocoinfo.config.shiro.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <dl>
 * <dd>Description:
 * ShiroFilter的代理对象，继承DelegatingFilterProxy表示可以将改bean的声明周期交给spring管理
 * 功能:解决访问静态资源时，应该不经过shiroFilter,访问静态资源，应该不需要与redis交互
 * </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/27 下午2:41</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class DelegatingShiroFilterProxy extends DelegatingFilterProxy {

    public DelegatingShiroFilterProxy(String targetName) {
        super(targetName);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        String ctx = StringUtils.trimToEmpty(req.getContextPath());
        String path = req.getRequestURI().substring(ctx.length());
        if (StringUtils.isNotEmpty(path) && (path.startsWith("/static/"))
                || path.startsWith("/api/generatejs") || path.contains("/favicon.ico")) {
            filterChain.doFilter(request, response);
        } else {
            super.doFilter(request, response, filterChain);
        }

    }
}
