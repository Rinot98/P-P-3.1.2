package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Set;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleById(int id) {
        return roleDao.getRoleById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleByName(String roleName) {
        return roleDao.getRoleByName(roleName);
    }

    @Override
    public Role getOrCreateRole(String roleName) {
        Role existingRole = roleDao.getRoleByName(roleName);
        if (existingRole != null) {
            return existingRole;
        } else {
            Role newRole = new Role();
            newRole.setRole(roleName);
            roleDao.saveRole(newRole);
            return newRole;
        }
    }

    @Override
    public void deleteRole(int id) {
        roleDao.deleteRole(id);
    }
}