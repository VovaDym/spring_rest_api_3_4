package com.dymov.Spring_Security_34.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dymov.Spring_Security_34.exeption.EntityNotFoundException;
import com.dymov.Spring_Security_34.model.dto.UpdateUserRequest;
import com.dymov.Spring_Security_34.model.entity.Role;
import com.dymov.Spring_Security_34.model.entity.User;
import com.dymov.Spring_Security_34.service.RoleService;
import com.dymov.Spring_Security_34.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AdminApiController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminApiController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    //Получение всех пользователей
    @GetMapping("/users")
    public List<User> getAdminPanel() {
        return userService.getAllUsers();
    }
    //Получение одного пользователя
    @GetMapping("/users/{id}")
    public User getUserForEditing(@PathVariable("id") long id) {
        return userService.getUserOrCreateIfNotExists(id);
    }
    //Добавление пользователя
    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@RequestBody UpdateUserRequest request) {
        User user = request.getUser();
        Long[] rolesIds = request.getRoleIds();
        Set<Role> roles = new HashSet<>();
        for (Long roleId : rolesIds) {
            Role role = roleService.findById(roleId).orElseThrow(() ->
                    new EntityNotFoundException("Роль с id=" + roleId + " не найдена"));
            roles.add(role);
        }

        user.setRoles(roles, false);
        userService.createUser(user);

        return ResponseEntity.ok().build();
    }
    //Изменение пользователя
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest request) {

        userService.updateUser(request.getId(), request.getUser(), request.getRoleIds());
        return ResponseEntity.ok().build();
    }
    //Удаление пользователя
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    //Получение всех ролей
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

}
