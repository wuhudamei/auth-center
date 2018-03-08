package cn.damei.rest.login;

import cn.damei.common.BaseController;
import cn.damei.config.shiro.token.CustomUserNameToken;
import cn.damei.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/login")
public class LoginController extends BaseController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public Object login(@RequestParam String username, @RequestParam String password) {
        return this.loginService.login(username, password, CustomUserNameToken.Type.SYSTEM, false);
    }
}
