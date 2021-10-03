package springbook.user.dao;

import springbook.user.domain.User;
import springbook.user.service.UserServiceImpl;

import java.util.List;

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

    public List<User> getAll() {
        for (User user : super.getAll()) {
            super.update(user); // 강제로 쓰기 시도 -> 읽기전용 속성으로 인한 예외가 발생해야 한다.
        }
        return null; // 메소드가 끝나기 전에 예외가 발생해야 하니 리턴 값은 별 의미 없다. 적당한 값을 넣어서 컴파일만 되게 한다.
    }

}
