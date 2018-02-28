<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<title>用户管理</title>
<div id="container" class="wrapper wrapper-content">
  <div id="breadcrumb">
    <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
  </div>
  <!-- ibox start -->
  <div class="ibox">
    <div class="ibox-content">
      <div class="row">
        <form id="searchForm" @submit.prevent="query">
          <div class="col-md-2">
            <div class="form-group">
              <label class="sr-only" for="keyword"></label>
              <input
                v-model="form.keyword"
                id="keyword"
                name="keyword"
                type="text"
                placeholder="用户名/姓名" class="form-control"/>
            </div>
          </div>
          <div class="col-md-2">
            <div class="form-group">
              <select v-model="form.status"
                      id="status"
                      name="status"
                      placeholder="选择状态"
                      class="form-control">
                <option value="">全部状态</option>
                <option value="OPEN">启用</option>
                <option value="LOCK">禁用</option>
              </select>
            </div>
          </div>
          <div class="col-md-1">
            <div class="form-group">
              <button id="searchBtn" type="submit" class="btn btn-block btn-outline btn-default"
                      alt="搜索"
                      title="搜索">
                <i class="fa fa-search"></i>
              </button>
            </div>
          </div>
          <!-- 将剩余栅栏的长度全部给button -->
          <div class="col-md-7 text-right">
            <shiro:hasPermission name="admin:user:add">
              <%--<div class="form-group">--%>
                <%--<button @click="createBtnClickHandler" id="createBtn" type="button"--%>
                        <%--class="btn btn-outline btn-primary">新增--%>
                <%--</button>--%>
              <%--</div>--%>
            </shiro:hasPermission>
          </div>
        </form>
      </div>
      <!-- <div class="columns columns-right btn-group pull-right"></div> -->
      <table v-el:data-table id="dataTable" width="100%"
             class="table table-striped table-bordered table-hover">
      </table>
    </div>
  </div>
  <!-- ibox end -->
</div>
<!-- 添加/编辑的modal-->
<div id="modal" class="modal fade" tabindex="-1" data-width="760">
  <validator name="validation">
    <form name="createMirror" novalidate class="form-horizontal" role="form">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>用户管理</h3>
      </div>
      <div class="modal-body">
        <div class="form-group" :class="{'has-error':$validation.username.invalid && submitBtnClick}">
          <label for="username" class="col-sm-2 control-label">用户名</label>
          <div class="col-sm-8">
            <input v-model="user.username"
                   v-validate:username="{
                     required:{rule:true,message:'请输入用户名'}
                   }"
                   maxlength="20"
                   id="username" type="text" class="form-control" placeholder="请输入用户名">
            <span v-cloak v-if="$validation.username.invalid && submitBtnClick"
                  class="help-absolute">
                <span v-for="error in $validation.username.errors">
                  {{error.message}} {{($index !== ($validation.username.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.name.invalid && submitBtnClick}">
          <label for="name" class="col-sm-2 control-label">姓名</label>
          <div class="col-sm-8">
            <input v-model="user.name"
                   v-validate:name="{
                     required:{rule:true,message:'请输入姓名'}
                   }"
                   maxlength="20"
                   data-tabindex="1"
                   id="name" name="name" type="text" class="form-control" placeholder="请输入姓名">
            <span v-cloak v-if="$validation.name.invalid && submitBtnClick" class="help-absolute">
                <span v-for="error in $validation.name.errors">
                  {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.mobile.invalid && submitBtnClick}">
          <label for="mobile" class="col-sm-2 control-label">手机</label>
          <div class="col-sm-8">
            <input v-model="user.mobile"
                   v-validate:mobile="{
                      required:{rule:true,message:'请输入手机号'},
                      mobile:{rule:true,message:'请输入正确的手机号'}
                   }"
                   maxlength="20"
                   data-tabindex="1"
                   id="mobile" name="mobile" type="text" class="form-control" placeholder="请输入手机号">
            <span v-cloak v-if="$validation.mobile.invalid && submitBtnClick" class="help-absolute">
                <span v-for="error in $validation.mobile.errors">
                  {{error.message}} {{($index !== ($validation.mobile.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group" :class="{'has-error':$validation.email.invalid && submitBtnClick}">
          <label for="email" class="col-sm-2 control-label">邮箱</label>
          <div class="col-sm-8">
            <input v-model="user.email"
                   v-validate:email="{
                        required:{rule:true,message:'请输入邮箱'},
                        email:{rule:true,message:'请输入正确的邮箱'}
                    }"
                   maxlength="50"
                   data-tabindex="1"
                   id="email" name="email" type="text" class="form-control" placeholder="请输入邮箱">
            <span v-cloak v-if="$validation.email.invalid && submitBtnClick" class="help-absolute">
                <span v-for="error in $validation.email.errors">
                  {{error.message}} {{($index !== ($validation.email.errors.length -1)) ? ',':''}}
                </span>
            </span>
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-2 control-label">所属应用</label>
          <div class="col-sm-8">
            <label v-for="app in apps">
              <input checked="{{app.checked}}"
                     type="checkbox"
                     value="{{app.id}}"
                     @click="checkApp(app,$event)"/> {{app.name}} &nbsp;&nbsp;
            </label>
          </div>
        </div>
      </div>
      <div class="modal-footer">
        <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">取消</button>
        <button @click="submit" :disabled="disabled" type="button" class="btn btn-primary">提交</button>
      </div>
    </form>
  </validator>
</div>
<!-- 设置角色的modal -->
<div id="rolesModal" class="modal fade" tabindex="-1" data-width="760">
  <form name="createMirror" novalidate class="form-inline" role="form">
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3>设置角色</h3>
    </div>

    <div class="modal-body">
      <div class="form-group" style="display: block">
        <div class="row ">
          <div class="col-md-3 form-group role-item ellips">
            <%--<label>--%>
              <%--<input @click="selAllCb(permissions,$event)" id="selAllCb"--%>
                     <%--type="checkbox"--%>
                     <%--data-checkbox="sub">--%>
              <%--所有角色</label>--%>
          </div>
        </div>
        <div v-for="permission in permissions">
          <div class="row ">
            <div class="col-md-3 col-xs-3 form-group role-item ellips">
              <label>
                <input @click="checkAll(permission,$event)" style="margin-left:40px"
                       type="checkbox"
                       data-checkbox="select"> {{$key}}</label>
            </div>
          </div>

          <div class="row" style="margin-left:60px">
            <div v-for="content in permission"
                 class="col-md-4 col-xs-4  form-group role-item ellips">
              <label>
                <input @click="checkSub(content,$event)" type="checkbox"
                       :checked="content.checked"
                       data-checkbox="sub">{{content.name}}</label>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="modal-footer">
      <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">取消</button>
      <button @click="submit" :disabled="disabled" type="button" class="btn btn-primary">提交</button>
    </div>
  </form>
</div>

<%--<div id="rolesModal" class="modal fade" tabindex="-1" data-width="760">--%>
  <%--<form name="createMirror" novalidate class="form-horizontal" role="form">--%>
    <%--<div class="modal-header">--%>
      <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>--%>
      <%--<h3>设置用户角色</h3>--%>
    <%--</div>--%>
    <%--<div class="modal-body">--%>
      <%--<div class="form-group">--%>
        <%--<div class="row" style="margin-left: 40px">--%>
          <%--<div v-for="role in roles">--%>
            <%--<div class="col-md-4 col-sm-4 col-xs-6  form-group role-item ellips">--%>
              <%--<label :for="role.id" +$index title="{{role.description}}">--%>
                <%--<input type="checkbox" :checked="role.checked"--%>
                       <%--id=role"+$index--%>
                       <%--@click="checkSub(role,$event)"--%>
                       <%--data-checkbox="sub"--%>
                       <%--data-rolename="role.name" data-rolevalue="role.id">--%>
                <%--{{role.name}}</label>--%>
            <%--</div>--%>
          <%--</div>--%>
        <%--</div>--%>
      <%--</div>--%>
    <%--</div>--%>
    <%--<div class="modal-footer">--%>
      <%--<button :disabled="disabled" type="button" data-dismiss="modal" class="btn">取消</button>--%>
      <%--<button @click="submit" :disabled="disabled" type="button" class="btn btn-primary">提交</button>--%>
    <%--</div>--%>
  <%--</form>--%>
<%--</div>--%>

<div id="appsModal" class="modal fade" tabindex="-1" data-width="700">
  <form name="createMirror" novalidate class="form-horizontal" role="form">
    <div class="modal-header">
      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
      <h3>设置应用权限</h3>
    </div>
    <div class="modal-body">
      <div class="form-group">
        <div class="row" style="margin-left: 40px">
          <label v-for="app in apps">
            <input checked="{{app.checked}}"
                   type="checkbox"
                   value="{{app.id}}"
                   @click="checkApp(app,$event)"/> {{app.name}} &nbsp;&nbsp;
          </label>
        </div>
      </div>
    </div>
    <div class="modal-footer">
      <button :disabled="disabled" type="button" data-dismiss="modal" class="btn">取消</button>
      <button @click="submit" :disabled="disabled" type="button" class="btn btn-primary">提交</button>
    </div>
  </form>
</div>

<script src="/static/js/containers/user/user.js"></script>