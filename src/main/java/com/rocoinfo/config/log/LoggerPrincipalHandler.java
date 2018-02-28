package com.rocoinfo.config.log;

import com.rocoinfo.aop.logger.entity.Principal;
import com.rocoinfo.aop.logger.principal.PrincipalHandler;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.utils.WebUtils;
import org.springframework.stereotype.Component;

/**
 * <dl>
 * <dd>Description: 功能描述</dd>
 * <dd>Company: 大诚若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/29 13:56</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Component
public class LoggerPrincipalHandler implements PrincipalHandler {

    @Override
    public Principal getPrincipal() {
        ShiroUser user = WebUtils.getLoggedUser();
        if (user != null) {
            return new Principal(user.getId(), user.getUsername());
        }
        return new Principal();
    }
}
