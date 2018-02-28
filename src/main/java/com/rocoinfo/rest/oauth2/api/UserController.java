package com.rocoinfo.rest.oauth2.api;

        import com.rocoinfo.common.oauth2.BaseOAuthController;
        import com.rocoinfo.common.oauth2.OAuthError;
        import com.rocoinfo.common.oauth2.OAuthResponse;
        import com.rocoinfo.entity.auth.App;
        import com.rocoinfo.entity.auth.Role;
        import com.rocoinfo.entity.auth.User;
        import com.rocoinfo.service.auth.RoleService;
        import com.rocoinfo.service.auth.UserService;
        import org.apache.commons.lang3.StringUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/7/31 上午10:23</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController("oauthUserController")
@RequestMapping("/oauth/user")
public class UserController extends BaseOAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/app")
    public Object findUsersFromAppid(@RequestParam String appid,
                                     @RequestParam String accessToken,
                                     @RequestParam(required = false) String roleName) {
        App app = this.getAppByAppid(appid);
        if (app == null) {
            return OAuthResponse.buildError(OAuthError.INVALID_APPID);
        }

        if (StringUtils.isBlank(accessToken) || !this.oAuthService.validateAppToken(accessToken, appid)) {
            return OAuthResponse.buildError(OAuthError.INVALID_APP_ACCESS_TOKEN);
        }

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("appId",app.getId());


        //通过角色名称和appid，确定roleId
        if(StringUtils.isNotEmpty(roleName)) {
            Role role = roleService.getRoleByNameAndAppId(roleName, app.getId());
            if (role == null) {
                return OAuthResponse.buildError(OAuthError.INVALID_ROLE_INAPP);
            }
            params.put("roleId",role.getId());
        }

        List<User> users = this.userService.findUsersFromAppid(params);
        return OAuthResponse.buildSuccess().put("users", users);
    }
}
