+(function () {
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
        password: ''
      },
      submitting: false
    },
    methods: {
      submit: function () {
        var self = this;
        var data = {
          username: self.form.username,
          password: self.form.password,
          openid: openid,
          uuid: uuid
        };
        self.submitting = true;
        self.$http.post(ctx + '/oauth/qrcode/bind', $.param(data)).then(function (response) {
          var result = response.data;
          if (result.code == 1) {
            window.location.href = result.data.redirect;
          } else {
            self.$toastr.error(result.message);
          }
        }).catch(function () {

        }).finally(function () {
          self.submitting = false;
        });
      }
    },
    created: function () {
    },
    ready: function () {
    }
  });
})();