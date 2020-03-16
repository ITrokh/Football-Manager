package com.mcw.football.service.impl;

import com.mcw.football.domain.Player;
import com.mcw.football.domain.Role;
import com.mcw.football.domain.User;
import com.mcw.football.repository.PlayerRepository;
import com.mcw.football.repository.UserRepository;
import com.mcw.football.service.MailSender;
import com.mcw.football.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean addUser(User user){
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {//если пользователь есть в бд, значит не добавляем
            return false;
        }

//        user.setActive(true); будет неактивен пока не активируют через почту
        if (user.getRoles() == null || user.getRoles().size() == 0) {
            user.setRoles(Collections.singleton(Role.USER));
        } else {
            user.setRoles(user.getRoles());
        }
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() != null && user.getRoles().contains(Role.PLAYER)) {
            createPlayer(user);
        }
        sendMessage(userRepository.saveAndFlush(user));
        return true;
    }

    private void createPlayer(User user) {
        Player player=playerRepository.findByUserId(user.getId());
        if(player==null) {
            playerRepository.saveAndFlush(new Player(user));
        }
    }
    private void deletePlayer(User user) {
        Player player=playerRepository.findByUserId(user.getId());
        if(player!=null) {
            playerRepository.deleteById(player.getId());
        }
    }
    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to MYApp. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        userRepository.saveAndFlush(user);
        return true;
    }

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> setOfRoles = Arrays.stream(Role.values())
                .map(Role::name)  //переводим из Enum в строковый вид
                .collect(Collectors.toSet());
        user.getRoles().clear();//очистка ролей
        //ложим роли в юзера
        for(String key : form.keySet()){
            if(setOfRoles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
        if(user.getRoles().contains(Role.PLAYER)) {
            createPlayer(user);
        }else{ deletePlayer(user);
        }
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepository.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findAllByRolesContains(role);
    }
}
