package com.rocoinfo.config.shiro.realm;

import com.google.common.collect.Lists;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.config.shiro.token.CustomUserNameToken;
import com.rocoinfo.entity.admin.SystemUser;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.enumeration.Status;
import com.rocoinfo.service.admin.SystemUserAppService;
import com.rocoinfo.service.admin.SystemUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.utils.Encodes;

import java.util.List;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午5:33</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class SystemUserDbRealm extends AuthorizingRealm {

    @Autowired
    private SystemUserService userService;
    @Autowired
    private SystemUserAppService userAppService;

    private Logger logger = LoggerFactory.getLogger(SystemUserDbRealm.class);

    private static final String OR_OPERATOR = " or ";
    private static final String AND_OPERATOR = " and ";
    private static final String NOT_OPERATOR = "not ";

    /**
     * 授权
     *
     * @param principals 认证信息
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        try {
            SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
            ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();

            if (userService != null) {//不知道什么情况，回调一次后userService就为null了
                // 如果是后台用户 需要查询用户对应的权限
                SystemUser user = this.userService.getAllInfoById(shiroUser.getId());

                if (user != null) {
                    auth.addRoles(user.getRoleNameList());
                    auth.addStringPermissions(user.getPermissions());
                    shiroUser.setRoles(user.getRoleNameList());
                    shiroUser.setPermissions(Lists.newArrayList(user.getPermissions()));
                }
            }
            return auth;
        } catch (RuntimeException e) {
            logger.warn("授权时发生异常", e);
            throw e;
        }
    }

    /**
     * 认证
     *
     * @param authenticationToken 用户用于认证的凭据
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        SystemUser user = this.userService.getAllInfoByUsername(token.getUsername());
        // 查询不到此用户，抛出用户名密码错误的异常
        if (user == null) {
            throw new IncorrectCredentialsException();
        }
        // 用户状态为Lock，抛出用户锁定的异常
        if (Status.LOCK.equals(user.getStatus())) {
            throw new LockedAccountException();
        }
        // 查询用户所拥有的系统权限
        List<App> apps = this.userAppService.findAppsByUserId(user.getId());
        ShiroUser shiroUser = new ShiroUser(user.getId())
                .setUsername(user.getUsername())
                .setName(user.getName())
                .setRoles(user.getRoleNameList())
                .setPermissions(Lists.newArrayList(user.getPermissions()))
                .setApps(apps);
        byte[] salt = Encodes.decodeHex(user.getSalt());
        try {
            return new SimpleAuthenticationInfo(shiroUser, user.getPassword(), ByteSource.Util.bytes(salt), getName());
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * 支持or and not 关键词, 不支持and or混用 <br/>
     * 支持如下写法< shiro:hasPermission name="showcase:tree:create or showcase:tree:update or showcase:tree:delete">
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if (permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for (String orPermission : permissions) {
                if (isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if (permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for (String orPermission : permissions) {
                if (!isPermittedWithNotOperator(principals, orPermission)) {
                    return false;
                }
            }
            return true;
        } else {
            return isPermittedWithNotOperator(principals, permission);
        }
    }

    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if (permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        } else {
            return super.isPermitted(principals, permission);
        }
    }

    /**
     * 支持系统用户 非单点登录用户
     *
     * @param token token
     * @return 支持返回true 不支持返回false
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        if (token != null && token instanceof UsernamePasswordToken) {
            CustomUserNameToken customToken = (CustomUserNameToken) token;
            return CustomUserNameToken.Type.SYSTEM.equals(customToken.getType()) && !customToken.isSsoLogin();
        }
        return false;
    }
}
