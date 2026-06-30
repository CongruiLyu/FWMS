package com.fwms.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 单例模式 - 数据库连接池(HikariCP)由Spring容器以单例方式管理
 * Spring Boot默认使用HikariCP作为DataSource单例
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public String dataSourceInfo(DataSource dataSource) {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikari = (HikariDataSource) dataSource;
            return "HikariCP Pool: " + hikari.getPoolName();
        }
        return "DataSource initialized";
    }
}
