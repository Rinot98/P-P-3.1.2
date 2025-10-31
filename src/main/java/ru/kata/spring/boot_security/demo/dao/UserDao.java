package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.MyUser;

import java.util.List;

public interface UserDao {
    public List<MyUser> getAllUsers();

    public void saveUser(MyUser myUser);

    public MyUser getUserById(int id);

    public MyUser getUserByName(String name);

    public void deleteUser(int id);

    public MyUser getUserByLogin(String username);
}
