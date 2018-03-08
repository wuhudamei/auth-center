package cn.damei.log;



import java.io.Serializable;


public class OAuthLogger implements Serializable {

    private static final long serialVersionUID = -6928765392911900556L;

    public OAuthLogger() {
    }

    public OAuthLogger(String id) {
        this.id = id;
    }


    private String id;



    private String module;


    private String ip;


    private String username;


    private Long appid;


    private String appname;


    private Long timestamp;


    private String browser;


    private Object result;


    private String server;

    public String getId() {
        return id;
    }

    public OAuthLogger setId(String id) {
        this.id = id;
        return this;
    }

    public String getModule() {
        return module;
    }

    public OAuthLogger setModule(String module) {
        this.module = module;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public OAuthLogger setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public OAuthLogger setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getAppid() {
        return appid;
    }

    public OAuthLogger setAppid(Long appid) {
        this.appid = appid;
        return this;
    }

    public String getAppname() {
        return appname;
    }

    public OAuthLogger setAppname(String appname) {
        this.appname = appname;
        return this;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public OAuthLogger setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getBrowser() {
        return browser;
    }

    public OAuthLogger setBrowser(String browser) {
        this.browser = browser;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public OAuthLogger setResult(Object result) {
        this.result = result;
        return this;
    }


    public String getServer() {
        return server;
    }

    public OAuthLogger setServer(String server) {
        this.server = server;
        return this;
    }
}
