package com.ushkov;

//import com.ushkov.beans.ApplicationBeans;
import com.ushkov.beans.PersistenceBeanConfiguration;
import com.ushkov.beans.SwaggerConfig;
//import com.ushkov.security.configuration.WebSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.ushkov.beans.PersistenceBeanConfiguration;
import com.ushkov.beans.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.ushkov")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSwagger2
@Import({
        //BeansEndpoint.ApplicationBeans.class,
        SwaggerConfig.class,
        PersistenceBeanConfiguration.class,
        //WebSecurityConfiguration.class,
        //SecurityConfig.class
})
public class MainSpringBootStarter {
    public static void main(String[] args) {
        SpringApplication.run(MainSpringBootStarter.class, args);
    }
}
