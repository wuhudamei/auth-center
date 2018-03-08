/**
 * Created by aaron on 2017/6/6.
 */
+(function (RocoUtils) {
    var login = new Vue({
        el: '#loginCont',
        http: {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        },
        data: {
            form: {
                username: '',
                password: '',
                appid: '',
                redirectUrl: '',
                state: '',
                uuid: ''
            },
            socket: null,
            qrcode: '',
            submitting: false,
            nameLogin: "0" //默认扫码登录
        },
        created: function () {
            this.form.appid = appid;
            this.form.redirectUrl = redirectUrl;
            this.form.state = state;
        },
        ready: function () {
            var self = this;
            // 生成随机的串
            var uuid = self.generateUUID();
            self.form.uuid = uuid;
            // 加载二维码
            self.qrcode = ctx + '/oauth/load/qrcode?uuid=' + uuid;
            self.socket = io.connect('http://socket.damei.net.cn'); //大美生产
            //self.socket = io.connect('http://sockettest.damei.net.cn'); //测试
            //self.socket = io.connect('http://localhost:14082'); //本地开发
            // 将随机串推到服务端，为了服务端向客户端推送消息时，区分推到哪个客户端
            self.socket.emit('req', {
                'uuid': uuid
            });
            // 监听后端推送的消息
            self.socket.on('req', function (data) {
                var res = data;
                if ((typeof data) == "string") {
                    res = eval("(" + data + ")");
                }
                if (res.code == 1) {
                    var loginUrl = res.url + '&state=' + self.form.state + '&redirectUrl=' + self.form.redirectUrl;
                    window.location.href = loginUrl;
                } else {
                    self.$toastr.error(res.message);
                }
            });
        },
        methods: {
            switchTab: function (type) {
                var self = this;
                if (type == "qrcode") {
                  $('#qrcode').addClass('current');
                  $('#form').removeClass('current');
                    self.nameLogin = "0";
                } else {
                  $('#form').addClass('current');
                  $('#qrcode').removeClass('current');
                    self.nameLogin = "1";
                }
            },
            submit: function () {
                var self = this;
                var data = {
                    username: self.form.username,
                    password: self.form.password,
                    appid: self.form.appid,
                    redirect_url: self.form.redirectUrl,
                    state: self.form.state,
                    uuid: self.form.uuid
                };
                self.submitting = true;
                self.$http.post(ctx + '/oauth/username/password', $.param(data)).then(function (response) {
                    var res = response.data;
                    if (res.code == '1') {
                        return window.location.href = res.data.redirect;
                    } else {
                        self.$toastr.error(res.message);
                    }
                }).catch(function (e) {
                    Vue.toastr.error(e);
                }).finally(function () {
                    self.submitting = false;
                });
            },
            generateUUID: function () {
                var s = [];
                var hexDigits = "0123456789abcdef";
                for (var i = 0; i < 36; i++) {
                    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
                }
                s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
                s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
                s[8] = s[13] = s[18] = s[23] = "-";

                var uuid = s.join("");
                return uuid.replace(/[-]/g, "");
            }
        }
    });
})(RocoUtils);