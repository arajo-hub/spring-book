package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/springbook", "spring", "book");
        return c;
    }

}

/*
*
* 같은 인터페이스 타입의 빈을 여러 개 정의한 경우
*
* <beans>
*   <bean id="localDBConnectionMaker" class="...LocalDBConnectionMaker" />
*   <bean id="testDBConnectionMaker" class="...TestDBConnectionMaker" />
*   <bean id="productionDBConnectionMaker" class="...ProductionDBConnectionMaker" />
*
*   <bean id="userDao" class="springbook.user.dao.UserDao">
*       <property name="connectionMaker" ref="localDBConnectionMaker" />
*   </bean>
* </beans>
*
* */