/*
Created by IntelliJ IDEA.
By trohi
Date: 21.05.2019 9:53
Project Name: football
*/
package com.mcw.football.service;

import com.mcw.football.domain.Message;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.ChatMessageDto;
import com.mcw.football.domain.dto.MessageDTO;
import com.mcw.football.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface MessageService {

   Page<MessageDTO> messageList(Pageable pageable, String filter, User user);

    Page<MessageDTO> messageListForUser(Pageable pageble, User currentUser, User author);

    Message saveMessage(ChatMessageDto messageDto);

    void likeMessage(ChatMessageDto chatMessageDto);

    List<ChatMessageDto> getAllMessagesByChatType(String chatType);
}
