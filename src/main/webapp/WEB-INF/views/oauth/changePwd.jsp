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

  <title>修改密码</title>
  <meta name="keywords" content="美的你认证中心绑定页面">
  <meta name="description" content="大美认证中心绑定页面">

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
      <div class="login-tit">修改密码</div>
      <form class="input-form" v-on:submit.prevent="submit" role="form">
        <div class="form-label clearfix">
          <input type="password" id="yPassword" v-model="form.originPwd" class="input-item" placeholder="请输入原密码">
        </div>
        <div class="form-label clearfix">
          <input type="password" id="nPassword" v-model="form.newPwd" class="input-item" placeholder="请输入新密码">
        </div>
        <div class="form-label clearfix">
          <input type="password" id="anPassword" v-model="form.confirmPwd" class="input-item" placeholder="请再次输入新密码">
        </div>
        <div class="form-label clearfix">
          <button class="btn" :disabled="submitting" type="submit">修 改</button>
        </div>
      </form>
    </div>
  </div>
  <div class="footer">
    <div class="copyright"><span>Copyright © 2018北京大美信息技术有限公司  版权所有</span></div>
  </div>
</div>
<!--/.wrapper-->
</body>


<script src="${ctx}/static/js/containers/login/changePwd.js"></script>
</body>
</html>
