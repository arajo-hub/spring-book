package springbook.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserLevelUpgradePolicy;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

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

    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
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

}