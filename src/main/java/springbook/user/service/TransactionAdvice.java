package springbook.user.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {

    PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = invocation.proceed(); // DefaultTransactionDefinition과 여기서 rollback 대상인 예외가 결합해서 트랜잭션 부가기능의 행동을 결정하는 TransactionAttribute 속성이 된다.
            this.transactionManager.commit(status);
            return ret;
        } catch (RuntimeException e) {
            // TransactionAdvice는 RuntimeException이 발생하는 경우에만 트랜잭션을 롤백시킨다.
            // 하지만 런타임 예외가 아닌 경우에는 트랜잭션이 제대로 처리되지 않고 메소드를 빠져나가게 되어 있다.
            // UserService는 런타임 예외만 던진다는 사실을 알기 때문에 일단 이렇게 정의해도 상관없지만,
            // 체크 예외를 던지는 타깃에 사용한다면 문제가 될 수 있다.
            // 그렇다면 런타임 예외만이 아니라 모든 종류의 예외에 대해 트랜잭션을 롤백시키도록 해야 할까?
            // 그래서는 안 된다. 비즈니스 로직상의 예외 경우를 나타내기 위해 타깃 오브젝트가 체크 예외를 던지는 경우에는 DB 트랜잭션은 커밋시켜야 하기 때문이다.
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
