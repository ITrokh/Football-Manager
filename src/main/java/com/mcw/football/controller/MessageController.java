package com.mcw.football.controller;

import com.mcw.football.domain.Message;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.ChatMessageDto;
import com.mcw.football.domain.dto.MessageDTO;
import com.mcw.football.repository.MessageRepository;
import com.mcw.football.service.MessageService;
import com.mcw.football.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageController {


    private final MessageRepository messageRepository;

    private final MessageService messageService;
    private final UserService userService;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(){
            return "greeting";
    }


    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter,//(required = false) потому что не всегда его вызываем
                       Model model,
                       @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal User user) {
        Page<MessageDTO> page = messageService.messageList(pageable, filter, user);
        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @Valid Message message,
                      BindingResult bindingResult,
                      Model model,
                      @RequestParam("file") MultipartFile file,
                      @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);

            model.addAttribute("message", null);

            messageRepository.save(message);
        }

        Page<MessageDTO> page = messageService.messageListForUser(pageable, user, user);
        model.addAttribute("url", "/main/" + user.getId());
        model.addAttribute("page", page);

        return "main";
    }

    private void saveFile(Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }//проверка на существование папки, если нету, то создаем

            String uuidFile = UUID.randomUUID().toString();//генерируем уникальный userId во избежание колизий
            String resultFilename = uuidFile + "-" + file.getOriginalFilename();//создание имя файла с исользованием uuid и изначального названия
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(@AuthenticationPrincipal User currentUser,
                               @PathVariable User author,
                               Model model,
                               @RequestParam(required = false) Message message,
                               @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble) {
        author=userService.getUserById(author.getId());
        Page<MessageDTO> page = messageService.messageListForUser(pageble, currentUser, author);
        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().stream().anyMatch(user->user.getId().equals(currentUser.getId())));
        model.addAttribute("page", page);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.getId().equals(author.getId()));
        model.addAttribute("url", "/user-messages/" + author.getId());

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser,
                                @PathVariable Long user,
                                @RequestParam("id") Message message,
                                @RequestParam("text") String text,
                                /*@RequestParam("tag") String tag,*/
                                @RequestParam("file") MultipartFile file) throws IOException {
        if(message.getAuthor().equals(currentUser)||currentUser.isAdmin()){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            /*if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }*/
            saveFile(message,file);
            messageRepository.save(message);
        }
        return "redirect:/user-messages/{user}";
    }

    @GetMapping("/getAllMessages")
    public @ResponseBody List<ChatMessageDto> getAllMessages(@RequestParam String chatType) {
        return messageService.getAllMessagesByChatType(chatType);
    }

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
       currentUser= userService.getUserById(currentUser.getId());
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }
        messageRepository.save(message);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .forEach((key, value) -> redirectAttributes.addAttribute(key, value));

        return "redirect:" + components.getPath();
    }
}
