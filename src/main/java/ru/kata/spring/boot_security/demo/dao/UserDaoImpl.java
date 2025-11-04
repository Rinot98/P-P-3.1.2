package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public void saveUser(User user) {
        em.persist(user);
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public User getUserByName(String name) {

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u " +
                "WHERE u.name = :username", User.class);
        query.setParameter("username", name);
        return query.getSingleResult();
    }

    @Override
    public void deleteUser(int id) {
        em.remove(getUserById(id));
    }

    @Override
    public void updateUser(User updatedUser) {
        em.merge(updatedUser);
    }

    @Override
    public User getUserByLogin(String login) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
    }

}
