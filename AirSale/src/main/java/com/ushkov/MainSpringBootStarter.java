package com.ushkov;

import com.ushkov.beans.ApplicationBeans;
import com.ushkov.beans.PersistenceBeanConfiguration;
import com.ushkov.beans.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(scanBasePackages = "com.ushkov")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@EnableCaching
@EnableSwagger2
@Import({
        ApplicationBeans.class,
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
