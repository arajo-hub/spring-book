package org.springframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.issuetracker.sqlservice.updatable.EmbeddedDbSqlRegistry;
import springbook.learningtest.spring.oxm.OxmSqlService;
import springbook.user.dao.TestUserService;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.MailSender;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.bind.Unmarshaller;
import java.sql.Driver;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages="springbook.user")
public class TestApplicationContext {

    @Autowired
    SqlService sqlService;

    @Autowired
    UserDao userDao;

//    @Resource
//    Database embeddedDatabase;

    /**
     * DB 연결과 트랜잭션
     */

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();

        ds.setDriverClass(Driver.class);
        ds.setUrl("jdbc:mysql://localhost/springbook?characterEncoding=UTF-8");
        ds.setUsername("spring");
        ds.setPassword("book");

        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    /**
    * 애플리케이션 로직 & 테스트
    */

//    @Bean
//    public UserDao userDao() {
////        UserDaoJdbc dao = new UserDaoJdbc();
////        dao.setDataSource(dataSource());
////        dao.setSqlService(sqlService());
////        dao.setSqlService(this.sqlService);
////        return dao;
//        return new UserDaoJdbc();
//    }

    @Bean
    public UserService userService() {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserDao(this.userDao);
        service.setMailSender(mailSender());
        return service;
    }

    @Bean
    public UserService testUserService() {
        TestUserService testService = new TestUserService();
        testService.setUserDao(this.userDao);
        testService.setMailSender(mailSender());
        return testService;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    /**
     * SQL 서비스
     */

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("springbook.user.sqlservice.jaxb");
        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(HSQL)
                .addScript("classpath:springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
                .build();
    }

}
