package org.springframework.beans.factory;

public class MessageFactoryBean implements FactoryBean<Message> {

    String text;

    public void setText(String text) { // 오브젝트 생성시 필요한 정보를 팩토리 빈의 프로퍼티로 설정해서 대신 DI 받을 수 있게.
        this.text = text;
    }

    @Override
    public Message getObject() throws Exception { // 실제 빈으로 사용될 오브젝트를 직접 생성
        return Message.newMessage(this.text);
    }

    @Override
    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
