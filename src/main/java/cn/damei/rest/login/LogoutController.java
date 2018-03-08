package cn.damei.rest.login;

import cn.damei.dto.StatusDto;
import cn.damei.common.BaseController;
import cn.damei.service.login.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/logout")
public class LogoutController extends BaseController {

    @Autowired
    private LogoutService logoutService;

    @GetMapping
    public Object logout() {
        this.logoutService.logout();
        return StatusDto.buildSuccess();
    }
}
