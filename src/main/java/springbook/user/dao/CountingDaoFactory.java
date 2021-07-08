//package springbook.user.dao;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * CountingConnectionMaker 의존관계가 추가된 DI 설정용 클래스
// * CountingConnectionMaker를 이용한 분석 작업이 모두 끝나면,
// * 다시 CountingDaoFactory 설정 클래스를 DaoFactory로 변경하거나
// * connectionMaker() 메소드를 수정하는 것만으로 DAO의 런타임 의존관계는 이전 상태로 복구된다.
// */
//@Configuration
//public class CountingDaoFactory {
//
//    @Bean
//    public UserDao userDao() {
//        return new UserDao(connectionMaker());
//    }
//
//    @Bean
//    public ConnectionMaker connectionMaker() {
//        return new CountingConnectionMaker(realConnectionMaker());
//    }
//
//    @Bean
//    public ConnectionMaker realConnectionMaker() {
//        return new DConnectionMaker();
//    }
//
//}
