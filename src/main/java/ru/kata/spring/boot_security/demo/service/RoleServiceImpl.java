package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDAO;

    public RoleServiceImpl(RoleDao roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional
    public Set<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    @Transactional
    public Role getRoleById(int id) {
        return roleDAO.getRoleById(id);
    }

    @Override
    @Transactional
    public Role getRoleByName(String roleName) {
        return roleDAO.getRoleByName(roleName);
    }

    @Override
    @Transactional
    public void saveRole(Set<Role> roles) {

        Set<String> allRolesName = getAllRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        roles.stream()
                .filter(role -> allRolesName.contains(role.getRole()))
                .forEach(roleDAO::saveRole);
    }

    @Override
    @Transactional
    public void saveRole(String roleName) {
        roleDAO.saveRole(new Role(roleName));
    }

    @Override
    @Transactional
    public void deleteRole(int id) {
        roleDAO.deleteRole(id);
    }
}