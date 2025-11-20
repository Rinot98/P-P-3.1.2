package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserById(int id);

    User getUserByName(String name);

    void deleteUser(int id);

    void updateUser(User updatedUser);

    User getUserByLogin(String login);

    User getCurrentUserProfile();
}
