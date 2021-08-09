package com.ushkov;


import com.ushkov.beans.ApplicationBeans;
import com.ushkov.beans.PersistenceBeanConfiguration;
import com.ushkov.beans.SwaggerConfig;
import com.ushkov.security.configuration.WebSecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(scanBasePackages = "com.ushkov", excludeName = "com.ushkov.controller.oldcontrollers")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableWebMvc
@EnableTransactionManagement
@EnableCaching
@EnableSwagger2
@Import({

        ApplicationBeans.class,
        SwaggerConfig.class,
        PersistenceBeanConfiguration.class,
        WebSecurityConfiguration.class
})
public class MainSpringBootStarter {
    public static void main(String[] args) {
        SpringApplication.run(MainSpringBootStarter.class, args);
    }
}
