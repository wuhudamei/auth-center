/**
 * 2017-5-17 Andy 修改
 * @description 
 * 新增 weixin:false参数
 * 新增 isWeiXin 方法,判断是否微信.
 */
(function() {
	$(function() {
		var header = new Vue({
			el : '#header',
			data : {
				isShow : false
			},
			methods : {
				logout : function() {
					var self = this;
					self.$http.get(ctx + '/api/logout').then(
							function(response) {
								window.location.href = ctx + "/login";
							});
				},
				isWeixin : function() {
					var self = this;
					var ua = navigator.userAgent.toLowerCase();
					if (ua.match(/MicroMessenger/i) == "micromessenger") { //微信
						self.isShow = false; //微信不显示菜单
					} else {
						self.isShow = true;  //PC显示菜单
					}
				}
			},
			created : function() {
				this.isWeixin();
			}
		});
	});
})();