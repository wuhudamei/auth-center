<%@ page language="java" pageEncoding="UTF-8" %>

<!--左侧导航开始-->
<nav id="nav" class="navbar-default navbar-static-side"
     role="navigation">
  <div class="nav-close">
    <i class="fa fa-times-circle"></i>
  </div>
  <div id="navUser" class="sidebar-collapse">
    <div class="nav-header">
      <div>
        <div class="dropdown profile-element">
          <%--<span> <img src="/static/img/head.jpg" class="img-circle" width="60px" height="60px"/>--%>
          <%--</span>--%>
          <%--<a data-toggle="dropdown" class="dropdown-toggle" href="#">--%>
          <%--<span class="clear"> <span class="block m-t-xs">--%>
          <%--<strong class="font-bold">--%>
          <%--<shiro:principal property="name"/>--%>
          <%--</strong>--%>
          <%--</span> <span class="text-muted text-xs block">--%>
          <%--</span>--%>
          <%--</span>--%>
          <%--</a>--%>
        </div>
      </div>
    </div>
    <!-- 左侧菜单 start-->
    <ul class="nav metismenu" id="sideMenu">
      <shiro:hasPermission name="system:manager">
        <li id="systemMenu"><a href="#">
          <i class="fa fa-edit"></i>
          <span class="nav-label">系统管理</span>
          <span class="fa arrow"></span>
        </a>
          <ul class="nav nav-second-level sidebar-nav">
            <shiro:hasPermission name="admin:menu">
              <li id="admin">
                <a href="/admin/admin/user">
                  <i class="fa fa-edit"></i> <span class="nav-label">管理员管理</span>
                </a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="role:menu">
              <li id="adminRole">
                <a href="/admin/admin/role">
                  <i class="fa fa-edit"></i> <span class="nav-label">角色管理</span>
                </a>
              </li>
            </shiro:hasPermission>
          </ul>
        </li>
      </shiro:hasPermission>

      <shiro:hasPermission name="app:menu">
        <li id="appMenu"><a href="#">
          <i class="fa fa-edit"></i>
          <span class="nav-label">应用管理</span>
          <span class="fa arrow"></span>
        </a>
          <ul class="nav nav-second-level sidebar-nav">
            <shiro:hasPermission name="app:list">
              <li id="appManager">
                <a href="/admin/app/apps">
                  <i class="fa fa-edit"></i> <span class="nav-label">应用管理</span>
                </a>
              </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="app:add">
              <li id="createApp">
                <a href="/admin/app/app">
                  <i class="fa fa-edit"></i> <span class="nav-label">新增应用</span>
                </a>
              </li>
            </shiro:hasPermission>
          </ul>
        </li>
      </shiro:hasPermission>

      <shiro:hasPermission name="user:menu">
        <li id="userMenu">
          <a href="/admin/user/user">
            <i class="fa fa-id-card" aria-hidden="true"></i>
            <span class="nav-label">用户管理</span>
          </a>
        </li>
      </shiro:hasPermission>

      <shiro:hasPermission name="pwd:edit">
        <li id="modifyPwd">
          <a href="/admin/password/modify">
            <i class="fa fa-edit" aria-hidden="true"></i>
            <span class="nav-label">修改密码</span>
          </a>
        </li>
      </shiro:hasPermission>
    </ul>
    <!-- 左侧菜单 end-->
  </div>
</nav>
<!--左侧导航结束-->