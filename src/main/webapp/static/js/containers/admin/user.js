var vueIndex = null;
+(function (RocoUtils) {
  $('#systemMenu').addClass('active');
  $('#admin').addClass('active');
  vueIndex = new Vue({
    el: '#container',
    data: {
      $dataTable: null,
      fUser: null,
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
          url: '/api/system/user',
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
            title: '用户名',
            align: 'center'
          }, {
            field: 'name',
            title: '姓名',
            align: 'center'
          }, {
            field: 'mobile',
            title: '电话',
            align: 'center'
          }, {
            field: 'email',
            title: '邮箱',
            align: 'center'
          }, {
            field: 'department',
            title: '部门',
            align: 'center'
          }, {
            field: 'company',
            title: '公司',
            align: 'center'
          }, {
            field: '', //将id作为排序时会和将id作为操作field内容冲突，导致无法排序
            title: "操作",
            align: 'center',
            formatter: function (value, row, index) {
              var operateStatus = '';
              var operateName = '';
              if (row.status == 'OPEN') {
                operateStatus = 'LOCK';
                operateName = '锁定';
              } else {
                operateStatus = 'OPEN';
                operateName = '启用';
              }
              var fragment = '';
              if (RocoUtils.hasPermission('admin:edit'))
                fragment += ('<button data-handle="operate-edit" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">编辑</button>');
              if (RocoUtils.hasPermission('admin:pwd'))
                fragment += ('<button data-handle="operate-resetpwd" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">重置密码</button>');
              if (RocoUtils.hasPermission('admin:role'))
                fragment += ('<button data-handle="operate-setRoles" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">设置角色</button>');
              if (RocoUtils.hasPermission('admin:app'))
                fragment += ('<button data-handle="operate-setApps" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">设置应用</button>');
              if (RocoUtils.hasPermission('admin:switch'))
                fragment += ('<button data-handle="operate-changeStatus" data-id="' + row.id + '"data-status="' + operateStatus + '" type="button" class="m-r-xs btn btn-xs btn-warning">' + operateName + '</button>');
              if (RocoUtils.hasPermission('admin:delete'))
                fragment += ('<button data-handle="operate-del" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">删除</button>');
              return fragment;
            }
          }]
        });

        // 编辑按钮
        self.$dataTable.on('click', '[data-handle="operate-edit"]', function (e) {
          var id = $(this).data('id');
          self.$http.get('/api/system/user/' + id).then(function (res) {
            if (res.data.code == 1) {
              var user = res.data.data;
              this.showModel(user, true);
            }
          });
        });

        // 重置密码
        self.$dataTable.on('click', '[data-handle="operate-resetpwd"]', function (e) {
          var id = $(this).data('id');
          swal({
            title: '重置密码',
            text: '确定要将该用户密码重置为默认密码？',
            type: 'info',
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            showCancelButton: true,
            showConfirmButton: true,
            showLoaderOnConfirm: true,
            confirmButtonColor: '#ed5565',
            closeOnConfirm: false
          }, function () {
            self.$http.get('/api/system/user/' + id + '/password').then(function (res) {
              if (res.data.code == 1) {
                self.$toastr.success('重置成功');
              }
            }).catch(function () {
              self.$toastr.error('系统异常');
            }).finally(function () {
              swal.close();
            });
          });
          e.stopPropagation();
        });

        // 设置角色
        self.$dataTable.on('click', '[data-handle="operate-setRoles"]', function (e) {
          var id = $(this).data('id');
          var _$modal = $('#rolesModal').clone();
          var $modal = _$modal.modal({
            backdrop: 'static',
            keyboard: false
          });
          rolesModal($modal, id);
        });

        // 设置应用
        self.$dataTable.on('click', '[data-handle="operate-setApps"]', function (e) {
          var id = $(this).data('id');
          var _$modal = $('#appsModal').clone();
          var $modal = _$modal.modal({
            backdrop: 'static',
            keyboard: false
          });
          appsModal($modal, id);
        });

        // 启用、禁用
        self.$dataTable.on('click', '[data-handle="operate-changeStatus"]', function (e) {
          var id = $(this).data('id');
          var status = $(this).data('status');
          var title = '';
          var text = '';
          if (status == 'OPEN') {
            title = '启用用户';
            text = '确定启用这个用户吗？';
          } else if (status == 'LOCK') {
            title = '锁定用户';
            text = '确定锁定这个用户吗？';
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
            self.$http.post('/api/system/user/status/' + id, {status: status}, {emulateJSON: true}).then(function (res) {
              if (res.data.code == 1) {
                self.$toastr.success('操作成功');
                self.$dataTable.bootstrapTable('refresh');
              }
            }).finally(function () {
              swal.close();
            });
          });
          e.stopPropagation();
        });

        // 删除
        self.$dataTable.on('click', '[data-handle="operate-del"]', function (e) {
          var id = $(this).data('id');
          swal({
            title: '删除用户',
            text: '确定要删除该用户？',
            type: 'info',
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            showCancelButton: true,
            showConfirmButton: true,
            showLoaderOnConfirm: true,
            confirmButtonColor: '#ed5565',
            closeOnConfirm: false
          }, function () {
            self.$http.delete('/api/system/user/' + id, {status: status}, {emulateJSON: true}).then(function (res) {
              if (res.data.code == 1) {
                self.$toastr.success('操作成功');
                self.$dataTable.bootstrapTable('refresh');
              }
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
      createBtnClickHandler: function (e) {
        var user = {
          username: '',
          name: '',
          phone: '',
          email: '',
          department: '',
          company: ''
        };
        this.showModel(user, false);
      },
      showModel: function (user, isEdit) {
        var _$modal = $('#modal').clone();
        var $modal = _$modal.modal({
          height: 450,
          maxHeight: 450,
          backdrop: 'static',
          keyboard: false
        });
        modal($modal, user, isEdit);
      }
    },
    created: function () {
      this.fUser = window.RocoUser;
    },
    ready: function () {
      this.drawTable();
    }
  });
  // 实现弹窗方法
  function modal($el, model, isEdit) {
    // 获取 node
    var el = $el.get(0);
    isEdit = isEdit || false;
    // 创建 Vue 对象编译节点
    var vueModal = new Vue({
      el: el,
      // 模式窗体必须引用 ModalMixin
      mixins: [RocoVueMixins.ModalMixin],
      validators: {
        mobile: function (val) {
          return /^1[3|4|5|7|8]\d{9}$/.test(val);
        },
        email: function (val) {
          return (/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            .test(val));
        }
      },
      $modal: $el, //模式窗体 jQuery 对象
      data: {
        //控制 按钮是否可点击
        disabled: false,
        //模型复制给对应的key
        user: model,
        submitBtnClick: false
      },
      methods: {
        submit: function () {
          var self = this;
          self.submitBtnClick = true;
          self.$validate(true, function () {
            if (self.$validation.valid) {
              self.disabled = true;
              if (isEdit) {
                self.$http.put('/api/system/user', self.user).then(function (response) {
                  self.processResponse(response);
                })
              } else {
                self.$http.post('/api/system/user', self.user, {emulateJSON: true}).then(function (response) {
                  self.processResponse(response);
                })
              }
            }
          });
        },
        processResponse: function (response) {
          var self = this;
          var res = response.data;
          if (res.code == 1) {
            $el.on('hidden.bs.modal', function () {
              vueIndex.$dataTable.bootstrapTable('refresh');
              self.$toastr.success('操作成功');
            });
            $el.modal('hide');
          } else {
            self.$toastr.error(res.message);
            self.disabled = false;
          }
        }
      }
    });
    // 创建的Vue对象应该被返回
    return vueModal;
  }

  function rolesModal($el, id) {
    //获取node
    var el = $el.get(0);

    //创建Vue对象编译节点
    var vueModal = new Vue({
        el: el,
        minxins: [RocoVueMixins.ModalMixin],
        $modal: $el, //模式窗体 jQuery 对象
        data: {
          roles: [],
          selectedRoles: {}
        },
        created: function () {
          this.getRoles(id);
        },
        ready: function () {
        },
        methods: {
          //查询用户角色信息
          getRoles: function (id) {
            var self = this;
            self.$http.get('/api/system/user/role/' + id).then(function (response) {
              var res = response.data;
              if (res.code == 1) {
                self.roles = res.data;
                self.setCheckedRole();//将该用户已有的角色添加到选中角色中
              }
            })
          },
          setCheckedRole: function () {
            var self = this;
            if (self.roles) {
              $(self.roles).each(function (index, _this) {
                if (_this.checked == true) {
                  self.selectedRoles[_this.id] = _this.id;
                }
              });
            }
          },
          // 查询组织架构
          checkSub: function (role, e) {
            var self = this;
            var checked = e.target.checked;
            if (checked) {
              self.selectedRoles[role.id] = role.id;
            } else {
              self.selectedRoles[role.id] = null;
            }
          },
          submit: function () {
            var self = this;
            var roles = [];
            for (var key in self.selectedRoles) {
              if (self.selectedRoles[key]) {
                roles.push(self.selectedRoles[key]);
              }
            }
            if (roles.length == 0) {
              Vue.toastr.warning('请至少选择一个角色');
              return false;
            }
            self.$http.post('/api/system/user/role/' + id, {roles: roles}, {emulateJSON: true}).then(function (res) {
              if (res.data.code == 1) {
                self.$toastr.success('操作成功');
                $el.modal('hide');
                self.$destroy();
              }
            }).finally(function () {
              self.disabled = false;
            });
          }
        }
      }
    );
    //创建的vue对象应该被返回
    return vueModal;
  }

  function appsModal($el, id) {
    //获取node
    var el = $el.get(0);

    //创建Vue对象编译节点
    var vueModal = new Vue({
        el: el,
        minxins: [RocoVueMixins.ModalMixin],
        $modal: $el, //模式窗体 jQuery 对象
        data: {
          apps: [],
          selectedApps: {}
        },
        created: function () {
          this.getApps(id);
        },
        ready: function () {
        },
        methods: {
          //查询用户应用信息
          getApps: function (id) {
            var self = this;
            self.$http.get('/api/system/user/app/' + id).then(function (response) {
              var res = response.data;
              if (res.code == 1) {
                self.apps = res.data;
                self.setCheckedApps();//将该用户已有的角色添加到选中角色中
              }
            })
          },
          setCheckedApps: function () {
            var self = this;
            if (self.apps) {
              $(self.apps).each(function (index, _this) {
                if (_this.checked == true) {
                  self.selectedApps[_this.id] = _this.id;
                }
              });
            }
          },
          // 点击事件
          checkSub: function (app, e) {
            var self = this;
            var checked = e.target.checked;
            if (checked) {
              self.selectedApps[app.id] = app.id;
            } else {
              self.selectedApps[app.id] = null;
            }
          },
          submit: function () {
            var self = this;
            var apps = [];
            for (var key in self.selectedApps) {
              if (self.selectedApps[key]) {
                apps.push(self.selectedApps[key]);
              }
            }
            if (apps.length == 0) {
              Vue.toastr.warning('请至少选择一个角色');
              return false;
            }
            self.$http.post('/api/system/user/app/' + id, {apps: apps}, {emulateJSON: true}).then(function (res) {
              if (res.data.code == 1) {
                self.$toastr.success('操作成功');
                $el.modal('hide');
                self.$destroy();
              }
            }).finally(function () {
              self.disabled = false;
            });
          }
        }
      }
    );
    //创建的vue对象应该被返回
    return vueModal;
  }
})
(this.RocoUtils);