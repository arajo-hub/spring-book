package springbook.user.dao;

public class DaoFactory {

    // UserDao 타입의 오브젝트를 어떻게 만들고, 어떻게 준비시킬지를 결정한다.
    // DaoFactory의 userDao 메소드를 호출하면 DConnectionMaker를 사용해
    // DB 커넥션을 가져오도록 이미 설정된 UserDao 오브젝트를 돌려준다.
    // UserDaoTest는 이제 UserDao가 어떻게 만들어지는지 어떻게 초기화되어 있는지에 신경 쓰지 않고
    // 팩토리로부터 UserDao 오브젝트를 받아다가, 자신의 관심사인 테스트를 위해 활용하기만 하면 그만이다.
    public UserDao userDao() {
//        return new UserDao(connectionMaker);
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
//        return new AccountDao(new DConnectionMaker());
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
//        return new MessageDao(new DConnectionMaker());
        return new MessageDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

}
