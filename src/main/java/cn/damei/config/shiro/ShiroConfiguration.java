package cn.damei.config.shiro;

import cn.damei.config.shiro.realm.AuthUserPasswordRealm;
import cn.damei.config.shiro.realm.AuthUserSSORealm;
import cn.damei.config.shiro.realm.SystemUserDbRealm;
import cn.damei.config.shiro.filter.CustomerUserFilter;
import cn.damei.config.shiro.filter.DelegatingShiroFilterProxy;
import cn.damei.utils.PasswordUtil;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class ShiroConfiguration {


    @Bean("systemUserDbRealm")
    public Realm systemUserDbRealm() {
        SystemUserDbRealm realm = new SystemUserDbRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    @Bean("authUserSSORealm")
    public Realm authUserSSORealm() {
        return new AuthUserSSORealm();
    }

    @Bean("authUserPasswordRealm")
    public Realm authUserPasswordRealm() {
        AuthUserPasswordRealm realm = new AuthUserPasswordRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }



    @Bean("hashedCredentialsMatcher")
    public CredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(PasswordUtil.HASH_ALGORITHM);
        matcher.setHashIterations(PasswordUtil.HASH_ITERATIONS);
        return matcher;
    }


    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager());
//        securityManager.setRealm(systemUserDbRealm());
        List<Realm> realms = new ArrayList<>();
        realms.add(systemUserDbRealm());
        realms.add(authUserPasswordRealm());
        realms.add(authUserSSORealm());
        securityManager.setRealms(realms);

        securityManager.setCacheManager(null);
        return securityManager;
    }

    public SessionManager sessionManager() {
        return new ServletContainerSessionManager();
    }

    @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        // 将filter交给spring管理
        filterRegistration.setFilter(new DelegatingShiroFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filter = new ShiroFilterFactoryBean();
        filter.setSecurityManager(securityManager());
        // 设置登录页面
        filter.setLoginUrl("/login");
        // 设置登录成功的页面
        filter.setSuccessUrl("/");
        // 设置没有权限跳转的页面
        filter.setUnauthorizedUrl("/login");
        // 设置fitler
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("user", new CustomerUserFilter());
        filter.setFilters(filterMap);

        // 定义url 需要对应的权限
        Map<String, String> chainDefinitionMap = new LinkedHashMap<>();
        chainDefinitionMap.put("/oauth/*", "anon");
        chainDefinitionMap.put("/druid", "anon");
        chainDefinitionMap.put("/druid/*", "anon");
        chainDefinitionMap.put("/login", "anon");
        chainDefinitionMap.put("/api/login","anon");
        chainDefinitionMap.put("/api/logout","anon");
        chainDefinitionMap.put("/externalApi/**","anon");
        chainDefinitionMap.put("/MP_verify_0oBnzdXyLkXePXtm.txt","anon");
        chainDefinitionMap.put("/MP_verify_CkSqh5WBpmK04WDs.txt","anon");
        chainDefinitionMap.put("/MP_verify_KznZROUB9KVvgvAK.txt","anon");
        chainDefinitionMap.put("/", "user");
        chainDefinitionMap.put("/*", "user");

        filter.setFilterChainDefinitionMap(chainDefinitionMap);
        return filter;
    }
}
