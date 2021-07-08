package springbook.user;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import static org.assertj.core.api.Assertions.assertThat;

public class P104 {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Test
    public void 스프링컨텍스트로부터가져온오브젝트출력코드() {

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        LOGGER.info("dao3 = " + dao3);
        LOGGER.info("dao4 = " + dao4);

        assertThat(dao3).isEqualTo(dao4); // 각기 다른 값을 가진 동일하지 않은 오브젝트

    }

}
