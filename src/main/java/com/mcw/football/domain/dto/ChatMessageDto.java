package com.mcw.football.domain.dto;

public class ChatMessageDto {

    private Long id;
    private MessageType type;
    private String content;
    private String sender;
    private Long userId;
    private ChatType chatType;
    private Integer likeAmount = 0;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public enum ChatType {
        GLOBAL,
        TEAM
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(Integer likeAmount) {
        this.likeAmount = likeAmount;
    }
}
