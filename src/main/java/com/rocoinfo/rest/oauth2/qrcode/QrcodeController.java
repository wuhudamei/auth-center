package com.rocoinfo.rest.oauth2.qrcode;

import com.rocoinfo.common.BaseController;
import com.rocoinfo.common.PropertyHolder;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.service.auth.AppService;
import com.rocoinfo.utils.QrcodeUtils;
import com.rocoinfo.utils.WebUtils;
import com.rocoinfo.wx.WxHttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * <dl>
 * <dd>Description:</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/5 下午1:22</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/oauth/load/qrcode")
public class QrcodeController extends BaseController {

    @Autowired
    private AppService appService;

    /**
     * 生成二维码，不同于以上的方法，以上两个方法是通过微信的api生成二维码，而本方法是通过ZXing生成的二维码 微信扫码登录时通过此方法生成的二维码
     *
     * @param uuid 前台传递的uuid，用来以后通过此uuid查找与服务器建立链接的SocketIOClient
     * @return
     */
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
