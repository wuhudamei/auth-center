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
  <link rel="stylesheet" href="${ctx}/static/css/style.css">
  <script src="${ctx}/static/js/lib.js"></script>
  <!-- 页面公用 -->
  <script>
    var ctx = '${ctx}';
  </script>
</head>

<body class="gray-bg">
<div id=loginCont class="middle-box text-center loginscreen  animated fadeInDown">
  <div>
    <div>
      <h1 class="logo-name"><img src="/static/img/logo.png"></h1>
    </div>
    <h3>修改密码</h3>

    <form v-on:submit.prevent="submit" role="form">
      <div class="form-group">
        <input
          v-model="form.originPwd"
          type="password" class="form-control" placeholder="请输入原密码" required>
      </div>
      <div class="form-group">
        <input
          v-model="form.newPwd"
          type="password" class="form-control" placeholder="请输入新密码" required>
      </div>
      <div class="form-group">
        <input
          v-model="form.confirmPwd"
          type="password" class="form-control" placeholder="请再次输入新密码" required>
      </div>
      <button :disabled="submitting" type="submit" class="btn btn-block btn-primary">修 改</button>
    </form>
  </div>
</div>

<script src="${ctx}/static/js/containers/login/changePwd.js"></script>
</body>
</html>
