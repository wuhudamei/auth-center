var vueIndex = null;
+(function (RocoUtils) {
    $('#appMenu').addClass('active');
    $('#appManager').addClass('active');
    vueIndex = new Vue({
        el: '#container',
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '/admin/app/apps',
                name: '应用管理'
            }, {
                path: '/',
                name: '权限管理',
                active: true //激活面包屑的
            }],
            $dataTable: null,
            redirectUrl: '/admin/app/apps',
            appId: null
        },
        methods: {
            initParams: function () {
                var self = this;
                var id = RocoUtils.getQueryString('id');
                if (RocoUtils.isEmpty(id)) {
                    self.$toastr.warning('缺少查询参数！');
                    RocoUtils.redirect2Url(self.redirectUrl, 2000);
                }
                self.appId = id;
                self.drawPermissionTree();
            },
            drawPermissionTree: function () {
                var self = this;
                var _$permissionTree = $('#jstree');
                self.$http.get('/api/app/permissions?appId=' + self.appId).then(function (response) {
                    var result = response.data;
                    if (result.code == 1) {
                        $.jstree.defaults.sort = function (obj, deep) {
                            return 1;
                        }
                        _$permissionTree.jstree({
                            core: {
                                multiple: false,
                                // 不加此项无法动态删除节点
                                check_callback: true,
                                data: result.data
                            },
                            types: {
                                default: {
                                    icon: 'glyphicon glyphicon-stop'
                                }
                            },
                            sort: function () {
                                var aaa = this.get_node(arguments[0]);
                                var bbb = this.get_node(arguments[1]);
                                return aaa.original.sort > bbb.original.sort ? 1 : -1;

                            },
                            plugins: ['sort', 'types', 'wholerow', 'changed']
                        });

                    } else if (result.code == '0') {
                        self.$toastr.error(result.message);
                        RocoUtils.redirect2Url(self.redirectUrl,3000)
                    }
                }).catch(function () {

                }).finally(function () {

                });
            },
            createBtnClickHandler: function (e) {
                var self = this;
                var _$permissionTree = $('#jstree');
                var ref = _$permissionTree.jstree(true),
                    sel = ref.get_selected(true);
                // 未选择分类创建一级分类
                if (!sel.length) {
                    toastr.warning("请选择节点");
                    return;
                } else {
                    if (sel[0].parents.length > 2) {
                        toastr.warning("权限最多只能有三级");
                        return;
                    }
                    var permission = {
                        id: '',
                        name: '',
                        description: '',
                        permission: '',
                        seq: '',
                        pid: sel[0].id,
                        appId: self.appId
                    };
                    self.showModel(permission, false);
                }
            },
            editBtn: function (e) {
                var self = this;
                var _$permissionTree = $('#jstree');
                var ref = _$permissionTree.jstree(true),
                    sel = ref.get_selected(true);
                // 未选择分类创建一级分类
                if (!sel.length) {
                    self.$toastr.warning("请选择节点");
                    return;
                } else {
                    var id = sel[0].id;
                    self.$http.get('/api/app/permissions/' + id + '/get').then(function (response) {
                        var result = response.data;
                        if (result.code == '1') {
                            self.showModel(result.data, true);
                        } else {
                            self.$toastr.warning(result.message);
                            return;
                        }
                    });
                }
            },
            showModel: function (permission, isEdit) {
                var self = this;
                var _$modal = $('#modal').clone();
                var $modal = _$modal.modal({
                    height: 300,
                    maxHeight: 450,
                    backdrop: 'static',
                    keyboard: false
                });
                modal($modal, permission, isEdit, function (data) {
                    var _$jstree = $('#jstree');

                    var ref = _$jstree.jstree(true),
                        sel = ref.get_selected(true);
                    if (permission.id != '') {
                        ref.rename_node(sel, data.text);
                    } else {
                        ref.create_node(permission.pid, data);
                    }
                });
            },
            deleteBtn: function (e) {
                var self = this;
                var _$permissionTree = $('#jstree');
                var ref = _$permissionTree.jstree(true),
                    sel = ref.get_selected(true);
                if (!sel.length) {
                    toastr.warning("请选择节点");
                    return;
                } else if(sel[0].parents.length == 1){
                    toastr.warning("根节点不能删除");
                    return;
                }else {
                    swal({
                        title: "你确定删除该权限吗?",
                        text: "警告:删除后不可恢复！",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        closeOnConfirm: false
                    }, function (isConfirm) {
                        if (isConfirm) {
                            self.$http.delete('/api/app/permissions/' + sel[0].id + '/del').then(function (response) {
                                var result = response.data;
                                if (result.code == 1) {
                                    swal("操作成功!", "", "success");
                                    ref.delete_node(sel);
                                } else {
                                    self.$toastr.error(result.message);
                                }
                            }).catch(function () {
                                swal("操作失败！", "", "error");
                            }).finally(function () {
                                swal.close();
                            });
                        }
                    });
                }
            }
        },
        created: function () {
            this.fUser = window.RocoUser;
        },
        ready: function () {
            this.initParams();
        }
    });
    // 新建编辑弹窗
    function modal($el, permission, isEdit, callback) {
        // 获取 node
        var el = $el.get(0);
        // 创建 Vue 对象编译节点
        var vueModal = new Vue({
            el: el,
            // 模式窗体必须引用 ModalMixin
            mixins: [RocoVueMixins.ModalMixin],
            $modal: $el, //模式窗体 jQuery 对象
            data: {
                //控制 按钮是否可点击
                disabled: false,
                //模型复制给对应的key
                permission: permission,
                isEdit: isEdit,
                submitBtnClick: false
            },
            created: function () {

            },
            ready: function () {
            },
            methods: {
                submit: function () {
                    var self = this;
                    self.submitBtnClick = true;
                    self.$validate(true, function () {
                        if (self.$validation.valid) {
                            self.disabled = true;
                            self.$http.post('/api/app/permissions', self.permission).then(function (response) {
                                var result = response.data;
                                if (result.code == 1) {
                                    $el.on('hidden.bs.modal', function () {
                                        self.$toastr.success('操作成功');
                                        callback(result.data);
                                    });
                                    $el.modal('hide');
                                } else {
                                    self.$toastr.error(res.message);
                                }
                            }).finally(function () {
                                self.disabled = false;
                            });
                        }
                    });
                }
            }
        });
        // 创建的Vue对象应该被返回
        return vueModal;
    }
})(RocoUtils);