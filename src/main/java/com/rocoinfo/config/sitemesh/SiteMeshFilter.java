package com.rocoinfo.config.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

import javax.servlet.annotation.WebFilter;

/**
 * <dl>
 * <dd>Description: 集成sitemesh</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/27 上午11:01</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@WebFilter(urlPatterns = "/*")
public class SiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.setMimeTypes("text/html", "application/xhtml+xml", "application/vnd.wap.xhtml+xml")
                .addDecoratorPath("/", "/WEB-INF/views/admin/layouts/signin.jsp")
                .addDecoratorPath("/index", "/WEB-INF/views/admin/layouts/signin.jsp")
                .addDecoratorPath("/admin/*", "/WEB-INF/views/admin/layouts/signin.jsp")
                .addExcludedPath("/static/*")
                .addExcludedPath("/login")
                .addExcludedPath("/login*")
                .addExcludedPath("/druid/*")
                .addExcludedPath("/oauth/*");

    }
}
