package org.springframework.beans.factory;

public interface FactoryBean<T> {

    T getObject() throws Exception; // 빈 오브젝트 생성해서 돌려준다.
    Class<? extends T> getObjectType(); // 생성되는 오브젝트의 타입을 알려준다.
    boolean isSingleton(); // getObject()가 돌려주는 오브젝트가 항상 같은 싱글톤 오브젝트인지 알려준다.

}
