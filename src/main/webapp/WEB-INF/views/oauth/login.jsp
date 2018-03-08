<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>登录页--大美认证中心系统</title>
  <meta name="keywords" content=" "/>
  <meta name="description" content=" "/>
  <meta http-equiv="x-ua-compatible" content="IE=Edge, chrome=1"/>
  <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no"/>
  <meta name="format-detection" content="telephone = no"/>
  <meta name="renderer" content="webkit">
  <link rel="stylesheet" href="${ctx}/static/css/lib.css">
  <link rel="stylesheet" href="${ctx}/static/static/css/style.css">
  <script src="${ctx}/static/js/lib.js"></script>
  <script>
    var ctx = '${ctx}';
  </script>
</head>

<body>
<div id="loginCont" style="height: 100%">
  <div class="wrap wrap-login">
    <header class="bar">
      <a href="javascript:void(0)" class="bar-item"><img src="${ctx}/static/img/logo-new.png"></a>
      <div class="bar-item">
        <p><i class="icon icon-tel">icon</i>全国统一免费咨询热线</p>
        <p class="bar-tel">400-825-8833</p>
      </div>
    </header>
    <div class="content content-login">
      <div class="item-content">
        <div class="item-inner"><img src="${ctx}/static/img/img.png"></div>
        <div class="item-inner clearfix">
          <div class="login-title">
            <h1>大美认证系统</h1>
            <p>Me:Dilly Authentication system</p>
          </div>
          <form v-on:submit.prevent="submit" class="login-form">
            <div class="login-nav">
              <div @click="switchTab('qrcode')" class="current" id="qrcode">二维码登录</div>
              <div @click="switchTab('login')" id="form">用户名登录</div>
            </div>

            <div class="login-content" v-if="nameLogin==0">
              <div class="qr-code">
                <img width="267px" height="267px" :src="qrcode"/>
              </div>
            </div>

            <div class="login-content" v-if="nameLogin==1">
              <label class="login-row">
                <div class="item-label"><i class="icon icon-user1">icon</i></div>
                <div class="item-input">
                  <input id="orgcodeipt" v-model="form.username" type="text" placeholder="请输入员工工号" required>
                </div>
              </label>
              <label class="login-row">
                <div class="item-label"><i class="icon icon-password1"></i></div>
                <div class="item-input">
                  <input id="loginpwdipt" v-model="form.password" type="password" placeholder="请输入密码" required>
                </div>
              </label>
              <button :disabled="submitting" type="submit" class="button button-login">登录</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <footer>
      <div class="copyright">Copyright(c) 2016 damei.cn All rights reserved 版权所有：大美装饰设计有限公司 — 京ICP备17009877号-1</div>
    </footer>
  </div>
</div>
<script>
  var appid = '${appid}';
  var redirectUrl = '${redirect_url}';
  var state = '${state}';
</script>
<script src="${ctx}/static/js/utils.js"></script>
<script src="${ctx}/static/js/socket.io.js"></script>
<script src="${ctx}/static/js/containers/oauth/login.js"></script>
</body>
</html>