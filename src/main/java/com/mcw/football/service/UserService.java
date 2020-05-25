/*
Created by IntelliJ IDEA.
By trohi
Date: 14.05.2019 16:12
Project Name: football
*/
package com.mcw.football.service;

import com.mcw.football.domain.Role;
import com.mcw.football.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;


public interface UserService extends UserDetailsService {

   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean addUser(User user);

    boolean activateUser(String code);

    List<User> findAll();

    void saveUser(User user, String username, Map<String, String> form);

    User getUserById(Long id);

    void updateProfile(User user, String password, String email, String username);

    void subscribe(User currentUser, User user);

    void unsubscribe(User currentUser, User user);

    List<User> getUsersByRole(Role role);
}
