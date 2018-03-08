package cn.damei.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.net.ssl.*;
import javax.servlet.ServletContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;



@Component
@Lazy(false)
public class PropertyHolder implements ServletContextAware, ApplicationContextAware {

    public static ApplicationContext appCtx;
    private static ServletContext servletContext;


    private static int socketIOPort;


    private static String baseurl;

    static {
        disableSslVerification();
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext ctx) {
        PropertyHolder.servletContext = ctx;
        ctx.setAttribute("ctx", ctx.getContextPath());
    }


    public static int getSocketIOPort() {
        return PropertyHolder.socketIOPort;
    }

    @Value("${socket.io.port}")
    public void setSocketIOPort(int socketIOPort) {
        PropertyHolder.socketIOPort = socketIOPort;
    }

    @Value("${base.url}")
    public void setBaseUrl(String baseUrl) {
        PropertyHolder.baseurl = baseUrl;
    }

    public static String getBaseurl(){
        return PropertyHolder.baseurl;
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }
}