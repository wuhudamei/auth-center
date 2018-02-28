package com.rocoinfo.rest.login;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.service.login.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午6:36</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/logout")
public class LogoutController extends BaseController {

    @Autowired
    private LogoutService logoutService;

    /**
     * 登录
     *
     * @return
     */
    @GetMapping
    @Logger(module = Type.LOGOUT)
    public Object logout() {
        this.logoutService.logout();
        return StatusDto.buildSuccess();
    }
}
