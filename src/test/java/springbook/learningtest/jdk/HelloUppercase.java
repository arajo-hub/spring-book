package springbook.learningtest.jdk;

public class HelloUppercase implements Hello {
    Hello hello;
    // 위임할 타깃 오브젝트.
    // 여기서는 타깃 클래스의 오브젝트인 것은 알지만
    // 다른 프록시를 추가할 수도 있으므로 인터페이스로 접근한다.

    public HelloUppercase(Hello hello) {
        this.hello = hello;
    }

    public String sayHello(String name) {
        return hello.sayHello(name).toUpperCase();
    }

    public String sayHi(String name) {
        return hello.sayHi(name).toUpperCase();
    }

    public String sayThankYou(String name) {
        return hello.sayThankYou(name).toUpperCase();
    }
}
