package com.rocoinfo.config.shiro.realm;

import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.config.shiro.token.CustomUserNameToken;
import com.rocoinfo.entity.auth.User;
import com.rocoinfo.service.auth.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <dl>
 * <dd>Description: 远程用户 单点登录realm</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/6 10:45</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class AuthUserSSORealm extends AuthorizingRealm {

    /**
     * 远程用户Service
     */
    @Autowired
    private UserService userService;

    /**
     * 自定义校验规则
     *
     * @param credentialsMatcher 校验器
     */
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        super.setCredentialsMatcher((token, info) -> {
            if (token instanceof CustomUserNameToken) {
                CustomUserNameToken customToken = (CustomUserNameToken) token;
                return StringUtils.isNotEmpty(customToken.getUsername()) && CustomUserNameToken.Type.AUTH_USER.equals(customToken.getType()) && customToken.isSsoLogin() && info != null;
            }
            return false;
        });
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomUserNameToken token = (CustomUserNameToken) authenticationToken;
        //查询用户信息
        User user = userService.getByUsername(token.getUsername());
        ShiroUser shiroUser = new ShiroUser(user.getId(), user.getUsername(), user.getName(),
                user.getJobNum(), null, null, user.getStoreCode());
        return new SimpleAuthenticationInfo(shiroUser, null, getName());
    }


    /**
     * 授权回调函数，进行鉴权但缓存中无用户权限信息时候调用(远程用户不能登录本系统，所以无需实现)
     *
     * @param principalCollection 当前用户凭证
     * @return 返回用户权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 支持 远程用户 单点登录用户
     *
     * @param token 用户token
     * @return 支持返回true 不支持返回false
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof CustomUserNameToken) {
            CustomUserNameToken customToken = (CustomUserNameToken) token;
            return CustomUserNameToken.Type.AUTH_USER.equals(customToken.getType()) && customToken.isSsoLogin();
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
