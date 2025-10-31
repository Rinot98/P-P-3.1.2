package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import ru.kata.spring.boot_security.demo.entity.MyUser;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<MyUser> getAllUsers() {
        return em.createQuery("SELECT u FROM MyUser u", MyUser.class)
                .getResultList();
    }

    @Override
    public void saveUser(MyUser myUser) {
        if (myUser.getId() != 0) {
            em.merge(myUser);
        } else {
            em.persist(myUser);
        }
    }

    @Override
    public MyUser getUserById(int id) {
        return em.find(MyUser.class, id);
    }

    @Override
    public MyUser getUserByName(String name) {

        TypedQuery<MyUser> query = em.createQuery("SELECT u FROM MyUser u " +
                "WHERE u.name = :username", MyUser.class);
        query.setParameter("username", name);
        return query.getSingleResult();
    }

    @Override
    public void deleteUser(int id) {
        MyUser myUser = em.find(MyUser.class, id);
        em.remove(myUser);
    }

    @Override
    public MyUser getUserByLogin(String login) {
        try {
            return em.createQuery("SELECT u FROM MyUser u WHERE u.email = :login", MyUser.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }
    }

}
