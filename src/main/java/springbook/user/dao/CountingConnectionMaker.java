package springbook.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 연결횟수 카운팅 기능이 있는 클래스
 */
public class CountingConnectionMaker implements ConnectionMaker {

    int counter = 0;
    private ConnectionMaker realConnectionMaker;

    public CountingConnectionMaker(ConnectionMaker realConnectionMaker) {
        this.realConnectionMaker = realConnectionMaker;
    }

    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        // CountingConnectionMaker는 자신의 관심사인 DB 연결횟수 카운팅 작업을 마치면
        // 실제 DB 커넥션을 만들어주는 realConnectionMaker에 저장된
        // ConnectionMaker 타입 오브젝트의 makeConnection()을 호출해서 그 결과를 DAO에게 돌려준다.
        this.counter++;
        return realConnectionMaker.makeConnection();
    }

    public int getCounter() {
        return this.counter;
    }

}
