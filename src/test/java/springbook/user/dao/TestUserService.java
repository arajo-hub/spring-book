package springbook.user.dao;

import springbook.user.domain.User;
import springbook.user.service.UserServiceImpl;

public class TestUserService extends UserServiceImpl {

    private String id;

    TestUserService(String id) {
        this.id = id;
    }

    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) {
            throw new TestUserServiceException();
        }

        super.upgradeLevel(user);
    }

}
