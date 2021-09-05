package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

/**
 * 이 인터페이스는 SimpleMailMessage라는 인터페이스를 구현한 클래스에 담긴
 * 메일 메시지를 전송하는 메소드로만 구성되어 있다.
 * 기본적으로는 JavaMail을 사용해 메일 발송 기능을 제공하는 JavaMailSenderImpl 클래스를 이용하면 된다.
 */
public interface MailSender {

    void send(SimpleMailMessage simpleMessage) throws MailException;
    void send(SimpleMailMessage[] simpleMessages) throws MailException;

}
