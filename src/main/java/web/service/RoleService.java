package web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import web.model.Role;
import web.model.User;

import javax.validation.Valid;
import java.util.List;

public interface RoleService {

    List<Role> getAllRole();

    Role readRole(long id);

    Role deleteRole(long parseUnsignedInt);

    void update(int id, Role role);
}
