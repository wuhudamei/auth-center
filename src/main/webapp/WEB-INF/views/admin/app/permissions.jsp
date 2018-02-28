<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<title>权限管理</title>
<link rel="stylesheet" href="/static/vendor/jstree/themes/default/style.css"/>
<!-- 面包屑 -->
<div id="container" class="wrapper wrapper-content">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox">
        <div class="ibox-content">
            <div class="ibox-content">
                <div class="row">
                    <form id="searchForm">
                        <div class="col-md-12 text-left">
                            <div class="form-group" id="buttons">
                                <a id="createBtn" @click="createBtnClickHandler"
                                   class="btn btn-outline btn-primary">新增权限</a>

                                <a id="editBtn" @click="editBtn"
                                   class="btn btn-outline btn-primary">编辑</a>

                                <a id="deleteBtn" @click="deleteBtn"
                                   class="btn btn-outline btn-danger">删除</a>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="ibox-content">
                    <div class="row">
                        <div class="col-md-5">
                            <div id="jstree"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- ibox end -->
</div>
<!-- container end-->

<!-- 新建/编辑的modal-->
<div id="modal" class="modal fade" tabindex="-1" data-width="760">
    <validator name="validation">
        <form name="createMirror" novalidate class="form-horizontal" role="form">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3>权限管理</h3>
            </div>
            <div class="modal-body">
                <div class="form-group" :class="{'has-error':$validation.name.invalid && submitBtnClick}">
                    <label for="name" class="col-sm-2 control-label">名称</label>
                    <div class="col-sm-8">
                        <input v-model="permission.name"
                               v-validate:name="{
                                    required:{rule:true,message:'请输入权限名称'}
                               }"
                               maxlength="20"
                               data-tabindex="1"
                               id="name" name="name" type="text" class="form-control" placeholder="请输入权限名称">
                        <span v-cloak v-if="$validation.name.invalid && submitBtnClick"
                              class="help-absolute">
                          <span v-for="error in $validation.name.errors">
                            {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                          </span>
                        </span>
                    </div>
                </div>

                <div class="form-group"
                     :class="{'has-error':$validation.description.invalid && submitBtnClick}">
                    <label for="description" class="col-sm-2 control-label">描述</label>
                    <div class="col-sm-8">
                        <textarea v-model="permission.description"
                                  v-validate:description="{
                                    required:{rule:true,message:'请输入描述'}
                                  }"
                                  id="description"
                                  maxlength="255"
                                  placeholder="请输入描述"
                                  class="form-control">
                        </textarea>
                        <span v-cloak v-if="$validation.description.invalid && submitBtnClick"
                              class="help-absolute">
                            <span v-for="error in $validation.description.errors">
                              {{error.message}} {{($index !== ($validation.description.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group"
                     :class="{'has-error':$validation.permission.invalid && submitBtnClick}">
                    <label for="description" class="col-sm-2 control-label">权限值</label>
                    <div class="col-sm-8">
                        <input v-model="permission.permission"
                               v-validate:permission="{
                                    required:{rule:true,message:'请输入权限值'}
                                  }"
                               id="permission"
                               maxlength="255"
                               placeholder="请输入权限值"
                               class="form-control">
                        <span v-cloak v-if="$validation.permission.invalid && submitBtnClick"
                              class="help-absolute">
                            <span v-for="error in $validation.permission.errors">
                              {{error.message}} {{($index !== ($validation.permission.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
                    </div>
                </div>

                <div class="form-group"
                     :class="{'has-error':$validation.seq.invalid && submitBtnClick}">
                    <label for="seq" class="col-sm-2 control-label">排序值</label>
                    <div class="col-sm-8">
                        <input v-model="permission.seq"
                               v-validate:seq="{
                                    required:{rule:true,message:'请输入排序值'}
                                  }"
                               id="seq"
                               type="number"
                               maxlength="255"
                               placeholder="请输入排序值"
                               class="form-control">
                        </input>
                        <span v-cloak v-if="$validation.seq.invalid && submitBtnClick"
                              class="help-absolute">
                            <span v-for="error in $validation.seq.errors">
                              {{error.message}} {{($index !== ($validation.seq.errors.length -1)) ? ',':''}}
                            </span>
                        </span>
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

<script src="/static/vendor/iCheck/icheck.min.js"></script>

<script src="/static/vendor/sweetalert/sweetalert.min.js"></script>
<script src="/static/vendor/jstree/jstree.js"></script>
<script src="${ctx}/static/js/containers/app/permissions.js?v=1.0"></script>
