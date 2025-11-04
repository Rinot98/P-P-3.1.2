package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getAllRoles();

    Role getRoleById(int id);

    Role getRoleByName(String roleName);

    void deleteRole(int id);

    public Role getOrCreateRole(String roleName);
}