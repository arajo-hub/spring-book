package springbook.learningtest.jdk.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import springbook.learningtest.jdk.Hello;
import springbook.learningtest.jdk.HelloTarget;
import springbook.learningtest.jdk.UppercaseHandler;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

public class DynamicProxyTest {

    @Test
    public void simpleProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget())); // JDK 다이내믹 프록시 생성

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); // 타깃 설정
        pfBean.addAdvice(new UppercaseAdvice()); // 부가기능을 담은 어드바이스를 여러 개 추가할 수도 있다.

        Hello proxiedHello = (Hello) pfBean.getObject(); // Factory Bean이므로 getObject()로 생성된 프록시를 가져온다.
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String)invocation.proceed(); // 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다. MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문이다.
            return ret.toUpperCase();
        }
    }

    static interface Hello { // 타깃과 프록시가 구현할 인터페이스
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    static class HelloTarget implements Hello { // 타깃 클래스
        public String sayHello(String name) { return "Hello " + name; }
        public String sayHi(String name) { return "Hi " + name; }
        public String sayThankYou(String name) { return "Thank You " + name; }
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello)pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }

}
