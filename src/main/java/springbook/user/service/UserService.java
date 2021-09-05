package springbook.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserLevelUpgradePolicy;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import sun.jvm.hotspot.debugger.AddressException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;

////public class UserService {
////
////    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
////    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
////
////    UserDao userDao;
////
////    // 트랜잭션 동기화 방식을 적용
////    // Connection을 생성할 때 사용할 DataSource를 DI받도록 한다.
////    private DataSource dataSource;
////
////    public void setDataSource(DataSource dataSource) {
////        this.dataSource = dataSource;
////    }
////
////    public void setUserDao(UserDao userDao) {
////        this.userDao = userDao;
////    }
////
//////    public void upgradeLevels() {
//////        List<User> users = userDao.getAll();
//////        for (User user : users) {
//////            Boolean changed = null;
//////            if (canUpgradeLevel(user)) {
//////                upgradeLevel(user);
//////            }
//////        }
//////    }
////
////    public void upgradeLevels() throws Exception {
//////        // 트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화한다.
//////        TransactionSynchronizationManager.initSynchronization();
//////        // DB 커넥션을 생성하고 트랜잭션을 시작한다. 이후의 DAO 작업은 모두 여기서 시작한 트랜잭션 안에서 진행된다.
//////        Connection c = DataSourceUtils.getConnection(dataSource);
//////        c.setAutoCommit(false);
//////
//////        try {
//////            List<User> users = userDao.getAll();
//////            for (User user : users) {
//////                if (canUpgradeLevel(user)) {
//////                    upgradeLevel(user);
//////                }
//////            }
//////            // 정상적으로 작업을 마치면 트랜잭션 커밋
//////            c.commit();
//////        } catch (Exception e) {
//////            // 예외가 발생하면 롤백한다.
//////            c.rollback();
//////            throw e;
//////        } finally {
//////            // 스프링 유틸리티 메소드를 이용해 DB 커넥션을 안전하게 닫는다.
//////            DataSourceUtils.releaseConnection(c, dataSource);
//////            // 동기화 작업 종료 및 정리
//////            TransactionSynchronizationManager.unbindResource(this.dataSource);
//////            TransactionSynchronizationManager.clearSynchronization();
////
////        // 스프링의 트랜잭션 추상화 API를 적용
////        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource); // JDBC 트랜잭션 추상 오브젝트 생성
////
////        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
////
////        try {
////            List<User> users = userDao.getAll();
////            for (User user : users) {
////                if (canUpgradeLevel(user)) {
////                    upgradeLevel(user);
////                }
////            }
////            transactionManager.commit(status);
////        } catch (RuntimeException e) {
////            transactionManager.rollback(status);
////            throw e;
////        }
////    }
////
////    private boolean canUpgradeLevel(User user) {
////        Level currentLevel = user.getLevel();
////        switch (currentLevel) {
////            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
////            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
////            case GOLD: return false;
////            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
////        }
////    }
////
////    protected void upgradeLevel(User user) {
////        user.upgradeLevel();
////        userDao.update(user);
////    }
////
////    public void add(User user) {
////        if (user.getLevel() == null) {
////            user.setLevel(Level.BASIC);
////        }
////        userDao.add(user);
////    }
////}
//
///**
// * 트랜잭션 매니저를 빈으로 분리시킨 UserService
// */
//public class UserService {
//
//    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
//    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
//
//    UserDao userDao;
//
//    private PlatformTransactionManager transactionManager;
//
//    public void setTransactionManager(PlatformTransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }
//
//    public void upgradeLevels() {
////        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
////        try {
////            List<User> users = userDao.getAll();
////            for (User user : users) {
////                if (canUpgradeLevel(user)) {
////                    upgradeLevel(user);
////                }
////            }
////            this.transactionManager.commit(status);
////        } catch (RuntimeException e) {
////            this.transactionManager.rollback(status);
////            throw e;
////        }
//        // 스프링의 트랜잭션 추상화 API를 적용
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
//
//        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
//
//        try {
//            List<User> users = userDao.getAll();
//            for (User user : users) {
//                if (canUpgradeLevel(user)) {
//                    upgradeLevel(user);
//                }
//            }
//            transactionManager.commit(status);
//        } catch (RuntimeException e) {
//            transactionManager.rollback(status);
//            throw e;
//        }
//
//        // JTA를 이용하는 글로벌 트랜잭션으로 변경하려면
//        // PlatformTransactionManager transactionManager = JtaTransactionManager(dataSource);
//
//        //
//    }
//
//    private boolean canUpgradeLevel(User user) {
//        Level currentLevel = user.getLevel();
//        switch (currentLevel) {
//            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
//            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
//            case GOLD: return false;
//            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
//        }
//    }
//
//    protected void upgradeLevel(User user) {
//        user.upgradeLevel();
//        userDao.update(user);
//    }
//
//}

public class UserService {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void upgradeLevels() {
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            this.transactionManager.commit(status);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(status);
            throw e;
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

//    // JavaMail을 이용한 메일 발송
//    private void sendUpgradeEMail(User user) {
//        Properties props = new Properties();
//        props.put("mail.smtp.host", "mail.ksug.org");
//        Session s = Session.getInstance(props, null);
//
//        MimeMessage message = new MimeMessage(s);
//        try {
//            message.setFrom(new InternetAddress("useradmin@ksug.org"));
//            message.addRecipient(Message.RecipientType.TO,
//                                    new InternetAddress(user.getEmail()));
//            message.setSubject("Upgrade 안내");
//            message.setText("사용자 님의 등급이 " + user.getLevel().name() +
//                    "로 업그레이드되었습니다");
//            Transport.send(message);
//        } catch (AddressException e) {
//            throw new RuntimeException(e);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    // 스프링의 MailSender를 이용한 메일 발송
    private void sendUpgradeEMail(User user) {
        // JavaMailSenderImpl은 내부적으로 JavaMail API를 이용해 메일을 전송해준다.
        // 복잡하고 지저분해 보이는 JavaMail API를 사용했던 경우에 비해 코드가 간결해졌다.
        // 하지만 아직은 JavaMail API를 사용하지 않는 테스트용 오브젝트로 대체할 수는 없다.
        // JavaMail API를 사용하는 JavaMailSenderImpl 클래스의 오브젝트를 코드에서 직접 사용하기 때문이다.
        // 그렇다면, 이제 스프링의 DI를 적용할 차례다.
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("mail.server.com");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name()
                            + "로 업그레이드되었습니다.");
        mailSender.send(mailMessage);
    }

    // 메일 전송 기능을 가진 오브젝트를 DI 받도록 수정
    private void sendUpgradeMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

        this.mailSender.send(mailMessage);
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

}