package springbook.user.dao;

import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * 1.1.2 UserDao
 *
 * UserDao의 관심사항
 * 1. DB와 연결을 위한 커넥션을 어떻게 가져올까라는 관심
 *  -> getConnection()으로 분리
 *  -> 이번엔 좀 더 나아가서 변화를 반기는 DAO를 만들어보자! -> 상속을 통한 확장 -> getConnection()을 추상메서드로.
 *  -> 아예 다른 클래스로 만들어보자!(완전 분리)
 * 2. 사용자 등록을 위해 DB에 보낼 SQL 문장을 담을 Statement를 만들고 실행하는 것
 * 3. 작업이 끝나면 사용한 리소스인 Statement와 Connection 오브젝트를 닫아줘서 소중한 공유 리소스를 시스템에 돌려주는 것
 *
 */
//public class UserDao {
//
//    public void add(User user) throws ClassNotFoundException, SQLException {
////        Class.forName("com.mysql.jdbc.Driver");
////        Connection c = DriverManager.getConnection(
////                "jdbc:mysql://localhost/springbook", "spring", "book");
//
//        Connection c = getConnection();
//
//        PreparedStatement ps = c.prepareStatement(
//                "insert into users(id, name, password) values (?, ?, ?)");
//
//        ps.setString(1, user.getId());
//        ps.setString(2, user.getName());
//        ps.setString(3, user.getPassword());
//
//        ps.executeUpdate();
//
//        ps.close();
//        c.close();
//
//    }
//
//    public User get(String id) throws ClassNotFoundException, SQLException {
//
////        Class.forName("com.mysql.jdbc.Driver");
////        Connection c = DriverManager.getConnection(
////                "jdbc:mysql://localhost/springbook", "spring", "book");
//
//        Connection c = getConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setName(rs.getString("name"));
//        user.setPassword(rs.getString("password"));
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        return user;
//
//    }
//
//    private Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost/springbook", "spring", "book");
//        return c;
//    }
//
//}

/**
 *
 * 상속을 통한 확장 방법이 제공되는 UserDao
 *
 */
//public abstract class UserDao {
//    public void add(User user) throws ClassNotFoundException, SQLException {
//        Connection c = getConnection();
//
//        PreparedStatement ps = c.prepareStatement(
//                "insert into users(id, name, password) values (?, ?, ?)");
//
//        ps.setString(1, user.getId());
//        ps.setString(2, user.getName());
//        ps.setString(3, user.getPassword());
//
//        ps.executeUpdate();
//
//        ps.close();
//        c.close();
//    }
//
//    public User get(String id) throws ClassNotFoundException, SQLException {
//        Connection c = getConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setName(rs.getString("name"));
//        user.setPassword(rs.getString("password"));
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        return user;
//    }
//
//    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
//
//}

/**
 *
 * 독립된 SimpleConnectionMaker를 사용하게 만든 UserDao
 *  -> N사와 D사에 UserDao 클래스만 공급하고 상속을 통해 DB 커넥션 기능을 확장해서 사용하게 했던 게 다시 불가능해졌다.
 *  왜냐하면 UserDao의 코드가 SimpleConnectionMaker라는 특정 클래스에 종속되어 있기 때문에
 *  상속을 사용했을 때처럼 UserDao 코드의 수정없이 DB 커넥션 생성 기능을 변경할 방법이 없다.
 *  다른 방식으로 DB 커넥션을 제공하는 클래스를 사용하기 위해서는 UserDao 소스코드의 다음 줄을 직접 수정해야 한다.
 *  ex) simpleConnectionMaker = new SimpleConnectionMaker();
 *  UserDao의 소스코드를 함께 제공하지 않고는 DB 연결 방법을 바꿀 수 없다는 처음 문제로 다시 되돌아와 버렸다.
 *
 *  이렇게 클래스를 분리한 경우에도 상속을 이용했을 때와 마찬가지로 자유로운 확장이 가능하게 하려면 두 가지 문제를 해결해야 한다.
 *  1. SimpleConnectionMaker의 메소드가 문제
 *  2. DB 커넥션을 제공하는 클래스가 어떤 것인지를 UserDao가 구체적으로 알고 있어야 한다는 점
 *
 */
//public class UserDao {
//
//    private SimpleConnectionMaker simpleConnectionMaker;
//
//    public UserDao() {
//        simpleConnectionMaker = new SimpleConnectionMaker();
//    }
//
//    public void add(User user) throws ClassNotFoundException, SQLException {
//        Connection c = simpleConnectionMaker.makeNewConnection();
//
//        PreparedStatement ps = c.prepareStatement(
//            "insert into users(id, name, password) values (?, ?, ?)");
//
//        ps.setString(1, user.getId());
//        ps.setString(2, user.getName());
//        ps.setString(3, user.getPassword());
//
//        ps.executeUpdate();
//
//        ps.close();
//        c.close();
//    }
//
//    public User get(String id) throws ClassNotFoundException, SQLException {
//        Connection c = simpleConnectionMaker.makeNewConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setName(rs.getString("name"));
//        user.setPassword(rs.getString("password"));
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        return user;
//    }
//
//}

//public class UserDao {
//
//    private ConnectionMaker connectionMaker; // 인터페이스를 통해 오브젝트에 접근하므로 구체적인 클래스 정보를 알 필요가 없다.
//
//    // 인스턴스 변수를 사용하도록 수정하면서 추가 -> 이렇게 해버리면 멀티스레드 환경에서 심각한 문제가 발생!
////    private Connection c;
////    private User user;
//
////    public UserDao() {
////        connectionMaker = new DConnectionMaker();
////        // UserDao의 다른 모든 곳에서는 언터페이스를 이용하게 만들어서
////        // DB 커넥션을 제공하는 클래스에 대한 구체적인 정보는 모두 제거가 가능했지만,
////        // 초기에 한 번 어떤 클래스의 오브젝트를 사용할지를 결정하는 생성자의 코드는 제거되지 않고 남아 있다.
////        // 제거하고 싶은데 간단한 방법이 보이지 않는다.
////        // 클래스 이름을 넣어서 오브젝트를 만들지 않으면 어떻게 사용하란 말인가!
////        // 결국, 여전히 UserDao 소스코드를 함께 제공해서, 필요할 때마다 UserDao의 생성자 메소드를 직접 수정하라고 하지 않고는
////        // 고객에게 자유로운 DB 커넥션 확장 기능을 가진 UserDao를 제공할 수가 없다.
////    }
//
//    // 생성자 수정
////    public UserDao(ConnectionMaker connectionMaker) {
////        this.connectionMaker = connectionMaker;
////    }
//
////    // 싱글톤 패턴을 적용한 UserDao
////    private static UserDao INSTANCE;
////
////    private UserDao(ConnectionMaker connectionMaker) {
////        this.connectionMaker = connectionMaker;
////    }
////
////    public static synchronized UserDao getInstance() {
////        if (INSTANCE == null) INSTANCE = new UserDao(???);
////        return INSTANCE;
////    }
//
//    // 의존관계 검색
////    public UserDao() {
////        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
////        this.connectionMaker = context.getBean("connectionMaker", ConnectionMaker.class); // 외부로부터의 주입이 아니라 스스로 IoC 컨테이너에게 요청 -> 검색
////    }
//
//    // 수정자 메소드 DI 방식을 사용
//    public void setConnectionMaker(ConnectionMaker connectionMaker) {
//        this.connectionMaker = connectionMaker;
//    }
//
//    // 위처럼 싱글톤 패턴을 적용하게 되면 private으로 바뀐 생성자는 외부에서 호출할 수가 없기 때문에
//    // DaoFactory에서 UserDao를 생성하며 ConnectionMaker 오브젝트를 넣어주는 게 이제는 불가능해졌다.
//    // 여러모로 생각해봐도 지금까지 깔끔하게 개선해온 UserDao에 싱글톤 패턴을 도입하는 건 무리로 보인다.
//
//    public void add(User user) throws ClassNotFoundException, SQLException {
//        Connection c = connectionMaker.makeConnection(); // 인터페이스에 정의된 메소드를 사용하므로 클래스가 바뀐다고 해도 메소드 이름이 변경될 걱정은 없다.
//
//        PreparedStatement ps = c.prepareStatement(
//            "insert into users(id, name, password) values (?, ?, ?)");
//
//        ps.setString(1, user.getId());
//        ps.setString(2, user.getName());
//        ps.setString(3, user.getPassword());
//
//        ps.executeUpdate();
//
//        ps.close();
//        c.close();
//    }
//
//    public User get(String id) throws ClassNotFoundException, SQLException {
//        Connection c = connectionMaker.makeConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
//        rs.next();
//        User user = new User();
//        user.setId(rs.getString("id"));
//        user.setName(rs.getString("name"));
//        user.setPassword(rs.getString("password"));
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        return user;
//    }
//
////    // 인스턴스 변수를 사용하도록 수정 -> 이렇게 해버리면 멀티스레드 환경에서 심각한 문제 발생!
////    public User get(String id) throws ClassNotFoundException, SQLException {
////        this.c = connectionMaker.makeConnection();
////
////        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
////        ps.setString(1, id);
////
////        ResultSet rs = ps.executeQuery();
////        rs.next();
////
////        this.user = new User();
////        this.user.setId(rs.getString("id"));
////        this.user.setName(rs.getString("name"));
////        this.user.setPassword(rs.getString("password"));
////
////        return this.user;
////    }
//
//}

/**
 * DataSource를 사용하는 UserDao
 */
//public class UserDao {
//
//    private DataSource dataSource;
//
//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    public void add(final User user) throws SQLException {
//
////        Connection c = dataSource.getConnection();
////
////        PreparedStatement ps = c.prepareStatement(
////            "insert into users(id, name, password) values (?, ?, ?)");
////
////        ps.setString(1, user.getId());
////        ps.setString(2, user.getName());
////        ps.setString(3, user.getPassword());
////
////        ps.executeUpdate();
////
////        ps.close();
////        c.close();
//
//        // 내부 클래스
////        class AddStatement implements StatementStrategy {
////
////            User user;
////
////            public AddStatement(User user) {
////                this.user = user;
////            }
////
////            @Override
////            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
////                PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
////
////                // user는 어디서 가져올까???
////                ps.setString(1, user.getId());
////                ps.setString(2, user.getName());
////                ps.setString(3, user.getPassword());
////
////                return ps;
////            }
////        }
////
////        StatementStrategy st = new AddStatement(user);
////        jdbcContextWithStatementStrategy(st);
//
//        // 메소드 파라미터로 이전한 익명 내부 클래스
//        jdbcContextWithStatementStrategy(
//            new StatementStrategy() {
//                @Override
//                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                    PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
//                    ps.setString(1, user.getId());
//                    ps.setString(2, user.getName());
//                    ps.setString(3, user.getPassword());
//
//                    return ps;
//                }
//            }
//        );
//
//    }
//
//    public User get(String id) throws SQLException {
//
//        Connection c = dataSource.getConnection();
//
//        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
//        ps.setString(1, id);
//
//        ResultSet rs = ps.executeQuery();
////        rs.next();
////
////        User user = new User();
////        user.setId(rs.getString("id"));
////        user.setName(rs.getString("name"));
////        user.setPassword(rs.getString("password"));
////
////        rs.close();
////        ps.close();
////        c.close();
//
//        // 데이터를 찾지 못하면 예외를 발생시키도록 수정
//        User user = null;
//        if (rs.next()) {
//            user = new User();
//            user.setId(rs.getString("id"));
//            user.setName(rs.getString("name"));
//            user.setPassword(rs.getString("password"));
//        }
//
//        rs.close();
//        ps.close();
//        c.close();
//
//        // 예외 던지기
//        if (user == null) throw new EmptyResultDataAccessException(1);
//
//        return user;
//
//    }
//
//    public void deleteAll() throws SQLException {
////        Connection c = dataSource.getConnection();
////
////        PreparedStatement ps = c.prepareStatement("delete from users");
////        ps.executeUpdate();
////
////        ps.close();
////        c.close();
//
////        // 예외 발생시에도 리소스를 반환하도록 수정
////        Connection c = null;
////        PreparedStatement ps = null;
////
////        try {
////            c = dataSource.getConnection();
////            // 템플릿 메소드 패턴 적용
//////            ps = makeStatement(c);
////            // 전략 패턴 적용
////            StatementStrategy strategy = new DeleteAllStatement();
////            ps = strategy.makePreparedStatement(c);
////            ps.executeUpdate();
////        } catch (SQLException e) {
////            throw e;
////        } finally {
////            if (ps != null) {
////                try {
////                    ps.close();
////                } catch (SQLException e) {
////                    // close()를 하다가 예외가 발생할 경우
////                    // 보통 로그를 남기는 등의 부가작업이 필요할 수 있으니 일단 만들어둔다.
////                }
////            }
////            if (c != null) {
////                try {
////                    c.close();
////                } catch (SQLException e) {
////                    //close()를 하다가 예외가 발생할 경우
////                    // 보통 로그를 남기는 등의 부가작업이 필요할 수 있으니 일단 만들어둔다.
////                }
////            }
////        }
//
////        // 클라이언트 책임을 담당하도록 수정
////        // 위 코드에서 StatementStrategy를 DeleteAllStatement로 구현하는 코드만 빼낸 것.
////        StatementStrategy st = new DeleteAllStatement(); // 선정한 전략 클래스의 오브젝트 생성
////        jdbcContextWithStatementStrategy(st); // 컨텍스트 호출 전략 오브젝트 전달
//
//        // 익명 내부 클래스를 적용한 deleteAll() 메소드
//        jdbcContextWithStatementStrategy(
//            new StatementStrategy() {
//                @Override
//                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                    return c.prepareStatement("delete from users");
//                }
//            }
//        );
//
//    }
//
//    // 메소드로 분리한 try/catch/finally 컨텍스트 코드
//    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
//        Connection c = null;
//        PreparedStatement ps = null;
//
//        try {
//            c = dataSource.getConnection();
//            ps = stmt.makePreparedStatement(c);
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            throw e;
//        } finally {
//            if (ps != null) { try { ps.close(); } catch (SQLException e) {} }
//            if (c != null) { try { c.close(); } catch (SQLException e) {} }
//        }
//    }
//
//    private PreparedStatement makeStatement(Connection c) throws SQLException {
//        PreparedStatement ps;
//        ps = c.prepareStatement("delete from users");
//        return ps;
//    }
//
//    public int getCount() throws SQLException {
////        Connection c = dataSource.getConnection();
////
////        PreparedStatement ps = c.prepareStatement("select count(*) from users");
////
////        ResultSet rs = ps.executeQuery();
////        rs.next();
////        int count = rs.getInt(1);
////
////        rs.close();
////        ps.close();
////        c.close();
////
////        return count;
//
//        // JDBC 예외처리를 적용
//        Connection c = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            c = dataSource.getConnection();
//            ps = c.prepareStatement("select count(*) from users");
//
//            rs = ps.executeQuery();
//            rs.next();
//            return rs.getInt(1);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                }
//            }
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (SQLException e) {
//                }
//            }
//            if (c != null) {
//                try {
//                    c.close();
//                } catch (SQLException e) {
//                }
//            }
//        }
//
//    }
//
//}

/**
 * JdbcContext를 DI 받아서 사용하도록 만든 UserDao
 */
//public class UserDao {
//
//    private DataSource dataSource;
//    private JdbcContext jdbcContext;
//
//    // JdbcContext 생성과 DI 작업을 수행
//    public void setDataSource(DataSource dataSource) {
//        this.jdbcContext = new JdbcContext();
//        this.jdbcContext.setDataSource(dataSource);
//        this.dataSource = dataSource;
//    }
//
////    // JdbcContext DI
////    public void setJdbcContext(JdbcContext jdbcContext) {
////        this.jdbcContext = jdbcContext;
////    }
//
//    public void add(final User user) throws SQLException {
//        this.jdbcContext.workWithStatementStrategy(
//            new StatementStrategy() {
//                @Override
//                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
//                    PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
//                    ps.setString(1, user.getId());
//                    ps.setString(2, user.getName());
//                    ps.setString(3, user.getPassword());
//
//                    return ps;
//                }
//            }
//        );
//    }
//
////    public void deleteAll() throws SQLException {
////        this.jdbcContext.workWithStatementStrategy(
////            new StatementStrategy() {
////                @Override
////                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
////                    return c.prepareStatement("delete from users");
////                }
////            }
////        );
////    }
//
//    public void deleteAll() throws SQLException {
//        this.jdbcContext.executeSql("delete from users");
//    }
//
//     // 아래 메소드만 JdbcContext로 옮긴다.
////    private void executeSql(final String query) throws SQLException {
////        this.jdbcContext.workWithStatementStrategy(
////            new StatementStrategy() {
////                @Override
////                public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
////                    return c.prepareStatement(query);
////                }
////            }
////        );
////    }
//
//}

/**
 * 스프링의 JdbcTemplate을 사용
 */
//public class UserDao {
//
////    private DataSource dataSource;
//    private JdbcTemplate jdbcTemplate;
//    private RowMapper<User> userMapper =
//            new RowMapper<User>() {
//                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                    User user = new User();
//                    user.setId(rs.getString("id"));
//                    user.setName(rs.getString("name"));
//                    user.setPassword(rs.getString("password"));
//                    return user;
//                }
//            };
//
//    public void setDataSource(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
////        this.dataSource = dataSource;
//    }
//
////    public void add(final User user) {
////        this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
////                user.getId(), user.getName(), user.getPassword());
////    }
//
////    public void add(User user) throws DuplicateUserIdException {
////        try {
////            // JDBC를 이용해 User 정보를 DB에 추가하는 코드 또는
////            // 그런 기능을 가진 다른 SQLException을 던지는 메소드를 호출하는 코드
////        } catch (SQLException e) {
////            // ErrorCode가 MYSQL의 "Duplicate Entry(1062)"이면 예외 전환
////            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
////                throw new DuplicateUserIdException(e); // 예외 전환
////            } else {
////                throw new RuntimeException(e); // 예외 포장
////            }
////        }
////    }
//
//    public User get(String id) {
//        return this.jdbcTemplate.queryForObject("select * from users where id =?",
//            new Object[] {id}, this.userMapper
////            new RowMapper<User>() {
////                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
////                    User user = new User();
////                    user.setId(rs.getString("id"));
////                    user.setName(rs.getString("name"));
////                    user.setPassword(rs.getString("password"));
////                    return user;
////                }
////            }
//            );
//    }
//
//    public void deleteAll() {
//        this.jdbcTemplate.update("delete from users");
//    }
//
//    public int getCount() {
////        return this.jdbcTemplate.query(new PreparedStatementCreator() {
////            @Override
////            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
////                return con.prepareStatement("select count(*) from users");
////            }
////        }, new ResultSetExtractor<Integer>() {
////            @Override
////            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
////                rs.next();
////                return rs.getInt(1);
////            }
////        });
//        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
//    }
//
//    public List<User> getAll() {
//        return this.jdbcTemplate.query("select * from users order by id",
//            this.userMapper
////            new RowMapper<User>() {
////                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
////                    User user = new User();
////                    user.setId(rs.getString("id"));
////                    user.setName(rs.getString("name"));
////                    user.setPassword(rs.getString("password"));
////                    return user;
////                }
////            }
//            );
//    }
//
//}

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void add(User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    @Override
    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id =?",
                new Object[] {id}, this.userMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id",
                this.userMapper);
    }

    @Override
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }
}