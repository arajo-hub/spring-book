package springbook.user.sqlservice;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

//public class XmlSqlService implements SqlService {

//    private Map<String, String> sqlMap = new HashMap<String, String>();
//
//    private String sqlmapFile; // 파일 이름을 외부에서 지정할 수 있도록 프로퍼티 추가
//
////    public XmlSqlService() {
////        String contextPath = Sqlmap.class.getPackage().getName();
////        try {
////            JAXBContext context = JAXBContext.newInstance(contextPath);
////            Unmarshaller unmarshaller = context.createUnmarshaller();
////            InputStream is = UserDao.class.getResourceAsStream("sqlmap.xml");
////            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
////
////            for (SqlType sql : sqlmap.getSql()) {
////                sqlMap.put(sql.getKey(), sql.getValue());
////            }
////        } catch (JAXBException e) {
////            throw new RuntimeException(e);
////        }
////
////        // 위처럼 생성자에서 예외가 발생할 수도 있는 복잡한 초기화 작업을 다루는 것은 좋지 않다.
////        // 오브젝트를 생성하는 중에 생성자에서 발생하는 예외는 다루기 힘들고,
////        // 상속하기 불편하며, 보안에도 문제가 생길 수 있다.
////        // 일단 초기 상태를 가진 오브젝트를 만들어놓고 별도의 초기화 메소드를 사용하는 방법이 바람직하다.
////
////        // 또 다른 문제점으로는 읽어들일 파일의 위치와 이름이 코드에 고정되어 있다는 점을 들 수 있다.
////        // SQL을 담은 XML 파일의 위치와 이름을 코드에 고정하는 건 별로 좋은 생각이 아니다.
////        // 코드의 로직과 여타 이유로 바뀔 가능성이 있는 내용은 외부에서 DI로 설정해줄 수 있게 만들어야 한다.
////    }
//
//    public void setSqlmapFile(String sqlmapFile) {
//        this.sqlmapFile = sqlmapFile;
//    }
//
//    public String getSql(String key) throws SqlRetrievalFailureException {
//        String sql = sqlMap.get(key);
//        if (sql == null)
//            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
//        else
//            return sql;
//    }
//
//    @PostConstruct // loadSql() 메소드를 빈의 초기화 메소드로 지정한다.
//    public void loadSql() {
//        String contextPath = Sqlmap.class.getPackage().getName();
//
//        try {
//            JAXBContext context = JAXBContext.newInstance(contextPath);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
//            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
//
//            for (SqlType sql : sqlmap.getSql()) {
//                sqlMap.put(sql.getKey(), sql.getValue());
//            }
//        } catch (JAXBException e) {
//            throw new RuntimeException(e);
//        }
//
//        // 이제 외부에서 XML 파일을 지정할 수 있고, 이를 이용해 SQL을 읽어들이는 초기화 작업을 담당할 메소드도 별도로 만들어뒀다.
//        // sqlmapFile 프로퍼티는 빈 설정의 <property> 태그를 이용해 지정해주면 된다.
//        // 하지만 loadSql()이라는 초기화 메소드는 언제 실행돼야 할까? 또, 어떻게 실행시킬 수 있을까?
//        // 이 XmlSqlService 오브젝트에 대한 제어권이 우리가 만드는 코드에 있다면, 오브젝트를 만드는 시점에서 초기화 메소드를 한 번 호출해주면 된다.
//        // 그러나 XmlSqlService 오브젝트는 빈이므로 제어권이 스프링에 있다.
//        // 생성은 물론이고 초기화도 스프링에게 맡길 수 밖에 없다.
//        // 그래서 스프링은 빈 오브젝트를 생성하고 DI 작업을 수행해서 프로퍼티를 모두 주입해준 뒤에 미리 지정한 초기화 메소드를 호출해주는 기능을 갖고 있다.
//        // AOP를 살펴볼 때 스프링의 빈 후처리기에 대해 설명했다.
//        // 빈 후처리기는 스프링 컨테이너가 빈을 생성한 뒤에 부가적인 작업을 수행할 수 있게 해주는 특별한 기능이다.
//        // AOP를 위한 프록시 자동생성기가 대표적인 빈 후처리기다. 프록시 자동생성기 외에도 스프링이 제공하는 여러 가지 빈 후처리기가 존재한다.
//        // 그중에서 애노테이션을 이용한 빈 설정을 지원해주는 몇 가지 빈 후처리기가 있다.
//        // 이 빈 후처리기는 <bean> 태그를 이용해 하나씩 등록할 수도 있지만, 그보다는 context 스키마의 annotation-config 태그를 사용하면 더 편리하다.
//        // context 네임스페이스를 사용해서 <context:annotation-confg/> 태그를 만들어 설정파일에 넣어주면 빈 설정 기능에 사용할 수 있는 특별한 애노테이션 기능을 부여해주는 빈 후처리기들이 등록된다.
//
//    }

//}

// SqlReader, SqlService, SqlRegistry 구현
public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    // SqlService
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    // SqlRegistry
    private Map<String, String> sqlMap = new HashMap<String, String>(); // sqlMap은 SqlRegistry 구현의 일부가 된다. 따라서 외부에서 직접 접근할 수 없다.

    // SqlReader
    private String sqlmapFile;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.sqlmapFile = sqlmapFile;
    }

    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null) throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        else return sql;
    }

    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);
            for (SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch(SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

}