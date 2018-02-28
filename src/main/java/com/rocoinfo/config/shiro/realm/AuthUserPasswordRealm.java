package com.rocoinfo.config.shiro.realm;

import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.config.shiro.token.CustomUserNameToken;
import com.rocoinfo.entity.auth.User;
import com.rocoinfo.service.auth.UserService;
import com.rocoinfo.utils.des.Encodes;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <dl>
 * <dd>Description: 远程用户用户名密码登录使用的realm</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/6 11:15</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class AuthUserPasswordRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 因远程用户不会用到本系统的授权回调 所以不必重写该方法
     *
     * @param principalCollection
     * @return always null
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomUserNameToken token = (CustomUserNameToken) authenticationToken;
        User user = userService.getByUsername(token.getUsername());
        ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(), user.getName(),
                user.getJobNum(), null, null, user.getStoreCode());
        byte[] salt = Encodes.decodeHex(user.getSalt());
        try {
            return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(salt), getName());
        } catch (RuntimeException e) {
            logger.error("认证回调失败！", e);
            throw e;
        }
    }

    /**
     * 该realm是否支持当前用户
     *
     * @param token 用户token
     * @return 支持返回true 不支持返回false
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof CustomUserNameToken) {
            CustomUserNameToken customToken = (CustomUserNameToken) token;
            return CustomUserNameToken.Type.AUTH_USER.equals(customToken.getType()) && !customToken.isSsoLogin();
        }
        return false;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
