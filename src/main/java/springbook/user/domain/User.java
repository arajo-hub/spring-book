package springbook.user.domain;

/**
 * 1.1.1 User
 */
public class User {

    String id;
    String name;
    String password;
    String email;

    Level level; // enum
    int login;
    int recommend;

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    // 자바빈의 규약을 따르는 클래스에 생성자를 명시적으로 추가했을 때는
    // 파라미터가 없는 디폴트 생성자도 함께 정의해주는 것을 잊지 말자.
    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level getLevel() { return level; }

    public void setLevel(Level level) { this.level = level; }

    public int getLogin() { return login; }

    public void setLogin(int login) { this.login = login; }

    public int getRecommend() { return recommend; }

    public void setRecommend(int recommend) { this.recommend = recommend; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", level=" + level +
                ", login=" + login +
                ", recommend=" + recommend +
                '}';
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        } else {
            this.level = nextLevel;
        }
    }
}
