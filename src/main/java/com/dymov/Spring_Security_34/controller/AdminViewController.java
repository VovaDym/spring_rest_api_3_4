package com.dymov.Spring_Security_34.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dymov.Spring_Security_34.model.entity.User;
import com.dymov.Spring_Security_34.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAdminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getByUsername(userDetails.getUsername());
        model.addAttribute("currentUser", currentUser);
        return "admin";
    }
}