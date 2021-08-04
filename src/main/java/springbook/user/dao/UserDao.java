package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.Connection;
import java.util.List;

public interface UserDao {

    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user1);

//    void add(Connection c, User user);
//    User get(Connection c, String id);
//    List<User> getAll(Connection c);
//    void deleteAll(Connection c);
//    int getCount(Connection c);
//    void update(Connection c, User user1);
}
