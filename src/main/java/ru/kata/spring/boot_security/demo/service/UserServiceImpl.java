package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.config.PasswordUtil;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordUtil passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordUtil passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void saveUser(User user) {
        prepareUserForSave(user);
        userDao.saveUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    public void updateUser(User user) {
        prepareUserForSave(user);
        userDao.updateUser(user);
    }

    private void prepareUserForSave(User user) {
        processRoles(user);
        processPassword(user);
    }

    private void processRoles(User user) {
        Set<Role> resolvedRoles = user.getRoles().isEmpty() && user.getId() == 0
                ? Set.of(roleService.getOrCreateRole("ROLE_USER"))
                : resolveRoles(user.getRoles());

        user.setRoles(resolvedRoles);
    }

    private Set<Role> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> roleService.getOrCreateRole(role.getRole()))
                .collect(Collectors.toSet());
    }

    private void processPassword(User user) {
        if (isPasswordEmpty(user.getPassword()) && user.getId() != 0) {
            user.setPassword(getExistingPassword(user.getId()));
        } else if (!isPasswordEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encodePassword(user.getPassword()));
        }
    }

    private boolean isPasswordEmpty(String password) {
        return password == null || password.trim().isEmpty();
    }

    private String getExistingPassword(int userId) {
        User existingUser = userDao.getUserById(userId);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found with id: " + userId);
        }
        return existingUser.getPassword();
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return getUserByLogin(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDao.getUserByLogin(login);

        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
