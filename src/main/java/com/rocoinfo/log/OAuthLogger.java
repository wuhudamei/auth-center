package com.rocoinfo.log;


import com.rocoinfo.aop.logger.LoggerType;

import java.io.Serializable;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/7/4 下午2:00</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class OAuthLogger implements Serializable {

    private static final long serialVersionUID = -6928765392911900556L;

    public OAuthLogger() {
    }

    public OAuthLogger(String id) {
        this.id = id;
    }

    /**
     * id
     */
    private String id;

    /**
     * 日志类型
     */
    private LoggerType type;

    /**
     * 模块
     */
    private String module;

    /**
     * 请求ip
     */
    private String ip;

    /**
     * 用户名
     */
    private String username;

    /**
     * 应用id
     */
    private Long appid;

    /**
     * 应用名称
     */
    private String appname;

    /**
     * 请求时间戳
     */
    private Long timestamp;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 当前服务器
     */
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

    public LoggerType getType() {
        return type;
    }

    public OAuthLogger setType(LoggerType type) {
        this.type = type;
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
