package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.configs.PasswordUtil;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.entity.MyUser;
import org.springframework.security.core.userdetails.User;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;
import java.util.Collection;
import java.util.Objects;
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
    public List<MyUser> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void saveUser(MyUser myUser) {
        Set<Role> managedRoles = myUser.getRoles().stream()
                .map(role -> roleService.getRoleByName(role.getRole()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        myUser.setRoles(managedRoles);
        if (myUser.getPassword() == null || myUser.getPassword().isEmpty()) {
            myUser.setPassword(userDao.getUserById(myUser.getId()).getPassword());
        } else {
            myUser.setPassword(passwordEncoder.encodePassword(myUser.getPassword()));
        }
        userDao.saveUser(myUser);
    }

    @Override
    public MyUser getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public MyUser getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }

    @Override
    public MyUser getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        MyUser myUser = userDao.getUserByLogin(login);

        Collection<? extends GrantedAuthority> authorities = myUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());

        return User.builder()
                .username(myUser.getEmail())
                .password(myUser.getPassword())
                .authorities(authorities)
                .build();
    }
}
