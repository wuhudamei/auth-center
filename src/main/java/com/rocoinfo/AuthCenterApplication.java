package com.rocoinfo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan // 开启@WebServlet、@WebFitler等注解
@EnableRedisHttpSession(redisNamespace = "auth") // 启动spring session 由redis统一管理session
@EnableConfigurationProperties
@EnableTransactionManagement // 开启事物
@MapperScan(basePackages = "com.rocoinfo.repository") // 扫描dao接口
public class AuthCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthCenterApplication.class, args);
	}
}
