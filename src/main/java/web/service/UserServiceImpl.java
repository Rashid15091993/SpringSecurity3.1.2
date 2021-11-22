package web.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.RoleDao;
import web.dao.UserDao;
import web.model.Role;
import web.model.User;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<Role> findAllRoles() {
        return roleDao.findAll();
    }

    @Transactional
    @Override
    public void createOrUpdateUser(User user) {
        if (0 == user.getId()) {
            createUser(user);
        } else {
            updateUser(user);
        }
    }

    @Transactional
    @Override
    public void createUser(User user) {
        userDao.createUser(user);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Transactional
    @Override
    public User readUser(long id) {
        return userDao.readUser(id);
    }

    @Transactional
    @Override
    public void deleteUser(long id) {
        Optional<User> user = Optional.ofNullable(userDao.getUserById(id));
        if (user.isPresent()) {
            try {
                userDao.deleteUser(user.get());
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    @Override
    public void update(int id, User user) {
        User updateuser = readUser(id);
        updateuser.setName(user.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getUserByName(s);
    }
}
