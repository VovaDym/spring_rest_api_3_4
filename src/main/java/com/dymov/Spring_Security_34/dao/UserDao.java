package com.dymov.Spring_Security_34.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dymov.Spring_Security_34.model.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String username);

}