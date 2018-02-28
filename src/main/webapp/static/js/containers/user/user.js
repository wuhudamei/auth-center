var vueIndex = null;
+(function (RocoUtils) {
  $('#userMenu').addClass('active');
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
      drawTable: function () {
        var self = this;
        self.$dataTable = $(this.$els.dataTable).bootstrapTable({
          url: '/api/users',
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
            field: 'status',
            title: '状态',
            align: 'center',
            formatter: function (value, row, index) {
              if (value == 'OPEN') {
                return '启用';
              } else if (value == 'LOCK') {
                return '锁定';
              }
              return '异常';
            }
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
              // fragment += ('<button data-handle="operate-edit" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">编辑</button>');
              // fragment += ('<button data-handle="operate-resetpwd" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">重置密码</button>');
              fragment += ('<button data-handle="operate-setRoles" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">设置角色</button>');
              fragment += ('<button data-handle="operate-setApps" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-info">设置应用</button>');
              // fragment += ('<button data-handle="operate-changeStatus" data-id="' + row.id + '"data-status="' + operateStatus + '" type="button" class="m-r-xs btn btn-xs btn-warning">' + operateName + '</button>');
              // fragment += ('<button data-handle="operate-del" data-id="' + row.id + '" type="button" class="m-r-xs btn btn-xs btn-danger">删除</button>');
              return fragment;
            }
          }]
        });

        // 编辑按钮
        self.$dataTable.on('click', '[data-handle="operate-edit"]', function (e) {
          var id = $(this).data('id');
          self.$http.get('/api/users/' + id).then(function (res) {
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
            self.$http.get('/api/users/' + id + '/password').then(function (res) {
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
          self.$http.get('/api/users/' + id).then(function (response) {
            var res = response.data;
            if (res.code == '1') {
              var user = res.data;
              if(user == null){
                  self.$toastr.error('用户已删除');
                  return;
              }
              var _$modal = $('#appsModal').clone();
              var $modal = _$modal.modal({
                backdrop: 'static',
                keyboard: false
              });
              appsModal($modal, user);
            }
          });
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
            self.$http.post('/api/users/' + id + '/status', {status: status}, {emulateJSON: true}).then(function (res) {
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
            self.$http.delete('/api/users/' + id, {status: status}, {emulateJSON: true}).then(function (res) {
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
          email: ''
        };
        this.showModel(user, false);
      },
      showModel: function (user, isEdit) {
        var _$modal = $('#modal').clone();
        var $modal = _$modal.modal({
          height: 300,
          maxHeight: 300,
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
        submitBtnClick: false,
        apps: []
      },
      created: function () {
        this.fetchLoginUserApps();
      },
      methods: {
        // 获取登录用户的app权限
        fetchLoginUserApps: function () {
          var self = this;
          self.$http.get('/api/system/user/app')
            .then(function (response) {
              var res = response.data;
              if (res.code == '1') {
                self.apps = res.data;
                _.forEach(self.apps, function (app, index) {
                  app.checked = false; // 默认全不选中
                });

                if (isEdit) {
                  // 如果是编辑模式，需要考虑checkbox回显
                  _.forEach(self.user.apps, function (app, index) {
                    var index = _.findIndex(self.apps, function (item) {
                      return app.id == item.id;
                    });
                    if (index != -1) {
                      self.apps[index].checked = true;
                    }
                  })
                }
              }
            });
        },
        checkApp: function (app, e) {
          var checked = e.target.checked;
          if (checked) {
            app.checked = true;
          } else {
            app.checked = false;
          }
        },
        submit: function () {
          var self = this;
          self.submitBtnClick = true;
          self.$validate(true, function () {
            if (self.$validation.valid) {
              self.disabled = true;
              var selectAppIds = [];
              _.forEach(self.apps, function (app, index) {
                if (app.checked) {
                  selectAppIds.push(app.id);
                }
              });
              self.user.appIds = selectAppIds;
              if (isEdit) {
                self.$http.put('/api/users', self.user).then(function (response) {
                  self.processResponse(response);
                })
              } else {
                self.$http.post('/api/users', self.user).then(function (response) {
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

  // copy的代码 不敢改 TAT 暂时留着permission吧
  function rolesModal($el, id) {
    //获取node
    var el = $el.get(0);

    //创建Vue对象编译节点
    var vueModal = new Vue({
        el: el,
        minxins: [RocoVueMixins.ModalMixin],
        $modal: $el, //模式窗体 jQuery 对象
        data: {
          permissions: {},
          selectedPermissions: {}
        },
        created: function () {
          this.getRolePermission(id);
        },
        ready: function () {
        },
        methods: {
          //查询角色权限
          getRolePermission: function (id) {
            var self = this;
            self.$http.get('/api/users/' + id + '/role').then(function (response) {
              var res = response.data;
              if (res.code == 1) {
                self.permissions = res.data;
                self.setCheckedPermissions();//将该角色已有的权限添加到已选列表中
              }
            })
          },
          setCheckedPermissions: function () {
            var self = this;
            if (self.permissions) {
              for (var module in self.permissions) {
                if (self.permissions[module]) {
                  var permissions = self.permissions[module];
                  $(permissions).each(function (index, _item) {
                    if (_item.checked == true) {
                      self.selectedPermissions[_item.id] = _item.id;
                    }
                  });
                }
              }
            }
          },
          //所有权限选择
          selAllCb: function (permissions, e) {
            var self = this;
            _.each(permissions,
              function (permission, index, array) {
                self.checkAll(permission, e);
              });
          },
          checkAll: function (permission, e) {
            var self = this;
            var checked = e.target.checked;
            _.each(permission,
              function (content, index, array) {
                content.checked = checked;
                self.checkSub(content, e);
              });
          },
          // 单个权限选择
          checkSub: function (content, e) {
            var self = this;
            var checked = e.target.checked;
            if (checked) {
              self.selectedPermissions[content.id] = content.id;
            } else {
              self.selectedPermissions[content.id] = null;
            }
          },
          submit: function () {
            var self = this;
            var permissions = [];
            for (var key in self.selectedPermissions) {
              if (self.selectedPermissions[key]) {
                permissions.push(self.selectedPermissions[key]);
              }
            }
            self.$http.post('/api/users/' + id + '/role', {roleIds: permissions}, {emulateJSON: true}).then(function (res) {
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

  function appsModal($el, model) {
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
        user: model,
        submitBtnClick: false,
        apps: []
      },
      created: function () {
        this.fetchLoginUserApps();
      },
      methods: {
        // 获取登录用户的app权限
        fetchLoginUserApps: function () {
          var self = this;
          self.$http.get('/api/system/user/app')
            .then(function (response) {
              var res = response.data;
              if (res.code == '1') {
                self.apps = res.data;
                _.forEach(self.apps, function (app, index) {
                  app.checked = false; // 默认全不选中
                });

                //checkbox回显
                _.forEach(self.user.apps, function (app, index) {
                  var index = _.findIndex(self.apps, function (item) {
                    return app.id == item.id;
                  });
                  if (index != -1) {
                    self.apps[index].checked = true;
                  }
                })
              }
            });
        },
        checkApp: function (app, e) {
          var checked = e.target.checked;
          if (checked) {
            app.checked = true;
          } else {
            app.checked = false;
          }
        },
        submit: function () {
          var self = this;
          self.submitBtnClick = true;
          self.disabled = true;
          var selectAppIds = [];
          _.forEach(self.apps, function (app, index) {
            if (app.checked) {
              selectAppIds.push(app.id);
            }
          });
          console.log(selectAppIds)
          self.$http.post('/api/users/' + self.user.id + '/app',
            {appIds: selectAppIds},
            {emulateJSON: true})
            .then(function (response) {
              self.processResponse(response);
            })
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
})(this.RocoUtils);