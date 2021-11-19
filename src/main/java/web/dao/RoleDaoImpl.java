package web.dao;

import org.springframework.stereotype.Repository;
import web.model.Role;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Role> getAllUsers() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    @Override
    public Role getRoleByName(String s) {
        return entityManager.createQuery(
                        "SELECT role FROM Role role WHERE role.role =:username", Role.class)
                .setParameter("username", s)
                .getSingleResult();
    }


    @Override
    public Role readRole(long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role deleteRole(long id) throws NullPointerException {
        Role role = readRole(id);
        if (null == role) {
            throw new NullPointerException("User not found");
        }
        entityManager.remove(role);
        entityManager.flush();
        return role;
    }
}

