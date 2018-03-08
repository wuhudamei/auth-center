package cn.damei.config.shiro.realm;

import cn.damei.config.shiro.ShiroUser;
import cn.damei.config.shiro.token.CustomUserNameToken;
import cn.damei.service.auth.UserService;
import cn.damei.entity.auth.User;
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


public class AuthUserSSORealm extends AuthorizingRealm {


    @Autowired
    private UserService userService;


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



    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


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
