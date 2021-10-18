package org.springframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springbook.user.dao.TestUserService;
import springbook.user.dao.UserDao;
import springbook.user.service.DummyMailSender;
import springbook.user.service.MailSender;
import springbook.user.service.UserService;

@Configuration
@Profile("test")
public class TestAppContext {

    @Autowired
    UserDao userDao;

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

}
