package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用实体类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 上午10:50</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class App extends IdEntity implements Serializable {

    private static final long serialVersionUID = 6651273048591334846L;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空！")
    @Length(max = 50, message = "应用名称不能超过50字符！")
    private String name;

    /**
     * 应用id 系统生成
     */
    private String appid;

    /**
     * 应用秘钥 系统生成
     */
    private String secret;

    /**
     * 应用token 用户输入
     */
    private String token;

    /**
     * 认证中心向应用推消息的url
     */
    private String url;

    /**
     * 微信的appid  用于微信授权获取用户openid
     */
    private String wxAppid;

    /**
     * 微信公众号的秘钥
     */
    private String wxSecret;

    /**
     * 是否开启消息推送，如果开启则表示允许认证中心向应用推送相应的消息
     */
    private Byte pushFlag;

    /**
     * 应用状态，启用、禁用
     */
    private Status status;

    /**
     * 逻辑删除标志
     */
    private Byte deleted;

    /**
     * 是否被选中，页面回显用
     */
    @Transient
    private Boolean checked;

    /**
     * 应用角色列表
     */
    @Transient
    private List<Role> roles;

    public App() {
    }

    public App(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWxAppid() {
        return wxAppid;
    }

    public void setWxAppid(String wxAppid) {
        this.wxAppid = wxAppid;
    }

    public String getWxSecret() {
        return wxSecret;
    }

    public void setWxSecret(String wxSecret) {
        this.wxSecret = wxSecret;
    }

    public Byte getPushFlag() {
        return pushFlag;
    }

    public void setPushFlag(Byte pushFlag) {
        this.pushFlag = pushFlag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * 应用状态枚举类
     */
    public enum Status {
        OPENED, LOCK
    }
}