package cn.damei.config.shiro.realm;

import cn.damei.config.shiro.ShiroUser;
import cn.damei.config.shiro.token.CustomUserNameToken;
import cn.damei.service.auth.UserService;
import cn.damei.entity.auth.User;
import cn.damei.utils.des.Encodes;
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


public class AuthUserPasswordRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(getClass());


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
