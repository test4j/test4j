package org.test4j.example.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.test4j.module.database.sql.Test4JDataSourceHelper;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.test4j.example.spring")
public class SpringConfig {
//    @Bean(name = "dataSource")
//    public DataSource newDataSource() {
//        return Test4JDataSourceHelper.createLocalDataSource("dataSource");
//    }
}
