package com.dymov.Spring_Security_34.service;

import com.dymov.Spring_Security_34.model.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();

    Optional<Role> findById(Long id);

    List<Role> findAllByIdIn(List<Long> ids);

    void save(Role newRole);
}
