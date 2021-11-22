package web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import web.model.Role;
import web.model.User;
import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    List<Role> findAllRoles();

    User readUser(long id);

    void deleteUser(long parseUnsignedInt);

    void createOrUpdateUser(User user);

    void createUser(User user);

    void update(int id, User user);

    void updateUser(User user);
}
