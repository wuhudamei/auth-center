package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;


public class App extends IdEntity implements Serializable {

    private static final long serialVersionUID = 6651273048591334846L;


    @NotEmpty(message = "应用名称不能为空！")
    @Length(max = 50, message = "应用名称不能超过50字符！")
    private String name;


    private String appid;


    private String secret;


    private String token;


    private String url;


    private String wxAppid;


    private String wxSecret;


    private Byte pushFlag;


    private Status status;


    private Byte deleted;


    @Transient
    private Boolean checked;


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


    public enum Status {
        OPENED, LOCK
    }
}