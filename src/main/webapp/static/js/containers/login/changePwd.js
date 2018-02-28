+(function () {
  var login = new Vue({
    el: '#loginCont',
    http: {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    },
    data: {
      jobNum: '',
      redirectUrl: '',
      form: {
        originPwd: '',
        newPwd: '',
        confirmPwd: ''
      },
      submitting: false
    },
    created: function () {
      var self = this;
      self.jobNum = this.$parseQueryString()['jobNum'];
      self.redirectUrl = this.$parseQueryString()['redirectUrl'];
    },
    methods: {
      submit: function () {
        var self = this;
        var data = {
          jobNum: self.jobNum,
          originPwd: self.form.originPwd,
          newPwd: self.form.newPwd,
          confirmPwd: self.form.confirmPwd
        };
        self.submitting = true;
        self.$http.post(ctx + '/oauth/password', data, {emulateJSON: true}).then(function (response) {
          var result = response.data;
          if (result.code == 1) {
            window.location.href = self.redirectUrl;
          } else {
            Vue.toastr.error(result.message);
          }
        }).catch(function () {

        }).finally(function () {
          self.submitting = false;
        });
      }
    },
    ready: function () {
    }
  });
})();