# 大美认证中心项目

## 部署说明：

1.login.js中websocket长连接的地址注意修改
2.logback.xml中发送到flume的日志是否开启
3.pom.xml中注意，javax.servlet-api和tomcat-embed-jasper两个依赖的scope必须是provided：

```
    <dependency>
      <groupId>org.apache.tomcat.embed</groupId>
      <artifactId>tomcat-embed-jasper</artifactId>
      <scope>provided</scope>
    </dependency>

    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <scope>provided</scope>
    </dependency>
```

## 接口文档：

### 1.1获取code地址（第三方应用应重定向到此地址）

地址：/oauth/code?appid=appid&redirect_url=redirect_url&state=state
如：/oauth/code?appid=138a41fd523dbe7238&redirect_url=http://www.rocoinfo.com&state=state

参数：
appid:应用id
redirect_url:回调地址
state:原样返回的地址

回调参数（回调redirect_url地址）：
如：http://www.rocoinfo.com/?code=8d4abca55e9234aca477c104065ad7b0&state=state

code:应用发放的code
state:原样返回的state


### 1.2第三方应用使用code获取token及用户信息接口（POST请求）
地址：/oauth/token

参数：
appid：应用id
secret:应用秘钥
code:上一步发放的code
scope: true:发放权限信息 false：不发放权限信息

返回值：
```
{
  "code": "1",
  "message": "success!",
  "data": {
    "access_token": "b40c549de3843e7c96555d1a37950555",
    "scope": [
      "test:test",
      "test1:test1"
    ],
    "userinfo": {
      "name": "张三",
      "mobile": "13333333333",
      "id": 1,
      "email": "fsafdsa@rocoinfo.com",
      "username": "test"
    }
  },
  "success": true
}
```
userinfo:用户基本信息
scope:用户权限信息
access_token:access_token


### 1.3用户登出接口（POST请求）
地址：/oauth/logout

参数：
appid    ：第三方应用appid
secret   ：第三方应用secret
username ：用户名

返回结果：
```
{
  "code": "1",
  "message": "success!",
  "data": null,
  "success": true
}
```

### 1.4第三方应用获取全局唯一的AccessToken接口（POST请求）
地址： /oauth/appToken

参数：
appid  ：第三方应用appid
secret ：第三方应用secret

返回结果：
```
{
    "code": "1",
    "message": "success!",
    "data": {
        "access_token": "8202893077dd3f3185a320a44bf806bb"
    },
    "success": true
}
```

### 1.5 第三方应用根据jobNum（用户工号）获取对应openId的接口（POST）
地址：/oauth/api/wechatuser/findBindsByJobNums

参数：
appid        ： 应用appid
access_token ：全局唯一的accessToken
job_nums     ：用户jobNums（工号列表）

返回结果：
```
{
    "code": "1",
    "message": "success!",
    "data": {
        "userInfo": {
            "00000117": [
                "oYq6cwGl0bA1zEwvcIMNnA46bHAI"
            ],
            "ZZ000018": [
                "oYq6cwLJPhiFC68O7hccuQJfy5BE"
            ]
        }
    },
    "success": true
}
```

### 1.6 修改密码

* 接口地址: /oauth/password
* 请求方式：get
* 请求参数：jobNum（员工号，必填），redirectUrl（修改成功后的回调地址，必填）

eg:
```
http://localhost:8080/oauth/password?jobNum=bj000001&redirectUrl=http://www.baidu.com
```
