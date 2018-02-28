<%--
  这个页面是点击微信菜单绑定账号时使用，和扫码登录绑定逻辑不同
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="renderer" content="webkit">

  <title>美得你账号绑定</title>
  <meta name="keywords" content="美的你认证中心绑定页面">
  <meta name="description" content="美得你认证中心绑定页面">

  <%--<link rel="shortcut icon" href="/static/css/img/favicon.ico">--%>
  <link rel="stylesheet" href="${ctx}/static/css/lib.css">
  <link rel="stylesheet" href="${ctx}/static/static/css/style.css">
  <script src="${ctx}/static/js/lib.js"></script>
  <!-- 页面公用 -->
  <script>
    var ctx = '${ctx}';
  </script>
</head>

<body class="gray-bg">
<div id="loginCont" style="height: 100%;">
  <div class="wrap wrap-login-wap">
    <header>
      <div class="logo-wap"></div>
    </header>
    <div class="content content-login-wap">
      <div class="item-content">
        <div class="item-inner clearfix">
          <form v-on:submit.prevent="submit" class="login-form" action="" method="POST">
            <div class="login-nav">
              <div class="current">
                <h1>美得你认证系统</h1>
                <p>Me:Dilly Authentication system</p>
              </div>
            </div>
            <div class="login-content">
              <label class="login-row">
                <div class="item-label"><i class="icon icon-user1">icon</i></div>
                <div class="item-input">
                  <input type="text" v-model="form.username" placeholder="请输入员工工号" required>
                </div>
              </label>
              <label class="login-row">
                <div class="item-label"><i class="icon icon-password1"></i></div>
                <div class="item-input">
                  <input type="password" v-model="form.password" placeholder="请输入密码" required>
                </div>
              </label>
              <button :disabled="submitting" type="submit" class="button button-login">登录</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <footer>
      <div class="copyright">
        <p>Copyright(c) 2016 mdni.cn All rights reserved </p>
        <p>版权所有：北京美得你装饰设计有限公司 — 京ICP备17009877号-1 </p>
      </div>
    </footer>
  </div>
</div>
<script>
  var openid = '${openid}';
  var uuid = '${uuid}';
</script>
<script src="${ctx}/static/js/containers/login/scan/bind.js"></script>
</body>
</html>
