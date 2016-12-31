package app.user;


import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.stream.Collectors;

public class UserDaoImpl implements UserDao{

    private Sql2o sql2o;
    private List<User> users;

    public UserDaoImpl(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<User> getAllUsers() {

        try (Connection conn = sql2o.open()) {
            users = conn.createQuery("SELECT * FROM users")
                    .executeAndFetch(User.class);
            return users;
        }
    }

    @Override
    public User getUserByUsername(String username) {

        System.out.println("username: " + username);
        return users.stream().filter(b -> b.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public Iterable<String> getAllUserNames() {
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    @Override
    public boolean createUser(User user) {

        String sql =
                "INSERT INTO users(username, firstname, lastname, salt, hashedpassword, role, openGameId) " +
                        "VALUES (:username, :firstname, :lastname, :salt, :hashedpassword, :role, :openGameId)";

        try (Connection con = sql2o.open()) {
            con.setRollbackOnException(false);
            con.createQuery(sql)
                    .addParameter("username", user.getUsername())
                    .addParameter("firstname", user.getFirstname())
                    .addParameter("lastname", user.getLastname())
                    .addParameter("salt", user.getSalt())
                    .addParameter("hashedpassword", user.getHashedPassword())
                    .addParameter("role", user.getRole())
                    .addParameter("openGameId", user.getOpenGameId())
                    .executeUpdate();
            return true;
        }
        catch (Exception e){
            System.out.println(e.toString());
            return false;
        }

    }

    @Override
    public boolean updateOpengameId(User user, int opengameId) {

        String updateSql = "UPDATE users SET openGameId = :openGameId WHERE id= " + user.getId()+";";
        System.out.println("sqlString:  " + updateSql);
        try (Connection con = sql2o.open()) {
            con.createQuery(updateSql)
                    .addParameter("openGameId", opengameId)
                    .executeUpdate();
        }

        getAllUsers();
        return true;
    }

    @Override
    public int getLatestOpengameId(String username) {

        int lastOpengameId;

        String sql = "SELECT opengameId FROM games " +
                " WHERE userId =" + getUserByUsername(username).getId() +
                " ORDER BY id DESC LIMIT 1;";

        try (Connection con = sql2o.open()) {

            lastOpengameId = Integer.parseInt(con.createQuery(sql).executeScalar().toString());

        }catch (Exception e){
            return 0;
        }

        return lastOpengameId;
    }

    @Override
    public boolean updateUser(User user) {

        String updateSql = "UPDATE users SET " +
                "firstname = :firstname, " +
                "lastname= :lastname, " +
                "salt = :salt, " +
                "hashedPassword = :hashedPassword, " +
                "role = :role " +
                "WHERE username = :username";

        try (Connection con = sql2o.open()) {
            con.createQuery(updateSql)
                    .addParameter("username", user.getUsername())
                    .addParameter("firstname", user.getFirstname())
                    .addParameter("lastname", user.getLastname())
                    .addParameter("salt", user.getSalt())
                    .addParameter("hashedPassword", user.getHashedPassword())
                    .addParameter("role", user.getRole())
                    .executeUpdate();
            return true;

        }catch (Exception e){
            return false;
        }
    }
}
