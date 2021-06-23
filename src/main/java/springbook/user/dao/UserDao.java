package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.*;

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

public class UserDao {

    private ConnectionMaker connectionMaker; // 인터페이스를 통해 오브젝트에 접근하므로 구체적인 클래스 정보를 알 필요가 없다.

//    public UserDao() {
//        connectionMaker = new DConnectionMaker();
//        // UserDao의 다른 모든 곳에서는 언터페이스를 이용하게 만들어서
//        // DB 커넥션을 제공하는 클래스에 대한 구체적인 정보는 모두 제거가 가능했지만,
//        // 초기에 한 번 어떤 클래스의 오브젝트를 사용할지를 결정하는 생성자의 코드는 제거되지 않고 남아 있다.
//        // 제거하고 싶은데 간단한 방법이 보이지 않는다.
//        // 클래스 이름을 넣어서 오브젝트를 만들지 않으면 어떻게 사용하란 말인가!
//        // 결국, 여전히 UserDao 소스코드를 함께 제공해서, 필요할 때마다 UserDao의 생성자 메소드를 직접 수정하라고 하지 않고는
//        // 고객에게 자유로운 DB 커넥션 확장 기능을 가진 UserDao를 제공할 수가 없다.
//    }

    // 생성자 수정
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection(); // 인터페이스에 정의된 메소드를 사용하므로 클래스가 바뀐다고 해도 메소드 이름이 변경될 걱정은 없다.

        PreparedStatement ps = c.prepareStatement(
            "insert into users(id, name, password) values (?, ?, ?)");

        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}