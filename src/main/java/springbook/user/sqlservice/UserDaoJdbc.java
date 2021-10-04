package springbook.user.sqlservice;

import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.util.List;
import java.util.Map;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private UserMapper userMapper;

//    private String sqlAdd;
//
//    public void setSqlAdd(String sqlAdd) {
//        this.sqlAdd = sqlAdd;
//    }

//    private Map<String, String> sqlMap;

//    public void setSqlMap(Map<String, String> sqlMap) {
//        this.sqlMap = sqlMap;
//    }

    private SqlService sqlService;

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

//    @Override
//    public void add(User user) {
//        this.jdbcTemplate.update(
//                this.sqlAdd,
//                user.getId(), user.getName(), user.getPassword(), user.getEmail(),
//                user.getLevel().intValue(), user.getLogin(), user.getRecommend());
//    }

    @Override
    public void add(User user) {
        this.jdbcTemplate.update(
//                this.sqlMap.get("add"),
                this.sqlService.getSql("userAdd"),
                user.getId(), user.getName(), user.getPassword(), user.getEmail(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public User get(String id) {
        return this.jdbcTemplate.queryForObject(this.sqlService.getSql("userGet"),
                new Object[] {id}, this.userMapper);
    }

    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(this.sqlService.getSql("userGetAll"),
                this.userMapper);
    }

    @Override
    public void deleteAll() {
        this.jdbcTemplate.update(this.sqlService.getSql("userDeleteAll"));
    }

    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForInt(this.sqlService.getSql("userGetCount"));
    }

    @Override
    public void update(User user1) {
        this.jdbcTemplate.update(this.sqlService.getSql("userUpdate"),
                user.getName(), user1.getPassword(), user.getEmail(),
                user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
                user.getId());
    }
}
