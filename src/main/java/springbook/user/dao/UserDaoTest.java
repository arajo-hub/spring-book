package springbook.user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
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
//        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        // XML
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//        System.out.println(user2.getPassword());
//        System.out.println(user2.getId() + " 조회 성공");

        // 이 테스트 방법에서 가장 돋보이는 건,
        // main() 메소드를 이용해 쉽게 테스트 수행을 가능하게 했다는 점과
        // 테스트할 대상인 UserDao를 직접 호출해서 사용한다는 점이다.

        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("조회 테스트 성공");
        }

    }

}
