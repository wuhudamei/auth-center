package cn.damei.config.db;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;


@WebServlet(urlPatterns = "/druid/*", initParams = {
        @WebInitParam(name = "allow", value = "", description = "白名单，如果不配置或value为空，则允许所有"),
        @WebInitParam(name = "deny", value = "", description = "黑名单"),
        @WebInitParam(name = "loginUsername", value = "druid", description = "用户名"),
        @WebInitParam(name = "loginPassword", value = "admin", description = "密码"),
        @WebInitParam(name = "resetEnable", value = "false", description = "禁用HTML页面上的Reset All功能")
})
public class DruidStatViewServlet extends StatViewServlet {
}
