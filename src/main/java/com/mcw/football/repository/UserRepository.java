package com.mcw.football.repository;

import com.mcw.football.domain.Role;
import com.mcw.football.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    List<User> findAllByRolesContains(Role role);
}
