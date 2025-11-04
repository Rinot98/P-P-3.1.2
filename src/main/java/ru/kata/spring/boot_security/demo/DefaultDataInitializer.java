package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DefaultDataInitializer implements CommandLineRunner {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound("ADMIN");
        createRoleIfNotFound("USER");
        createRoleIfNotFound("MANAGER");

        createUserIfNotFound("admin@admin.com", "admin", "Admin", "User", 30, "1234567890", Set.of("ADMIN", "MANAGER"));
        createUserIfNotFound("user@user.com", "user", "Regular", "User", 25, "0987654321", Set.of("USER"));
        createUserIfNotFound("manager@manager.com", "manager", "Project", "Manager", 35, "5555555555", Set.of("MANAGER", "USER"));
    }

    private void createRoleIfNotFound(String roleName) {
        if (roleService.getRoleByName(roleName) == null) {
            roleService.getOrCreateRole(roleName);
        }
    }

    private void createUserIfNotFound(String email, String password, String name, String surname, int age, String phone, Set<String> roleNames) {
        try {
            User existingUser = userService.getUserByLogin(email);
            if (existingUser == null) {
                createNewUser(email, password, name, surname, age, phone, roleNames);
            }
        } catch (UsernameNotFoundException e) {
            createNewUser(email, password, name, surname, age, phone, roleNames);
        }
    }

    private void createNewUser(String email, String password, String name, String surname, int age, String phone, Set<String> roleNames) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setAge(age);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPassword(password);

        Set<Role> roles = roleNames.stream()
                .map(roleService::getRoleByName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userService.saveUser(user);
    }
}