package com.dymov.Spring_Security_34.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dymov.Spring_Security_34.model.entity.Role;

import java.util.List;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

    List<Role> findAllById(Iterable<Long> ids);

}