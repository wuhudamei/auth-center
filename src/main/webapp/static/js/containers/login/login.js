+(function (RocoUtils, io) {
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
        uuid: ''
      },
      socket: null,
      qrcode: '',
      submitting: false,
      nameLogin: "0" //默认扫码登录
    },
    created: function () {
    },
    methods: {
      a: function (type) {
        var self = this;
        if (type == "qrcode") {
          self.nameLogin = "0";
        } else {
          self.nameLogin = "1";
        }
      },
      submit: function () {
        var self = this;
        var data = {
          username: self.form.username,
          password: self.form.password,
          uuid: self.form.uuid
        };
        self.submitting = true;
        self.$http.post(ctx + '/api/login', $.param(data)).then(function (res) {
          if (res.data.code == 1) {
            window.location.href = RocoUtils.parseQueryString().successUrl || ctx + '/index';
          } else {
            Vue.toastr.error(res.data.message);
          }
        }).catch(function (e) {
          Vue.toastr.error(e);
        }).finally(function () {
          self.submitting = false;
        });
      },
    }
  });
})(RocoUtils, io);