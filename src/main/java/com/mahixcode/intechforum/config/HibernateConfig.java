/*
 * Copyright (C) 2019 maximen39
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahixcode.intechforum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author maximen39
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.mahixcode.intechforum.jpa")
public class HibernateConfig {

    private final Environment environment;

    public HibernateConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        managerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        managerFactoryBean.setDataSource(dataSource());
        managerFactoryBean.setPersistenceUnitName("jpaPersistenceUnit");
        managerFactoryBean.setPackagesToScan("com.mahixcode.intechforum.model");
        managerFactoryBean.setJpaProperties(hibernateProperties());
        return managerFactoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.show_sql",
                environment.getRequiredProperty("spring.jpa.show-sql")
        );
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto",
                environment.getRequiredProperty("spring.jpa.hibernate.ddl-auto")
        );
        hibernateProperties.setProperty(
                "hibernate.dialect",
                environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect")
        );
        return hibernateProperties;
    }
}
