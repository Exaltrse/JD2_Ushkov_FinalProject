package com.ushkov.beans;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

public class PersistenceBeanConfiguration {

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setPackagesToScan("com.ushkov");
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(getAdditionalProperties());
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }

    private Properties getAdditionalProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("current_session_context_class", "org.springframework.orm.hibernate5.SpringSessionContext");
        return properties;
    }
}
