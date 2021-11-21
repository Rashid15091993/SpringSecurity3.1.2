package web.dao;
import web.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

     User getUserByName(String s);

    void createUser(User user);

    void updateUser(User user);

    User readUser(long id);

    void deleteUser(User user);

    User getUserById(long id);
}
