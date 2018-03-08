package cn.damei.service.login;

import cn.damei.config.shiro.ShiroUser;
import cn.damei.config.shiro.token.CustomUserNameToken;
import cn.damei.dto.StatusDto;
import cn.damei.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

    @Autowired
    private LogoutService logoutService;

    private Logger logger = LoggerFactory.getLogger(LoginService.class);


    public StatusDto login(String username, String password, CustomUserNameToken.Type type, boolean ssoLogin) {
        try {
            this.login(new CustomUserNameToken(username, password,type,ssoLogin));
            //将当前登录用户信息返回
            ShiroUser loginUser = WebUtils.getLoggedUser();
            return StatusDto.buildSuccess("登录成功", loginUser);
        } catch (AuthenticationException e) {
            return StatusDto.buildFailure(getErrorResponseEntity(e));
        }
    }

    public void login(AuthenticationToken token) throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        //如果已登录，先退出
        if (subject.getPrincipal() != null) {
            this.logoutService.logout();
        }
        //登录
        subject.login(token);
    }


    private String getErrorResponseEntity(AuthenticationException e) {
        final String loginFail = "[账户/密码]不正确或账户被锁定";

        if (e instanceof IncorrectCredentialsException) {
            return "账户或密码不正确";
        }
        if (e instanceof ExpiredCredentialsException) {
            return "密码已过期";
        }
        if (e instanceof CredentialsException) {
            return "密码验证失败";
        }
        if (e instanceof UnknownAccountException) {
            return "账户或密码不正确";
        }
        if (e instanceof LockedAccountException) {
            return "账户已被锁定";
        }
        if (e instanceof DisabledAccountException) {
            return "账户已被禁用";
        }
        if (e instanceof ExcessiveAttemptsException) {
            return "尝试次数太多";
        }
        if (e instanceof AccountException) {
            String error = e.getMessage();
            if (StringUtils.isBlank(error)) {
                error = loginFail;
            }
            return error;
        }
        logger.debug(loginFail, e);
        return loginFail;
    }
}
