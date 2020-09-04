package com.cartest.pro.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JdbcUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory
            .getLogger(DataSourceConfig.class);

    @Autowired
    private DruidDataSourceProperties properties;


    @Value("${spring.datasource.password}")
    private String passwd;

    // 数据库类型: mysql、oracle
    private static String dbType = "";

    @Bean(value = "customDataSource") public DataSource dataSource()
            throws RuntimeException {
        //Const.GRAMMAR = gramar;
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(passwd);
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(
                properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(
                properties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setTestWhileIdle(properties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(properties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(
                properties.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(
                properties.getMaxPoolPreparedStatementPerConnectionSize());

        try {
            if (Objects.nonNull(druidDataSource)) {
                druidDataSource.setFilters("wall,stat");
                druidDataSource.setUseGlobalDataSourceStat(true);
                druidDataSource.init();
            }
        } catch (Exception e) {
            throw new RuntimeException("load datasource error, dbProperties is :",
                    e);
        }
        return druidDataSource;
    }

    /**
     * 返回sqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(
            @Qualifier(value = "customDataSource") DataSource dataSource)
            throws RuntimeException {
        try {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean
                    .setTypeAliasesPackage("com.cartest.pro.pojo");
            //添加XML目录
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            // 从driverName和jdbcUrl得到数据库类型dbType
            dbType = JdbcUtils.getDbType(properties.getUrl(),
                    properties.getDriverClassName());
            String xmlLocation = String
                    .format("classpath:/mapping/*Mapper.xml", dbType);
            sqlSessionFactoryBean.setConfigLocation(
                    resolver.getResource("classpath:/mybatis-config.xml"));
            sqlSessionFactoryBean
                    .setMapperLocations(resolver.getResources(xmlLocation));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            logger.error("sqlSessionFactoryBean is error!", e);
            throw new RuntimeException("sqlSessionFactoryBean is error!", e);
        }
    }

}

