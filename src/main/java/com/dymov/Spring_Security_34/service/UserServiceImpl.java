package com.dymov.Spring_Security_34.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dymov.Spring_Security_34.dao.UserDao;
import com.dymov.Spring_Security_34.exeption.EntityNotFoundException;
import com.dymov.Spring_Security_34.model.entity.Role;
import com.dymov.Spring_Security_34.model.entity.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDao userDao;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserDao userDao, RoleService roleService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDao = userDao;
        this.roleService = roleService;
    }


@Override
@Transactional
public void createUser(User user) {
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
        throw new IllegalArgumentException("Пользователь должен иметь хотя бы одну роль");
    }

    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

    try {
        userDao.save(user);
        System.out.println("Пользователь успешно создан: " + user.getUsername());
    } catch (Exception e) {
        System.out.println("Ошибка при сохранении пользователя: " + e);
        throw e;
    }
}

    @Override
    @Transactional
    public void updateUser(Long userId, User updatedUser, Long[] rolesIds) {
        User existingUser = userDao.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));

        if (!updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        } else {
            updatedUser.setPassword(existingUser.getPassword());
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setAge(updatedUser.getAge());

        Set<Role> updatedRoles = new HashSet<>();
        if (rolesIds != null && rolesIds.length > 0) {
            for (Long roleId : rolesIds) {
                Optional<Role> optionalRole = roleService.findById(roleId);
                if (optionalRole.isPresent()) {
                    updatedRoles.add(optionalRole.get());
                } else {
                    throw new EntityNotFoundException("Роль с id=" + roleId + " не найдена");
                }
            }
        }
        if (!updatedRoles.isEmpty()) {
            existingUser.setRoles(updatedRoles, true);
        }

        userDao.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserOrCreateIfNotExists(long id) {
        return userDao.findById(id)
                .orElse(new User(id));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = getByUsername(username);

        List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findByUsername(String username) {
        return getByUsername(username) != null;
    }

}