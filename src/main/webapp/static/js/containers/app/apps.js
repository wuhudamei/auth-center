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
                path: '/',
                name: '应用管理',
                active: true //激活面包屑的
            }],
            $dataTable: null,
            form: {
                keyword: '',
                status: ''
            },
            organizations: ''
        },
        methods: {
            auto: function () {
                this.$dataTable.bootstrapTable('selectPage', 1);
            },
            drawTable: function () {
                var self = this;
                self.$dataTable = $(this.$els.dataTable).bootstrapTable({
                    url: '/api/apps',
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
                        field: 'name',
                        title: '名称',
                        align: 'center'
                    }, {
                        field: 'appid',
                        title: '应用id',
                        align: 'center'
                    }, {
                        field: 'status',
                        title: '应用状态',
                        align: 'center',
                        formatter: function (value, row) {
                            // OPENED, LOCK
                            if ('OPENED' == value) {
                                return '开启';
                            } else if ('LOCK' == value) {
                                return '锁定'
                            }
                            return value;
                        }
                    }, {
                        field: '', //将id作为排序时会和将id作为操作field内容冲突，导致无法排序
                        title: "操作",
                        align: 'center',
                        formatter: function (value, row, index) {
                            var statusName = '';
                            var status = '';
                            var btnClass = 'btn-default';
                            if (row.status == 'OPENED') {
                                statusName = '锁定';
                                status = 'LOCK';
                                btnClass = 'btn-warning';
                            } else if (row.status == 'LOCK') {
                                statusName = '启用';
                                status = 'OPENED';
                                btnClass = 'btn-info';
                            }
                            var fragment = '';
                            if (RocoUtils.hasPermission('app:edit'))
                                fragment += ('<button data-handle="operate-edit" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">编辑</button>');

                            // if (RocoUtils.hasPermission('app:role'))
                                fragment += ('<button data-handle="operate-user" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">用户管理</button>');

                            if (RocoUtils.hasPermission('app:role'))
                                fragment += ('<button data-handle="operate-role" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">角色管理</button>');
                            if (RocoUtils.hasPermission('app:permission'))
                                fragment += ('<button data-handle="operate-permission" data-id="' + row.id + '" data-status="' + status + '" type="button" class="m-r-xs btn btn-xs btn-info">权限管理</button>');
                            if (RocoUtils.hasPermission('app:switch'))
                                fragment += ('<button data-handle="operate-changeStatus" data-id="' + row.id + '" data-status="' + status + '" type="button" class="m-r-xs btn btn-xs ' + btnClass + '">' + statusName + '</button>');
                            if (RocoUtils.hasPermission('app:delete'))
                                fragment += ('<button data-handle="operate-delete" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">删除</button>');
                            return fragment;
                        }
                    }]
                });

                self.$dataTable.on('click', '[data-handle="operate-edit"]', function (e) {
                    var id = $(this).data('id');
                    window.location.href = '/admin/app/app?id=' + id;
                    e.stopPropagation();
                });

                self.$dataTable.on('click','[data-handle="operate-user"]',function(e){
                    var id = $(this).data('id');
                    window.location.href='/admin/app/users?id='+id;
                    e.stopPropagation();
                });

                self.$dataTable.on('click', '[data-handle="operate-permission"]', function (e) {
                    var id = $(this).data('id');
                    window.location.href = '/admin/app/permissions?id=' + id;
                    e.stopPropagation();
                });

                self.$dataTable.on('click', '[data-handle="operate-role"]', function (e) {
                    var id = $(this).data('id');
                    window.location.href = '/admin/app/roles?id=' + id;
                    e.stopPropagation();
                });

                self.$dataTable.on('click', '[data-handle="operate-changeStatus"]', function (e) {
                    var id = $(this).data('id');
                    var status = $(this).data('status');
                    var title = '';
                    var text = '';
                    if (status == 'OPENED') {
                        title = '启用应用';
                        text = '确定启用该应用么？';
                    } else if (status == 'LOCK') {
                        title = '锁定应用';
                        text = '确定锁定该应用么？';
                    }
                    swal({
                        title: title,
                        text: text,
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.put('/api/apps/changeStatus?id=' + id + '&status=' + status).then(function (res) {
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

                self.$dataTable.on('click', '[data-handle="operate-delete"]', function (e) {
                    var id = $(this).data('id');
                    swal({
                        title: '删除应用',
                        text: '确定删除该应用么？',
                        type: 'info',
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        showCancelButton: true,
                        showConfirmButton: true,
                        showLoaderOnConfirm: true,
                        confirmButtonColor: '#ed5565',
                        closeOnConfirm: false
                    }, function () {
                        self.$http.delete('/api/apps/' + id + '/del').then(function (res) {
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
                this.$dataTable.bootstrapTable('refresh');
            },
            createBtnClickHandler: function (e) {
                window.location.href = '/admin/app/app';
                e.stopPropagation();
            }
        },
        created: function () {
            this.fUser = window.RocoUser;
        },
        ready: function () {
            this.drawTable();
        }
    });
})
(this.RocoUtils);