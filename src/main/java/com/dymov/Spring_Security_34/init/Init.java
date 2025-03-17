package com.dymov.Spring_Security_34.init;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.dymov.Spring_Security_34.model.entity.Role;
import com.dymov.Spring_Security_34.model.entity.User;
import com.dymov.Spring_Security_34.service.RoleService;
import com.dymov.Spring_Security_34.service.UserService;

import java.util.Collections;

@Component
@Transactional
public class Init {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @PostConstruct
    public void init() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");

        roleService.save(roleAdmin);
        roleService.save(roleUser);

        User admin = new User(
                "admin",
                "admin",
                Collections.singleton(roleAdmin),
                "Admin",
                "Admin",
                (byte) 50,
                "admin@mail.com"
        );

        User user = new User(
                "user",
                "user",
                Collections.singleton(roleUser),
                "User",
                "User",
                (byte) 30,
                "user@email.com"
        );

        userService.createUser(admin);
        userService.createUser(user);
    }

}
