package springbook.learningtest.jdk;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.Locale;

public class UppercaseHandler implements InvocationHandler {

//    Hello target;
//
//    public UppercaseHandler(Hello target) {
//        this.target = target;
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        String ret = (String)method.invoke(target, args); // 타깃으로 위임. 인터페이스의 메소드 호출에 모두 적용된다.
//        return ret.toUpperCase(); // 부가기능 제공
//    }

    // 확장된 UppercaseHandler

    Object target; // 어떤 종류의 인터페이스를 구현한 타깃에도 적용 가능하도록 Object 타입으로 수정

    private UppercaseHandler(Object target) {
        this.target = target;
    }

//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        Object ret = method.invoke(target, args);
//        if (ret instanceof String) { // 호출한 메소드의 리턴 타입이 String인 경우만 대문자 변경 기능을 적용
//            return ((String)ret).toUpperCase();
//        } else {
//            return ret;
//        }
//    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if (ret instanceof String && method.getName().startsWith("say")) { // 리턴 타입과 메소드 이름이 일치하는 경우에만 부가기능 적용
            return ((String)ret).toUpperCase();
        } else {
            return ret; // 조건이 일치하지 않으면 타깃 오브젝트의 호출 결과를 그대로 리턴한다.
        }
    }

}
