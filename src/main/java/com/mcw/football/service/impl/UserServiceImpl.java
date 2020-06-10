package com.mcw.football.service.impl;

import com.google.common.base.Strings;
import com.mcw.football.domain.Role;
import com.mcw.football.domain.Student;
import com.mcw.football.domain.User;
import com.mcw.football.repository.StudentRepository;
import com.mcw.football.repository.UserRepository;
import com.mcw.football.service.MailSenderService;
import com.mcw.football.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private StudentRepository studentRepository;

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
        user=userRepository.save(user);
        if (user.getRoles() != null && user.getRoles().contains(Role.STUDENT)) {
            createStudent(user);
        }
        sendMessage(userRepository.saveAndFlush(user));
        return true;
    }

    private void createStudent(User user) {
        Student student= studentRepository.findByUserId(user.getId());
        if(student==null) {
            studentRepository.saveAndFlush(new Student(user));
        }else{
            studentRepository.saveAndFlush(user.getStudent());
        }
    }
    private void deleteStudent(User user) {
        Student student= studentRepository.findByUserId(user.getId());
        if(student!=null) {
            studentRepository.deleteById(student.getId());
        }
    }
    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to My diploma app. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSenderService.send(user.getEmail(), "Activation code", message);
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
        if(user.getStudent()!=null){
            String group = form.get("group");
            if(group!=null) {
                String groupRegex = "^\\w{2}-\\w";
                Pattern pattern = Pattern.compile(groupRegex);
                Matcher matcher = pattern.matcher(group);
                if(matcher.find()){
                    user.getStudent().setGroup(group);
                }else{
                    user.getStudent().setGroup("");
                }
            }
        }
        if(user.getRoles().contains(Role.STUDENT)) {
            createStudent(user);
        }else{ deleteStudent(user);
        }
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    public void updateProfile(User user, String password, String email, String username) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
                user.setActive(false);
            }
        }
        if(!Strings.isNullOrEmpty(username) && !username.equals(user.getUsername())){
            user.setUsername(username);
        }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    public void subscribe(User currentUser, User user) {
        currentUser=getUserById(currentUser.getId());
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        currentUser=getUserById(currentUser.getId());
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findAllByRolesContains(role);
    }
}
