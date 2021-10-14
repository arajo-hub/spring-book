package springbook.user.sqlservice.updatable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import springbook.issuetracker.sqlservice.AbstractUpdatableSqlRegistryTest;
import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;
import springbook.issuetracker.sqlservice.updatable.EmbeddedDbSqlRegistry;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(HSQL).addScript(
                        "classpath:springbook/user/sqlservice/updatable/sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void transactionalUpdate() {
        checkFindResult("SQL1", "SQL2", "SQL3");

        HashMap<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY9999!@#$", "Modified9999");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
            // 예외가 발생해서 catch 블록으로 넘어가지 않으면 뭔가 잘못된 것이다.
            // 그때는 테스트를 강제로 실패하게 만들고 기대와 다르게 동작한 원인을 찾도록 해야 한다.
        } catch(SqlUpdateFailureException e) {
            // catch문에 내용 없음
        }
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

}
