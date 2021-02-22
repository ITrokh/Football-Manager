package com.mcw.football.controller;

import com.mcw.football.domain.Message;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.ChatMessageDto;
import com.mcw.football.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDto sendMessage(@Payload ChatMessageDto chatMessageDto) {
        var message = messageService.saveMessage(chatMessageDto);
        chatMessageDto.setId(message.getId());
        chatMessageDto.setUserId(message.getAuthor().getId());
        return chatMessageDto;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageDto addUser(@Payload ChatMessageDto chatMessageDto,
                                  SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessageDto.getSender());
        headerAccessor.getSessionAttributes().put("userId", chatMessageDto.getUserId());
        return chatMessageDto;
    }

    @MessageMapping("/chat.like")
    @SendTo("/topic/like")
    public ChatMessageDto like(@Payload ChatMessageDto chatMessageDto) {

        messageService.likeMessage(chatMessageDto);
        return chatMessageDto;
    }

    @MessageMapping("/chat.sendMessageTeam")
    @SendTo("/topic/team")
    public ChatMessageDto sendMessageTeam(@Payload ChatMessageDto chatMessageDto){
        Message message = messageService.saveMessage(chatMessageDto);
        chatMessageDto.setId(message.getId());
        chatMessageDto.setUserId(message.getAuthor().getId());
        return chatMessageDto;
    }

    @MessageMapping("/chat.addUserTeam")
    @SendTo("/topic/team")
    public ChatMessageDto addUserTeam(@Payload ChatMessageDto chatMessageDto,
                                      SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessageDto.getSender());
        headerAccessor.getSessionAttributes().put("userId", chatMessageDto.getUserId());
        return chatMessageDto;
    }

    @MessageMapping("/chat.likeTeam")
    @SendTo("/topic/likeTeam")
    public ChatMessageDto likeTeam(@Payload ChatMessageDto chatMessageDto) {

        messageService.likeMessage(chatMessageDto);
        return chatMessageDto;
    }

    @GetMapping("/chat")
    public String getChatPage(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("user", currentUser);
        return "chat";
    }
}
