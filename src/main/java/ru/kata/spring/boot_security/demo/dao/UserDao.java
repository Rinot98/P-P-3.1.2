package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserDao {
    public List<User> getAllUsers();

    public void saveUser(User user);

    public User getUserById(int id);

    public User getUserByName(String name);

    public void deleteUser(int id);

    public void updateUser(User updatedUser);

    public User getUserByLogin(String username);
}
