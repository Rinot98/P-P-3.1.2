package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    public List<User> getAllUsers();

    public void saveUser(User user);

    public User getUserById(int id);

    public User getUserByName(String name);

    public void deleteUser(int id);

    public void updateUser(User updatedUser);

    public User getUserByLogin(String login);

    public User getCurrentUserProfile();
}
