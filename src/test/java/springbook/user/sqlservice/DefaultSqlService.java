package springbook.user.sqlservice;

public class DefaultSqlService extends BaseSqlService {
    // DI 설정이 없을 경우 디폴트로 적용하고 싶은 의존 오브젝트를 생성자에서 넣어준다.
    // DI란 클라이언트 외부에서 의존 오브젝트를 주입해주는 것이지만
    // 이렇게 자신이 사용할 디폴트 의존 오브젝트를 스스로 DI 하는 방법도 있다.
    // 이렇게 코드를 통해 의존관계의 오브젝트를 직접 주입해주면 특별히 DI가 필요한 상황이 아닌 대부분의 경우에는 편리하게 사용할 수 있다.
    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }
}
