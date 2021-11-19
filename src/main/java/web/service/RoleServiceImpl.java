package web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAllRole() {
        return roleDao.getAllUsers();
    }


    @Override
    public Role readRole(long id) {
        return roleDao.readRole(id);
    }

    @Override
    public Role deleteRole(long id) {
        Role role = null;
        try {
            role = roleDao.deleteRole(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return role;
    }

    @Override
    public void update(int id, Role role) {
        Role updaterole = readRole(id);
        updaterole.setRole(role.getRole());
    }

}
