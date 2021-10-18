package org.springframework.core.io.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.learningtest.spring.oxm.OxmSqlService;
import springbook.user.sqlservice.SqlService;

@Configuration
public class SqlServiceContext {

    @Autowired
    SqlMapConfig sqlMapConfig;

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.setSqlmap(this.sqlMapConfig.getSqlMapResource());
        return sqlService;
    }

}
