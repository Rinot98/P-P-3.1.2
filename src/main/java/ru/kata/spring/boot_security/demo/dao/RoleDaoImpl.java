package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Set<Role> getAllRoles() {
        List<Role> roles = em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        return new HashSet<>(roles);
    }

    @Override
    public Role getRoleById(int id) {
        return em.find(Role.class, id);
    }

    @Override
    public Role getRoleByName(String roleName) {
        try {
            return em.createQuery("SELECT r FROM Role r WHERE r.role = :roleName", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void saveRole(Role role) {
        em.persist(role);
    }

    @Override
    public void deleteRole(int id) {
        Role role = em.find(Role.class, id);
        if (role != null) {
            em.remove(role);
        }
    }
}