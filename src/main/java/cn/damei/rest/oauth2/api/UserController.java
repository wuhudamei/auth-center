package cn.damei.rest.oauth2.api;

        import cn.damei.common.oauth2.BaseOAuthController;
        import cn.damei.common.oauth2.OAuthError;
        import cn.damei.entity.auth.Role;
        import cn.damei.entity.auth.User;
        import cn.damei.service.auth.RoleService;
        import cn.damei.service.auth.UserService;
        import cn.damei.common.oauth2.OAuthResponse;
        import cn.damei.entity.auth.App;
        import org.apache.commons.lang3.StringUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;

        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;


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
