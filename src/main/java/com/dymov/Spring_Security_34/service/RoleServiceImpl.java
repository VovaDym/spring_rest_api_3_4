package com.dymov.Spring_Security_34.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dymov.Spring_Security_34.dao.RoleDao;
import com.dymov.Spring_Security_34.model.entity.Role;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleDao.findById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllByIdIn(List<Long> ids) {
        return roleDao.findAllById(ids);
    }

    @Override
    @Transactional
    public void save(Role newRole) {
        roleDao.saveAndFlush(newRole);
    }
}
