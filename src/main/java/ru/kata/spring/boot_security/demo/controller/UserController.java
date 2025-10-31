package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.kata.spring.boot_security.demo.entity.MyUser;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all-users";
    }

    @GetMapping("/user/profile")
    public String showMyProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            MyUser myUser = userService.getUserByLogin(authentication.getName());
            model.addAttribute("user", myUser);
            model.addAttribute("isOwnProfile", true);
        } catch (Exception e) {
            MyUser tempUser = new MyUser();
            tempUser.setName(authentication.getName());
            tempUser.setEmail(authentication.getName());
            model.addAttribute("user", tempUser);
            model.addAttribute("isOwnProfile", true);
        }
        return "user-profile";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUserProfile(@PathVariable("id") int userId, Model model) {
        MyUser myUser = userService.getUserById(userId);
        model.addAttribute("user", myUser);
        // Проверяем, смотрит ли пользователь свой профиль
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        boolean isOwnProfile = currentUsername.equals(myUser.getEmail());
        model.addAttribute("isOwnProfile", isOwnProfile);
        return "user-profile";
    }

    @GetMapping("/admin/addNewUser")
    public String addUser(Model model) {
        model.addAttribute("user", new MyUser());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-form";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new MyUser());
        model.addAttribute("allRoles", roleService.getAllRoles().stream()
                .filter(role -> role.getRole().equals("USER"))
                .collect(Collectors.toSet()));
        return "registration-form";
    }

    @PostMapping("/registration/saveUser")
    public String saveRegistration(@ModelAttribute("user") MyUser myUser) {
        // Устанавливаем роль USER по умолчанию для новых пользователей
        Role userRole = roleService.getRoleByName("USER");
        if (userRole != null) {
            myUser.setRoles(Set.of(userRole));
        }
        userService.saveUser(myUser);
        return "redirect:/";
    }

    @PostMapping("/admin/saveUser")
    public String saveUser(@ModelAttribute("user") MyUser myUser) {
        userService.saveUser(myUser);
        return "redirect:/admin";
    }

    @GetMapping("/admin/updateInfo")
    public String updateInfo(@RequestParam("userId") int id, Model model) {
        MyUser myUser = userService.getUserById(id);
        model.addAttribute("user", myUser);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "user-update-form";
    }

    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
