package cn.damei.rest.oauth2.qrcode;

import cn.damei.common.PropertyHolder;
import cn.damei.service.auth.AppService;
import cn.damei.wx.WxHttpHandler;
import cn.damei.common.BaseController;
import cn.damei.entity.auth.App;
import cn.damei.utils.QrcodeUtils;
import cn.damei.utils.WebUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


@RestController
@RequestMapping(value = "/oauth/load/qrcode")
public class QrcodeController extends BaseController {

    @Autowired
    private AppService appService;


    @RequestMapping(method = RequestMethod.GET)
    public Object get(String uuid, HttpServletResponse response) {
        OutputStream os = null;
        // 输出为jpeg图片
        response.setContentType("image/jpeg");
        // 禁止浏览器缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setIntHeader("Expires", -1);
        try {
            //获取第三方应用的appid,进而查找对应的微信appid,没有获取到直接返回null
            String appid = (String) WebUtils.getSession().getAttribute("appid");
            if (StringUtils.isEmpty(appid)) {
                return null;
            }
            App app = appService.getByAppid(appid);
            if (app == null || StringUtils.isEmpty(app.getWxAppid())) {
                return null;
            }

            os = response.getOutputStream();
            String redirectUrl = PropertyHolder.getBaseurl() + "/oauth/qrcode?appid=" + appid;
            String content = WxHttpHandler.buildUrl(app.getWxAppid(), redirectUrl, uuid, WxHttpHandler.Type.BASE);
            QrcodeUtils.generateQRCode(content, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
        return null;
    }
}
