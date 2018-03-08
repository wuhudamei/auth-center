package cn.damei.rest.oauth2.api;

import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.dto.StatusDto;
import cn.damei.service.auth.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/oauth/password")
public class ChangePasswordController extends BaseOAuthController {

    private static final String CHANGE_PASSWORD_PAGE = "/oauth/changePwd";

    @Autowired
    private UserService userService;


    @GetMapping
    public Object redirectPage(@RequestParam String jobNum,
                               @RequestParam String redirectUrl,
                               Model model) {

        ModelAndView view = new ModelAndView(ERROR_PAGE);

        if (StringUtils.isAnyBlank(jobNum, redirectUrl)) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, "员工号或回调url不能为空");
            return view;
        }

        view.setViewName(this.buildRedirectUrl(jobNum, redirectUrl));
        return view;
    }

    private String buildRedirectUrl(String jobNum, String redirectUrl) {
        return "redirect:" + CHANGE_PASSWORD_PAGE + "?jobNum=" + jobNum + "&redirectUrl=" + redirectUrl;
    }

    @PostMapping
    public Object changePassword(@RequestParam String jobNum,
                                 @RequestParam String originPwd,
                                 @RequestParam String newPwd,
                                 @RequestParam String confirmPwd) {
        if (StringUtils.isAnyBlank(jobNum, originPwd, newPwd, confirmPwd)) {
            return StatusDto.buildFailure("参数不能为空");
        }

        if (!newPwd.equals(confirmPwd)) {
            return StatusDto.buildFailure("新密码和确认密码不相同");
        }

        StatusDto res = this.userService.changePassword(jobNum, originPwd, newPwd);
        return res;
    }
}
