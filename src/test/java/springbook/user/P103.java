//package springbook.user;
//
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
////import springbook.user.dao.DaoFactory;
//import springbook.user.dao.UserDao;
//import static org.assertj.core.api.Assertions.*;
//
//public class P103 {
//
//    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
//
//    @Test
//    public void 직접생성한DaoFactory오브젝트출력코드() {
//
//        DaoFactory factory = new DaoFactory();
//        UserDao dao1 = factory.userDao();
//        UserDao dao2 = factory.userDao();
//
//        LOGGER.info("dao1 = " + dao1);
//        LOGGER.info("dao2 = " + dao2);
//
//        assertThat(dao1).isNotEqualTo(dao2); // 각기 다른 값을 가진 동일하지 않은 오브젝트
//
//    }
//
//}
