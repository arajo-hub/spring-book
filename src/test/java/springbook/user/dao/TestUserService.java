package springbook.user.dao;

import springbook.user.domain.User;
import springbook.user.service.UserService;

public class TestUserService extends UserService {

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
