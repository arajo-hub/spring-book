package springbook.user.dao;

import springbook.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {

    private List<User> users;
    private List<User> updated = new ArrayList();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    public List<User> getUpdated() {
        return this.updated;
    }

    public List<User> getAll() {
        return this.users;
    }

    public void update(User user) {
        updated.add(user);
    }

    /**
     * 아래는 테스트에 사용되지 않는 메소드
     */

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }

}
