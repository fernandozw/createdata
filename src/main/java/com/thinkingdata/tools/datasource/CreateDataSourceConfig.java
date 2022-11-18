package com.thinkingdata.tools.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.thinkingdata.server.dao", sqlSessionFactoryRef = "createDataSqlSessionFactory")
public class CreateDataSourceConfig {
    // 将这个对象放入Spring容器中
    @Bean(name = "createDataSource")
    // 表示这个数据源是默认数据源
    @Primary
    // 读取application.yml中的配置参数映射成为一个对象
    // prefix表示参数的前
    @ConfigurationProperties(prefix = "spring.datasource.createdata")
    public DataSource getDataSourceCreateData()
    {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "createDataSqlSessionFactory")
    // 表示这个数据源是默认数据源
    @Primary
    // @Qualifier表示查找Spring容器中名字为webUiDataSource的对象
    public SqlSessionFactory createDataSqlSessionFactory(@Qualifier("createDataSource") DataSource datasource)
            throws Exception
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath:mapping/createData/*.xml"));
        return bean.getObject();
    }
    @Bean("createDataSqlSessionTemplate")
    // 表示这个数据源是默认数据源
    @Primary
    public SqlSessionTemplate createDataSqlSessionTemplate(
            @Qualifier("createDataSqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }

}
