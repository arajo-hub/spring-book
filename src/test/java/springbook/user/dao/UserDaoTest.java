package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.learningtest.jdk.TransactionHandler;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.*;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sun.tools.doclint.Entity.times;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@ExtendWith(SpringExtension.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations = "/test-applicationContext.xml")
//@DirtiesContext // 테스트 메소드에서 애플리케이션 컨텍스트의 구성이나 상태를 변경한다는 것을 테스트 컨텍스트 프레임워크에 알려준다.
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;
    // 인스턴스 변수인 context는 어디에서도 초기화해주는 코드가 없다.
    // 따라서 메소드에서 context를 사용하려고 하면 NullPointerException이 발생해야 한다.
    // 하지만 테스트는 아무런 문제 없이 성공적으로 끝난다.
    // context 변수에 애플리케이션 컨텍스트가 들어 있기 때문이다.

    // applicationContext.xml에 정의된 빈이 아니라,
    // ApplicationContext라는 타입의 변수에 @Autowired를 붙였는데
    // 애플리케이션 컨텍스트가 DI됐다. 어찌 된 일일까?
    // 스프링 애플리케이션 컨텍스트는 초기화할 때 자기 자신도 빈으로 등록한다.
    // 따라서 애플리케이션 컨텍스트에는 ApplicationContext 타입의 빈이 존재하는 셈이고
    // DI도 가능한 것이다.

    @Autowired
    DataSource dataSource;

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    MailSender mailSender;

    @Autowired
    UserService testUserService;

//    @Autowired // ApplicationContext 타입의 인스턴스 변수를 없애고 UserDao 빈을 직접 DI 받는다.
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    List<User> users;

//    @BeforeEach // @Test 메소드가 실행되기 전에 먼저 실행되어야 하는 메소드를 정의
//    public void setUp() {
//
//        // @BeforeEach 메소드가 테스트 메소드 개수만큼 반복되기 때문에 애플리케이션 컨텍스트도 세 번 만들어진다.
//        // 지금은 설정도 간단하고 빈도 몇 개 없어서 별문제 아닌 듯하지만, 빈이 많아지고 복잡해지면
//        // 애플리케이션 컨텍스트 생성에 적지 않은 시간이 걸릴 수 있다.
//        // 애플리케이션 컨텍스트가 만들어질 때는 모든 싱글톤 빈 오브젝트를 초기화한다.
//        // 단순히 빈 오브젝트를 만드는 정도라면 상관없지만,
//        // 어떤 빈은 오브젝트가 생성될 때 자체적인 초기화 작업을 진행해서 제법 많은 시간을 필요로 하기 때문이다.
//        // 또 한 가지 문제는 애플리케이션 컨텍스트가 초기화될 때
//        // 어떤 빈은 독자적으로 많은 리소스를 할당하거나 독립적인 스레드를 띄우기도 한다는 점이다.
//        // 이런 경우에는 테스트를 마칠 때마다 애플리케이션 컨텍스트 내의 빈이 할당한 리소스 등을 깔끔하게 정리해주지 않으면
//        // 다음 테스트에서 새로운 애플리케이션 컨텍스트가 만들어지면서 문제를 일으킬 수도 있다.
//        // 문제는 JUnit이 매번 테스트 클래스의 오브젝트를 새로 만든다는 점이다.
//        // 따라서 여러 테스트가 함께 참조할 애플리케이션 컨텍스트를 오브젝트 레벨에 저장해두면 곤란하다.
//        // 그렇다면 스태틱 필드에 애플리케이션 컨텍스트를 저장해두면 어떨까?
//        // JUnit은 테스트 클래스 전체에 걸쳐 딱 한 번만 실행되는 @BeforeClass 스태틱 메소드를 지원한다.
//        // 이 메소드에서 애플리케이션 컨텍스트를 만들어 스태틱 변수에 저장해두고 테스트 메소드에서 사용하게 할 수 있다.
//        // 하지만 이보다는 스프링이 직접 제공하는 애플리케이션 컨텍스트 테스트 지원 기능을 사용하는 것이 더 편리하다.
////        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
//
//        this.user1 = new User("gyumee", "박성철", "springno1", Level.BASIC, 1, 0);
//        this.user2 = new User("leegw700", "이길원", "springno2", Level.SILVER, 55, 10);
//        this.user3 = new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40);
//
//        this.dao = this.context.getBean("userDao", UserDao.class);
//
//        // 테스트를 위한 수동 DI 적용
//        // 이렇게 해두면 테스트가 진행되는 동안에는 UserDao가 테스트용 DataSource를 사용해서 동작하게 된다.
//        // 이 방법의 장점은 XML 설정파일을 수정하지 않고도 테스트 코드를 통해 오브젝트 관계를 재구성할 수 있다는 것이다.
//        // 예외적인 상황을 만들기 위해 일부러 엉뚱한 오브젝트를 넣거나, 위와 같이 테스트용으로 준비된 오브젝트를 사용하게 할 수 있다.
////        DataSource dataSource = new SingleConnectionDataSource(
////                "jdbc:mysql://localhost/testdb", "spring", "book", true);
////        dao.setDataSource(dataSource); // 코드에 의한 수동 DI
//
//        // 애플리케이션 컨텍스트 없는 DI
//        // 인스턴스 변수 dao 에도 @Autowired를 없애줘야 한다.
////        this.dao = new UserDao();
////        DataSource dataSource = new SingleConnectionDataSource(
////                "jdbc:mysql://localhost/testdb", "spring", "book", true);
////        dao.setDataSource(dataSource);
//
//    }

    static class MockMailSender implements org.springframework.mail.MailSender {

        private List<String> requests =  new ArrayList<String>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
        }
    }

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id = "madnite1";

        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void addAndGet() throws SQLException {

//        User user = new User();
//        user.setId("gyumee");
//        user.setName("박성철");
//        user.setPassword("springno1");
//
//        dao.add(user);
//
//        User user2 = dao.get(user.getId());
//
//        assertThat(user2.getName()).isEqualTo(user.getName());
//        assertThat(user2.getPassword()).isEqualTo(user.getPassword());

        // 이 테스트는 첫 실행시 성공이지만, 그 이후 시도시 실패.
        // 이미 데이터가 입력되어 있으므로.
        // 반복적으로 테스트를 했을 때 테스트가 실패하기도 하고 성공하기도 한다면 이는 좋은 테스트라고 할 수 없다.
        // 가장 좋은 해결책은 본 테스트를 마치고 나면 테스트가 등록한 사용자 정보를 삭제해서, 테스트를 수행하기 이전 상태로 만들어주는 것이다.

//        dao.deleteAll();
//        assertThat(dao.getCount()).isEqualTo(0);
//
//        User user = new User("gyumee", "박성철", "springno1");
//
//        dao.add(user);
//        assertThat(dao.getCount()).isEqualTo(1);
//
//        User user2 = dao.get(user.getId());
//
//        assertThat(user2.getName()).isEqualTo(user.getName());
//        assertThat(user2.getPassword()).isEqualTo(user.getPassword());

        // get() 테스트 기능을 보완
//        User user1 = new User("gyumee", "박성철", "springno1");
//        User user2 = new User("leegw700", "이길원", "springno2");

        System.out.println(user1.toString());

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userget1 = dao.get(user1.getId());

        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);

    }

    @Test
    public void count() throws SQLException {

//        User user1 = new User("gyumee", "박성철", "springno1");
//        User user2 = new User("leegw700", "이길원", "springno2");
//        User user3 = new User("bumjin", "박범진", "springno3");

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);

    }

    @Test
    public void getUserFailure() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            dao.get("unknown_id");
        });

    }

    @Test
    public void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        dao.add(user1); // Id: gyumee
        List<User> users1 = dao.getAll();
        assertThat(users1.size()).isEqualTo(1);

        dao.add(user2); // Id: leegw700
        List<User> users2 = dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3); // Id: bumjin
        List<User> users3 = dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test
    public void duplicateKey() {
        dao.deleteAll();

        assertThrows(DataAccessException.class, () -> {
            dao.add(user1);
            dao.add(user1); // 같은 사용자 두 번 등록 -> 예외 발생!
        });

        // org.springframework.dao.DuplicateKeyException: PreparedStatementCallback;
//        dao.add(user1);
//        dao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException) ex.getRootCause(); // getRootCause() 메소드를 이용하면 중첩되어 있는 SQLException을 가져올 수 있다.
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource); // translate()를 하면 SQLException을 DataAccessException 타입의 예외로 변환해준다.
            assertThat(set.translate(null, null, sqlEx), instanceOf(DuplicateKeyException.class));
        }
    }

    @Test
    public void update() {
        dao.deleteAll();

        dao.add(user1); // 수정할 사용자
        dao.add(user2); // 수정하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("springno6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);

        // 현재 테스트는 수정할 로우의 내용이 바뀐 것만 확인할 뿐이지,
        // 수정하지 않아야 할 로우의 내용이 그대로 남아 있는지는 확인해주지 못한다는 문제

        // 이 문제를 해결할 방법
        // 1. JdbcTemplate의 update()가 돌려주는 리턴 값을 확인
        // 2. 테스트를 보강해서 원하는 사용자 외의 정보는 변경되지 않았음을 직접 확인 -> 이 방법으로 보완!

    }

    // 빈 주입 확인 테스트
    @Test
    public void bean() {
        assertThat(this.userServiceImpl, notNullValue());
    }

//    @Test
//    public void upgradeLevels() throws Exception {
//        userDao.deleteAll();
//        for (User user : users) {
//            userDao.add(user);
//        }
//
//        userService.upgradeLevels();
//
//        checkLevelUpgraded(users.get(0), false);
//        checkLevelUpgraded(users.get(1), true);
//        checkLevelUpgraded(users.get(2), false);
//        checkLevelUpgraded(users.get(3), true);
//        checkLevelUpgraded(users.get(4), false);
//    }
//
//    @Test
//    @DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다.
//    public void upgradeLevels() throws Exception {
//        // 목 오브젝트를 통해 메일 발송 여부를 검증하도록 수정
//        userDao.deleteAll();
//        for (User user : users) {
//            userDao.add(user);
//        }
//
//        MockMailSender mockMailSender = new MockMailSender();
//        userService.setMailSender((MailSender) mockMailSender);
//
//        userService.upgradeLevels();
//
//        checkLevelUpgraded(users.get(0), false);
//        checkLevelUpgraded(users.get(1), true);
//        checkLevelUpgraded(users.get(2), false);
//        checkLevelUpgraded(users.get(3), true);
//        checkLevelUpgraded(users.get(4), false);
//
//        List<String> request = mockMailSender.getRequests();
//        assertThat(request.size()).isEqualTo(2);
//        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
//        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
//    }

    @Test
    public void upgradeLevels() throws Exception {
//        // 목 오브젝트를 통해 메일 발송 여부를 검증하도록 수정
//        userDao.deleteAll();
//        for (User user : users) {
//            userDao.add(user);
//        }
//
//        MockMailSender mockMailSender = new MockMailSender();
//        userServiceImpl.setMailSender((MailSender) mockMailSender);
//
//        userServiceImpl.upgradeLevels();
//
//        checkLevelUpgraded(users.get(0), false);
//        checkLevelUpgraded(users.get(1), true);
//        checkLevelUpgraded(users.get(2), false);
//        checkLevelUpgraded(users.get(3), true);
//        checkLevelUpgraded(users.get(4), false);
//
//        List<String> request = mockMailSender.getRequests();
//        assertThat(request.size()).isEqualTo(2);
//        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
//        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());

        // MockUserDao를 사용해서 만든 고립된 테스트
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender((MailSender) mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size()).isEqualTo(2);
        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());

    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4); // GOLD 레벨
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null); // 레벨이 비어 있는 사용자

        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    @DirtiesContext // 컨텍스트 설정을 변경하기 때문에 여전히 필요!
    public void upgradeAllOrNothing() throws Exception {
//        TestUserService testUserService = new TestUserService(users.get(3).getId());
//        testUserService.setUserDao(this.userDao);
////        testUserService.setDataSource(this.dataSource);
//        testUserService.setMailSender(mailSender); // 테스트용 UserService를 위한 메일 전송  오브젝트의 수동 DI
//
//        UserServiceTx txUserService = new UserServiceTx();
//        txUserService.setTransactionManager(transactionManager);
//        txUserService.setUserService(testUserService);
//
//        userDao.deleteAll();
//
//        for (User user : users) {
//            userDao.add(user);
//        }
//
//        try {
//            txUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
//        } catch (TestUserServiceException e) {
//
//        }
//
//        checkLevelUpgraded(users.get(1), false);

//        // 다이내믹 프록시를 이용한 트랜잭션 테스트
//        TestUserService testUserService = new TestUserService(users.get(3).getId());
//        testUserService.setUserDao(this.userDao);
//        testUserService.setMailSender(mailSender); // 테스트용 UserService를 위한 메일 전송  오브젝트의 수동 DI
//
//        TransactionHandler txHandler = new TransactionHandler();
//        // 트랜잭션 핸들러가 필요한 정보와 오브젝트를 DI해준다.
//        txHandler.setTarget(testUserService);
//        txHandler.setTransactionManager(transactionManager);
//        txHandler.setPattern("upgradeLevels");
//        // UserService 인터페이스 타입의 다이내믹 프록시 생성
//        UserService txUserService = (UserService) Proxy.newProxyInstance(
//                getClass().getClassLoader(), new Class[] { UserService.class }, txHandler);
//
//        userDao.deleteAll();
//
//        for (User user : users) {
//            userDao.add(user);
//        }
//
//        try {
//            txUserService.upgradeLevels();
//            fail("TestUserServiceException expected");
//        } catch (TestUserServiceException e) {
//
//        }
//
//        checkLevelUpgraded(users.get(1), false);

        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

//        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class); // 테스트용 타깃 주입
        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
//            txUserService.upgradeLevels();
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void mockUpgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
    }

    @Test
    public void advisorAutoProxyCreator() {
        assertThat(testUserService).isEqualTo(java.lang.reflect.Proxy.class);
    }

}
