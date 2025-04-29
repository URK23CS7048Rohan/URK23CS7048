package com.healthcare.config;

import javax.sql.DataSource; // This import doesn't need to change as javax.sql is still used in Java 22

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Autowired
    private Environment env;
    
    /**
     * Attempts to create a MySQL datasource, falls back to H2 if MySQL is unavailable
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        boolean fallbackEnabled = Boolean.parseBoolean(env.getProperty("app.db.fallback.enabled", "true"));
        
        if (fallbackEnabled) {
            try {
                // Try to connect to MySQL
                DataSource mysqlDataSource = mysqlDataSource();
                // Test the connection
                mysqlDataSource.getConnection().close();
                log.info("Successfully connected to MySQL database");
                return mysqlDataSource;
            } catch (Exception e) {
                log.warn("Could not connect to MySQL database. Falling back to H2 in-memory database. Error: {}", e.getMessage());
                return h2DataSource();
            }
        } else {
            // If fallback is disabled, just return MySQL datasource
            return mysqlDataSource();
        }
    }
    
    /**
     * MySQL datasource configuration
     */
    private DataSource mysqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }
    
    /**
     * H2 in-memory datasource configuration
     */
    private DataSource h2DataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.h2.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.h2.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.h2.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.h2.password"));
        return dataSource;
    }
}