//package springbook.user.dao;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DUserDao extends UserDao{
//
//    public Connection getConnection() throws ClassNotFoundException, SQLException {
//        // D사 DB Connection 생성 코드
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection c = DriverManager.getConnection(
//                "jdbc:mysql://localhost/springbook", "spring", "book");
//        return c;
//    }
//
//}
