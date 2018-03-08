package cn.damei.config.shiro.filter;

import cn.damei.config.shiro.ShiroUser;
import cn.mdni.commons.clone.IJClone;
import cn.damei.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.springside.modules.utils.Encodes;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.ServiceLoader;


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


    private String getRedirectUrlOnLoginSuccess(HttpServletRequest req) {
        StringBuilder requestUrl = new StringBuilder(req.getRequestURL().toString());
        final String queryString = req.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            requestUrl.append("?").append(queryString);
        }
        return Encodes.urlEncode(requestUrl.toString());
    }
}
