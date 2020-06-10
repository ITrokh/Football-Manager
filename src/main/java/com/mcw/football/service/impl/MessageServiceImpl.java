package com.mcw.football.service.impl;

import com.mcw.football.domain.Message;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.ChatMessageDto;
import com.mcw.football.domain.dto.MessageDTO;
import com.mcw.football.repository.MessageRepository;
import com.mcw.football.service.MessageService;
import com.mcw.football.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    private final UserService userService;
    @Override
    public Page<MessageDTO> messageList(Pageable pageable, String filter, User user){
        if (filter != null && !filter.isEmpty()) {
            return messageRepository.findByTag(filter, pageable, user);
        } else {
            return messageRepository.findAll(pageable, user);
        }
    }
    @Override
    public Page<MessageDTO> messageListForUser(Pageable pageble, User currentUser, User author) {
        /*List<Message> all = messageRepository.findAll();
        List<MessageDTO> authorsMessages = all.stream().filter(message -> message.getAuthor().getId().equals(currentUser.getId()))
                .map(message -> new MessageDTO(message, (long) message.getLikes().size(), message.getLikes().stream()
                .anyMatch(liKe -> liKe.getId().equals(currentUser.getId())))).collect(Collectors.toList());
        return new PageImpl<>(authorsMessages, pageble, authorsMessages.size());*/
        return messageRepository.findAllWhereAuthor(pageble,currentUser,author);
    }
    @Override
    public Message saveMessage(ChatMessageDto messageDto) {
        User user = userService.getUserById(messageDto.getUserId());
        Message message = new Message();
        message.setText(messageDto.getContent());
        message.setAuthor(user);
        message.setChatType(messageDto.getChatType().name());
        return messageRepository.saveAndFlush(message);
    }
    @Override
    @Transactional// чтобы весь метод находился в транзакции и лайки нормально загружались, иначе будет LazyException
    public void likeMessage(ChatMessageDto chatMessageDto) {
        Message message = messageRepository.findById(chatMessageDto.getId()).orElseThrow(NullPointerException::new);
        User user = userService.getUserById(chatMessageDto.getUserId());
        Set<User> likes = message.getLikes();

        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }

        messageRepository.save(message);
        chatMessageDto.setLikeAmount(likes.size());
    }
    @Override
    public List<ChatMessageDto> getAllMessagesByChatType(String chatType) {
        Pageable top = PageRequest.of(0, 100);
        List<Message> allByChatType = messageRepository.findAllByChatTypeOrderById(chatType, top);
        List<ChatMessageDto> messageDtos = new ArrayList<>();
        for (Message message : allByChatType) {
            ChatMessageDto messageDTO = new ChatMessageDto();
            messageDTO.setId(message.getId());
            messageDTO.setUserId(message.getAuthor().getId());
            messageDTO.setSender(message.getAuthorName());
            messageDTO.setLikeAmount(message.getLikes().size());
            messageDTO.setContent(message.getText());
            messageDTO.setChatType(ChatMessageDto.ChatType.valueOf(message.getChatType()));
            messageDtos.add(messageDTO);
        }
        return messageDtos;
    }
}
