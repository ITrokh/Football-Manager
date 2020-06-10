/*
Created by IntelliJ IDEA.
By trohi
Date: 14.05.2019 18:51
Project Name: football
*/
package com.mcw.football.controller;

import com.mcw.football.domain.Role;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponseDto;
import com.mcw.football.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")//чтобы каждый раз не писать в мепиингах /user
/*@PreAuthorize("hasAuthority('ADMIN')")//доступ к полям только у админа*/
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {//@PathVariable User user используем чтобы получать пользователя напрямую из базы,
                                                                        // а на обращться к репозиторию
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(@RequestParam String username,
                           @RequestParam Map<String, String> form, //так как переменное количество полей,
                           // они будут все попадать в форму, но придут те, которые отмечены флажком и каждый раз будет разное количество полей
                           @RequestParam("userId") User user){
        userService.saveUser(user, username, form);
        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        if(user.isStudent()){
            model.addAttribute("student",new StudentResponseDto(user.getStudent()));
        }
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email,
                                @RequestParam String username) {
        userService.updateProfile(user, password, email, username);
       return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(@AuthenticationPrincipal User currentUser,
                            @PathVariable User user){
        userService.subscribe(currentUser, user);

        return "redirect:/user-messages/"+user.getId();
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(@AuthenticationPrincipal User currentUser,
                            @PathVariable User user){
        userService.unsubscribe(currentUser, user);

        return "redirect:/user-messages/"+user.getId();
    }

    @GetMapping("{type}/{user}/list")
    public String userList(Model model,
                           @PathVariable User user,
                           @PathVariable String type){
        model.addAttribute("userChannel",user);
        model.addAttribute("type",type);

        if("subscriptions".equals(type)){
            model.addAttribute("users", user.getSubscriptions());
        } else{ model.addAttribute("users", user.getSubscribers());}
        return "subscriptions";
    }
}
