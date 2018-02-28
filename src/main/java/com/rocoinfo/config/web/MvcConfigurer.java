package com.rocoinfo.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * <dl>
 * <dd>Description: 配置不经过controller直接访问jsp页面</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/27 上午10:52</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Configuration
public class MvcConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 配置不经过controller的jsp页面
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/login").setViewName("/admin/login");
        registry.addViewController("/oauth/error");
        registry.addViewController("/oauth/success");
        registry.addViewController("/oauth/login");
        registry.addViewController("/oauth/bind");
        registry.addViewController("/oauth/scan/bind");
        registry.addViewController("/oauth/changePwd");
        registry.addViewController("/admin/*");
        registry.addViewController("/admin/*/*");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

}
