package cn.damei.wx;

import cn.damei.utils.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;


public class WxHttpHandler {

    private static OkHttpClient httpClient;

    static {
        httpClient = (new OkHttpClient()).newBuilder().connectTimeout(10L, TimeUnit.SECONDS).writeTimeout(10L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).build();
    }


    public static String getOpenId(String appId, String secret, String code) {

        if (StringUtils.isAnyEmpty(appId, secret, code)) {
            return null;
        }
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", new Object[]{appId, secret, code});

        Request request = (new Request.Builder()).url(url).get().build();
        String result = execute(request);

        return JsonUtils.fromJsonAsMap(result, String.class, String.class).get("openid");
    }


    public static String buildUrl(String appid, String url, String state, Type type) {
        if (StringUtils.isNoneBlank(appid, url) && type != null) {
            return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect", appid, URLEncode(url), type.getValue(), state);
        } else {
            throw new IllegalArgumentException("url或type为不能为空");
        }
    }


    private static String URLEncode(String url) {
        String res = null;

        try {
            res = URLEncoder.encode(url, "UTF-8");
            return res;
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("url encoding failed!");
        }
    }

    public static enum Type {
        BASE("snsapi_base"),
        INFO("snsapi_userinfo");

        private String value;

        private Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }



    private static String execute(Request request) {
        try {
            Response e = httpClient.newCall(request).execute();
            if (!e.isSuccessful()) {
                throw new RuntimeException("Unexpected code" + e);
            } else {
                return e.body().string();
            }
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }
}
