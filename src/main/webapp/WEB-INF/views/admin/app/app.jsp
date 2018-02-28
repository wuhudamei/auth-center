<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<title>应用</title>
<!-- 面包屑 -->
<div id="container" class="wrapper wrapper-content">
    <div id="breadcrumb">
        <bread-crumb :crumbs="breadcrumbs"></bread-crumb>
    </div>
    <!-- ibox start -->
    <div class="ibox" v-cloak>
        <div class="ibox-content">
            <validator name="validation">
                <h2 class="text-center">{{title}}</h2>
                <hr/>
                <form name="createOrUpdateForm" novalidate class="form-horizontal" role="form">

                    <div class="form-group" :class="{'has-error':$validation.name.invalid && submitBtnClick}">
                        <label for="name" class="col-sm-2 control-label">应用名称:</label>
                        <div class="col-sm-8">
                            <input v-model="app.name"
                                   id="name"
                                   v-validate:name="{required:{rule:true,message:'请输入应用名称'}}"
                                   maxlength="50"
                                   data-tabindex="1"
                                   name="name" class="form-control"
                                   placeholder="应用名称"/>
                            <span v-cloak v-if="$validation.name.invalid && submitBtnClick"
                                  class="help-absolute">
                            <span v-for="error in $validation.name.errors">
                                {{error.message}} {{($index !== ($validation.name.errors.length -1)) ? ',':''}}
                            </span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group" v-if="isEdit">
                        <label for="appid" class="col-sm-2 control-label">应用Id:</label>
                        <div class="col-sm-8">
                            <input v-model="app.appid"
                                   id="appid"
                                   name="appid"
                                   disabled
                                   maxlength="50"
                                   data-tabindex="1"
                                   class="form-control"
                                   placeholder="应用Id"/>
                        </div>
                    </div>

                    <div class="form-group" v-if="isEdit">
                        <label for="secret" class="col-sm-2 control-label">应用秘钥:</label>
                        <div class="col-sm-8">
                            <input v-model="app.secret"
                                   id="secret"
                                   name="secret"
                                   disabled
                                   maxlength="50"
                                   data-tabindex="1"
                                   class="form-control"
                                   placeholder="应用秘钥"/>
                        </div>
                    </div>

                    <div class="form-group" :class="{'has-error':$validation.token.invalid && submitBtnClick}">
                        <label for="token" class="col-sm-2 control-label">应用token:</label>
                        <div class="col-sm-8">
                            <input v-model="app.token"
                                   v-validate:token="{required:{rule:true,message:'请输入token'}}"
                                   maxlength="50"
                                   data-tabindex="1"
                                   id="token" name="token" type="text" class="form-control" placeholder="token">
                            <span v-cloak v-if="$validation.token.invalid && submitBtnClick"
                                  class="help-absolute">
                              <span v-for="error in $validation.token.errors">
                                {{error.message}} {{($index !== ($validation.token.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group" :class="{'has-error':$validation.url.invalid && submitBtnClick}">
                        <label for="url" class="col-sm-2 control-label">推送消息url:</label>
                        <div class="col-sm-8">
                            <input v-model="app.url"
                                   v-validate:url="{required:{rule:true,message:'请输入url'}}"
                                   maxlength="50"
                                   data-tabindex="1"
                                   id="url" name="url" type="text" class="form-control" placeholder="推送消息的url">
                            <span v-cloak v-if="$validation.url.invalid && submitBtnClick"
                                  class="help-absolute">
                              <span v-for="error in $validation.url.errors">
                                {{error.message}} {{($index !== ($validation.url.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group" :class="{'has-error':$validation.pushFlag.invalid && submitBtnClick}">
                        <label for="pushFlag" class="col-sm-2 control-label">是否接受消息推送:</label>
                        <div class="col-sm-8">
                            <select v-model="app.pushFlag"
                                   v-validate:push-flag="{required:{rule:true,message:'请选择是否收消息'}}"
                                   maxlength="50"
                                   data-tabindex="1"
                                   id="pushFlag" name="pushFlag" type="text" class="form-control" placeholder="是否接收消息">
                                <option value="1">接收</option>
                                <option value="0">不接收</option>
                            </select>
                            <span v-cloak v-if="$validation.pushFlag.invalid && submitBtnClick"
                                  class="help-absolute">
                              <span v-for="error in $validation.pushFlag.errors">
                                {{error.message}} {{($index !== ($validation.pushFlag.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>

                    <div class="form-group" :class="{'has-error':$validation.wxAppid.invalid && submitBtnClick}">
                        <label for="wxAppid" class="col-sm-2 control-label">微信的appid:</label>
                        <div class="col-sm-8">
                            <input v-model="app.wxAppid"
                                   v-validate:wx-appid="{required:{rule:true,message:'请输入微信appid'}}"
                                   maxlength="50"
                                   data-tabindex="1"
                                   id="wxAppid" name="wxAppid" type="text" class="form-control" placeholder="微信的appId">
                            <span v-cloak v-if="$validation.wxAppid.invalid && submitBtnClick"
                                  class="help-absolute">
                              <span v-for="error in $validation.wxAppid.errors">
                                {{error.message}} {{($index !== ($validation.wxAppid.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>
                    <div class="form-group" :class="{'has-error':$validation.wxSecret.invalid && submitBtnClick}">
                        <label for="wxSecret" class="col-sm-2 control-label">微信的秘钥:</label>
                        <div class="col-sm-8">
                            <input v-model="app.wxSecret"
                                   v-validate:wx-secret="{required:{rule:true,message:'请输入微信秘钥'}}"
                                   maxlength="50"
                                   data-tabindex="1"
                                   id="wxSecret" name="wxSecret" type="text" class="form-control" placeholder="微信的秘钥">
                            <span v-cloak v-if="$validation.wxSecret.invalid && submitBtnClick"
                                  class="help-absolute">
                              <span v-for="error in $validation.wxSecret.errors">
                                {{error.message}} {{($index !== ($validation.wxSecret.errors.length -1)) ? ',':''}}
                              </span>
                            </span>
                        </div>
                    </div>

                    <div class="text-center">
                        <button @click="submit" :disabled="disabled" type="button"
                                class="btn btn-primary">提交
                        </button>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <button @click="cancel" type="button" data-dismiss="modal" class="btn">返回</button>
                    </div>
                </form>
            </validator>
        </div>
    </div>
    <!-- ibox end -->
</div>
<!-- container end-->

<script src="${ctx}/static/js/containers/app/app.js?v=1.0"></script>
