package cn.damei.config.sitemesh;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;

import javax.servlet.annotation.WebFilter;


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
