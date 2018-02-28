<%--
  这个页面是点击微信菜单绑定账号时使用，和扫码登录绑定逻辑不同
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>错误页面</title>
  <meta name="keywords" content="">
  <meta name="description" content="错误页面">
  <link rel="shortcut icon" href="favicon.ico">

  <link rel="stylesheet" href="${ctx}/static/css/lib.css">
  <link rel="stylesheet" href="${ctx}/static/css/style.css">
</head>
<body class="gray-bg">
<div class="middle-box text-center animated fadeInDown">
  <h1>500</h1>
  <h3 class="font-bold">${error_msg.message}</h3>
  <div class="error-desc">
  </div>
</div>
</body>
</html>