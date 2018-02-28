package com.rocoinfo;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * <dl>
 * <dd>Description: spring boot 打成war包在tomcat中启动需要此类，起到类似web.xml的作用</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/7 下午1:55</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Configuration
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AuthCenterApplication.class);
    }
}
