package com.dymov.Spring_Security_34.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dymov.Spring_Security_34.model.entity.Role;
import com.dymov.Spring_Security_34.model.entity.User;
import com.dymov.Spring_Security_34.service.RoleService;
import com.dymov.Spring_Security_34.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUserForm(Model model,
                              @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        model.addAttribute("users", userService.getAllUsers());

        User currentUser = userService.getByUsername(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("newUser", new User());

        List<Role> allRoles = roleService.getAllRoles();
        model.addAttribute("allRoles", allRoles);

        return "user";
    }
}