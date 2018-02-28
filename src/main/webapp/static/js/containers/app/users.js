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
                name: '用户管理',
                active: true //激活面包屑的
            }],
            $dataTable: null,
            form: {
                appId: '',
                keyword: ''
            },
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
                self.form.appId = id;
                self.drawTable();
            },
            auto: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $(this.$els.dataTable).bootstrapTable({
                    url: '/api/app/users',
                    method: 'get',
                    dataType: 'json',
                    cache: false, //去缓存
                    pagination: true, //是否分页
                    sidePagination: 'server', //服务端分页
                    queryParams: function (params) {
                        // 将table 参数与搜索表单参数合并
                        return _.extend({}, params, self.form);
                    },
                    mobileResponsive: true,
                    undefinedText: '-', //空数据的默认显示字符
                    striped: true, //斑马线
                    maintainSelected: true, //维护checkbox选项
                    sortName: 'id', //默认排序列名
                    sortOrder: 'desc', //默认排序方式
                    columns: [{
                        field: 'username',
                        title: '工号',
                        align: 'center'
                    }, {
                        field: 'name',
                        title: '姓名',
                        align: 'center'
                    }, {
                        field: 'mobile',
                        title: '手机号',
                        align: 'center'
                    }, {
                        field: 'email',
                        title: '邮箱',
                        align: 'center'
                    }, {
                        field: '', //将id作为排序时会和将id作为操作field内容冲突，导致无法排序
                        title: "操作",
                        align: 'center',
                        formatter: function (value, row, index) {
                            var fragment = '';
                            fragment += ('<button data-handle="operate-delete" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">删除</button>');
                            return fragment;
                        }
                    }]
                });

                self.$dataTable.on('click', '[data-handle="operate-delete"]', function (e) {
                    var id = $(this).data('id');
                    swal({
                        title: '删除绑定用户',
                        text: '确定删除该用户么？',
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.delete('/api/app/users/' + id + '/del?appId='+self.appId).then(function (res) {
                            if (res.data.code == 1) {
                                self.$toastr.success('操作成功');
                                self.$dataTable.bootstrapTable('refresh');
                            }
                        }).catch(function () {

                        }).finally(function () {
                            swal.close();
                        });
                    });
                    e.stopPropagation();
                });
            },
            query: function () {
                this.$dataTable.bootstrapTable('refresh', {pageNumber: 1});
            },
            addUser: function (e) {
                this.showModel();
            },
            showModel: function () {
                var _$modal = $('#addUserModal').clone();
                var $modal = _$modal.modal({
                    width: 680,
                    maxWidth: 680,
                    height: 560,
                    maxHeight: 560,
                    backdrop: 'static',
                    keyboard: false
                });
                modal($modal);
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
    function modal($el, model, isEdit) {
        // 获取 node
        var el = $el.get(0);
        // 创建 Vue 对象编译节点
        var vueModal = new Vue({
            el: el,
            mixins: [RocoVueMixins.ModalMixin],
            $modal: $el,
            created: function () {
            },
            data: {
                form: {
                    keyword: '',
                    appId: vueIndex.appId
                },
                users: []
            },
            methods: {
                query: function () {
                    this.$dataTable.bootstrapTable('selectPage', 1);
                },
                drawTable: function () {
                    var self = this;
                    self.$dataTable = $('#stylistDataTable', self._$el).bootstrapTable({
                        url: '/api/app/users/notBindUsers',
                        method: 'get',
                        dataType: 'json',
                        cache: false,
                        pagination: true,
                        sidePagination: 'server',
                        queryParams: function (params) {
                            return _.extend({},
                                params, self.form);
                        },
                        mobileResponsive: true,
                        undefinedText: '-',
                        striped: true,
                        maintainSelected: true,
                        sortOrder: 'desc',
                        columns: [{
                            checkbox: true,
                            align: 'center',
                            width: '5%',
                            formatter: function (value, row) {
                                var select = false;
                                self.users.forEach(function (val) {
                                    if (row.id == val.userId) {
                                        select = true;
                                    }
                                });
                                return select;
                            }
                        }, {
                            field: 'username',
                            title: '工号',
                            align: 'center'
                        }, {
                            field: 'name',
                            title: '姓名',
                            align: 'center'
                        }, {
                            field: 'mobile',
                            title: '手机号',
                            align: 'center'
                        }]
                    });
                    //勾选
                    self.$dataTable.on('check.bs.table', function (row, data) {
                        var useApp = {
                            appId:self.form.appId,
                            userId:data.id
                        }
                        self.users.push(useApp);
                    });
                    //取消勾选
                    self.$dataTable.on('uncheck.bs.table', function (row, data) {
                        self.users.forEach(function (val, index) {
                            if (data.id == val.userId) {
                                self.users.splice(index, 1);
                            }
                        });
                    });
                    //全选功能  TODO
                    self.$dataTable.on('check-all.bs.table', function (row, data) {
                        console.log(data);
                    });
                    //取消全选  TODO
                    self.$dataTable.on('uncheck-all.bs.table', function (row, data) {
                        console.log(data);
                    });
                },
                commit: function () {
                    var self = this;
                    if(self.users.length == 0){
                        self.$toastr.warning('至少添加一个用户！');
                        return;
                    }
                    self.$http.post('/api/app/users/addUsers', self.users).then(function (response) {
                        var result = response.data;
                        if (result.code == '1') {
                            $el.on('hidden.bs.modal', function () {
                                vueIndex.$dataTable.bootstrapTable('refresh');
                                self.$toastr.success('添加用户成功！');
                            });
                            $el.modal('hide');
                        } else {
                            self.$toastr.error(result.message);
                        }
                    });
                }
            },
            ready: function () {
                this.drawTable();
            }
        });

        // 创建的Vue对象应该被返回
        return vueModal;
    }

})(RocoUtils);