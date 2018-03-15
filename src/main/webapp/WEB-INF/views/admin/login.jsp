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

  <title>大美登录</title>
  <meta name="keywords" content="大美">
  <meta name="description" content="大美单点系统登录">

  <%--<link rel="shortcut icon" href="/static/css/img/favicon.ico">--%>
  <link rel="stylesheet" href="${ctx}/static/css/lib.css">
  <link rel="stylesheet" href="${ctx}/static/static/styledm.css">
  <script src="${ctx}/static/js/lib.js"></script>
  <!-- 页面公用 -->
  <script>
    var ctx = '${ctx}';
  </script>
</head>

<body>
<div id="loginCont" class="wrapper">
  <div class="header-bg">
    <div class="header clearfix">
      <a href="javascript:;" class="logo pull-left">
        <img src="${ctx}/static/img/logo.png" alt="logo">
      </a>
      <!--/.logo-->
      <div class="log-in pull-right clearfix">
        <div class="item pull-left">
          <%--系统管理员 你好，欢迎使用大美新风认证中心系统平台！--%>
        </div>
        <%--<a class="item pull-left _last" href="javascript:void(0);">退出</a>--%>
      </div>
      <!--/.log-in-->
    </div>
    <!--/.header-->
  </div>
  <!--/.header-bg-->
  <div class="content content-login">
    <div class="login-block">
      <div class="login-tit">管理员登录</div>
      <form class="input-form" v-on:submit.prevent="submit" role="form">
        <div class="form-label clearfix">
          <input v-model="form.username" type="text" class="input-item" placeholder="请输入用户名" required>
        </div>
        <div class="form-label clearfix">
          <input v-model="form.password" type="password" class="input-item" placeholder="请输入密码" required>
        </div>
        <div class="form-label clearfix">
          <button class="btn" :disabled="submitting" type="submit">登录</button>
        </div>
      </form>
    </div>
  </div>
  <div class="footer">
    <div class="copyright"><span>Copyright © 2018北京大美信息技术有限公司  版权所有</span></div>
  </div>
</div>
<!--/.wrapper-->

<script src="${ctx}/static/js/utils.js"></script>
<script src="${ctx}/static/js/socket.io.js"></script>
<script src="${ctx}/static/js/containers/login/login.js"></script>
</body>

</html>
