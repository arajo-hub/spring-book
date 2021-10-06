package springbook.user.sqlservice;

public interface SqlRegistry {
    void registerSql(String key, String sql); // SqlReader는 읽어들인 SQL을 이 메소드를 이용해 레지스트리에 저장한다.

    String findSql(String key) throws SqlNotFoundException; // 키로 SQL을 검색한다. 검색이 실패하면 예외를 던진다.
}
