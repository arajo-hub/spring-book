package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration // 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정 정보라는 표시
public class DaoFactory {

    // UserDao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
    // DaoFactory의 userDao 메소드를 호출하면 DConnectionMaker를 사용해
    // DB 커넥션을 가져오도록 이미 설정된 UserDao 오브젝트를 돌려준다.
    // UserDaoTest는 이제 UserDao가 어떻게 만들어지는지 어떻게 초기화되어 있는지에 신경 쓰지 않고
    // 팩토리로부터 UserDao 오브젝트를 받아다가, 자신의 관심사인 테스트를 위해 활용하기만 하면 그만이다.

//    @Bean // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
//    public UserDao userDao() {
////        return new UserDao(connectionMaker);
////        return new UserDao(connectionMaker());
//        // 수정자 메소드 DI를 사용하는 것으로 변경
//        UserDao userDao = new UserDao();
//        userDao.setConnectionMaker(connectionMaker());
//        // = <property name="connectionMaker" ref="connectionMaker" />
//        return userDao;
//    }

    /*
     *
     * UserDao 빈을 위한 XML 정보
     *
     * <bean id="userDao" class="springbook.dao.UserDao">
     *   <property name="connectionMaker" ref="connectionMaker" />
     * </bean>
     *
     * */

    // DataSource 타입의 dataSource 빈 정의 메소드
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");
        dataSource.setUsername("spring");
        dataSource.setPassword("book");

        return dataSource;
    }


    // DataSource 타입의 빈을 DI 받는 userDao() 빈 정의 메소드
    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public AccountDao accountDao() {
//        return new AccountDao(new DConnectionMaker());
        return new AccountDao(connectionMaker());
    }

    @Bean
    public MessageDao messageDao() {
//        return new MessageDao(new DConnectionMaker());
        return new MessageDao(connectionMaker());
    }

    @Bean // --------------------------------------> <bean
    public ConnectionMaker connectionMaker() { // -> id="connectionMaker"
        return new DConnectionMaker(); // ---------> class="springbook...DConnectionMaker" />
    }

}

/*
*
* 완성된 XML 설정정보
*
* <beans>
*   <bean id="connectionMaker" class="springbook.user.dao.DConnectionMaker" />
*   <bean id="userDao" class="springbook.user.dao.UserDao">
*       <property name="connectionMaker" ref="connectionMaker" />
*   </bean>
* </beans>
*
* */