package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> getAllRoles();

    Role getRoleById(int id);

    Role getRoleByName(String roleName);

    void saveRole(Role role);

    void deleteRole(int id);
}