package springbook.user.dao;

import springbook.user.domain.User;

/**
 * UserService에 DI를 통해 적용해보는 것은 간단한 작업이니
 * DI 적용 실습 차원에서 직접 만들어볼 것.
 */
public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);

}
