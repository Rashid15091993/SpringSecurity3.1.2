package web.dao;
import web.model.Role;
import web.model.User;

import java.util.List;

public interface RoleDao {
    List<Role> getAllUsers();

    Role getRoleByName(String s);

    Role readRole(long id);

    Role deleteRole(long id);
}
