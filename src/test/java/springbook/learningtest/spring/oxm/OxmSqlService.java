package springbook.learningtest.spring.oxm;

import springbook.user.dao.UserDao;
import springbook.user.sqlservice.*;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService {
    private final BaseSqlService baseSqlService = new BaseSqlService();
    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    // OxmSqlReader는 private 멤버 클래스이므로 외부에서 접근하거나 사용할 수 없다.
    // 또한 OxmSqlService는 이를 final로 선언하고 직접 오브젝트를 생성하기 때문에
    // OxmSqlReader를 DI하거나 변경할 수 없다.
    // 이렇게 두 개의 클래스를 강하게 결합하고 더 이상의 확장이나 변경을 제한해두는 이유는 무엇일까?
    // 그것은 OXM을 이용하는 서비스 구조로 최적화하기 위해서다.
    // 하나의 클래스로 만들어두기 때문에 빈의 등록과 설정은 단순해지고 쉽게 사용할 수 있다.

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlmapFile(String sqlmapFile) {
        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    @PostConstruct
    public void loadSql() {
//        this.oxmSqlReader.read(this.sqlRegistry);
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);

        this.baseSqlService.loadSql();
    }

    public String getSql(String key) throws SqlRetrievalFailureException {
//        try {
//            return this.sqlRegistry.findSql(key);
//        } catch (SqlNotFoundException e) {
//            throw new SqlRetrievalFailureException(e);
//        }
        return this.baseSqlService.getSql(key);
    }

    private class OxmSqlReader implements SqlReader {
        private Unmarshaller unmarshaller;
        private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
        private String sqlmapFile = DEFAULT_SQLMAP_FILE;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);

                for (SqlType sql : sqlmap.getSql()) {
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmapFile + "을 가져올 수 없습니다", e);
            }
        }
    }
}
