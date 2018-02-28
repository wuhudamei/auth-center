package com.rocoinfo.rest.login;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.config.shiro.token.CustomUserNameToken;
import com.rocoinfo.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午5:25</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/login")
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    @Logger(module = Type.LOGIN, excludeParams = {"password"})
    public Object login(@RequestParam String username, @RequestParam String password) {
        return this.loginService.login(username, password, CustomUserNameToken.Type.SYSTEM, false);
    }
}
