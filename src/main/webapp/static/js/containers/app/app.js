var vueIndex = null;
+(function (RocoUtils) {
    $('#appMenu').addClass('active');
    vueIndex = new Vue({
        el: '#container',
        data: {
            // 面包屑
            breadcrumbs: [{
                path: '/',
                name: '主页'
            }, {
                path: '/',
                name: '新增应用',
                active: true //激活面包屑的
            }],
            submitBtnClick: false,
            disabled: false,
            redirectUrl: '/admin/app/apps',
            title: '',
            isEdit: false,
            app: null


        },
        methods: {
            initParams: function () {
                var self = this;
                var params = RocoUtils.parseQueryString(window.location.search.substr(1));
                var id = params['id'];
                if (RocoUtils.isNotEmpty(id)) {
                    self.title = '编辑应用信息';
                    self.isEdit = true;
                    $('#appManager').addClass('active');
                    self.buildApp(id);
                } else {
                    self.title = '新增应用';
                    self.isEdit = false;
                    $('#createApp').addClass('active');
                    self.buildApp(null);
                }

            },
            buildApp: function (id) {
                var self = this;
                if (id) {
                    self.$http.get('/api/apps/' + id + '/get').then(function (response) {
                        var result = response.data;
                        if (result.code == '1') {
                            self.app = result.data;
                        }
                    });
                } else {
                    self.app = {
                        name: '',
                        token: '',
                        url: '',
                        wxAppid: '',
                        wxSecret: '',
                        pushFlag: 1
                    };
                }
            },
            submit: function () {
                var self = this;
                self.submitBtnClick = true;
                self.$validate(true, function () {
                    if (self.$validation.valid) {
                        self.disabled = true;
                        self.$http.post('/api/apps', self.app).then(function (response) {
                            var result = response.data;
                            if (result.code == '1') {
                                self.$toastr.success(result.message);
                                RocoUtils.redirect2Url(self.redirectUrl, 2000);
                            } else {
                                self.$toastr.error(result.message);
                            }
                        }).finally(function () {
                            self.disabled = false;
                        });
                    }
                });
            },
            cancel: function () {
                var self = this;
                RocoUtils.redirect2Url(self.redirectUrl);
            }
        },
        created: function () {
        },
        ready: function () {
            this.initParams();
        }
    });
})
(this.RocoUtils);