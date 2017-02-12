package app.user;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();
    List<User> getAllUsersForRole(String username, int role);

    User getUserByUsername(String username);

    Iterable<String> getAllUserNames();

    boolean createUser(User user);

    boolean updateOpengameId(User user, int opengameId);

    int getLatestOpengameId(String username);

    boolean updateUser(User user);

}
