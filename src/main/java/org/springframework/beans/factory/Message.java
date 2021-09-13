package org.springframework.beans.factory;

public class Message {

    String text;

    private Message(String text) { // private이라 외부에서 생성 X
        this.text = text;
    }

    public String getText() {
        return text;
    }

    // 생성자 대신 사용할 수 있는 스태틱 팩토리 메소드를 제공
    public static Message newMessage(String text) {
        return new Message(text);
    }

}
