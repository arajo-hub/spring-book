package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

//        // UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트를 만든다.
//        ConnectionMaker connectionMaker = new DConnectionMaker();
//
//        // 1. UserDao 생성
//        // 2. 사용할 ConnectionMaker 타입의 오브젝트 제공.
//        // -> 두 오브젝트 사이의 의존관계 설정 효과
//        UserDao dao = new UserDao(connectionMaker);
////        UserDao dao = new UserDao();

        // 팩토리를 사용하도록 수정한 UserDaoTest
//        UserDao dao = new DaoFactory().userDao();

        // 애플리케이션 컨텍스트를 적용한 UserDaoTest
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

    }

}
