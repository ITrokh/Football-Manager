package com.mcw.football.domain.dto;

import com.mcw.football.domain.Team;

import java.time.LocalDate;

public class TeamResponse {
    private Long id;
    private String name;
    private LocalDate createdDate;
    private Integer playerAmount;

    public TeamResponse(Team team, int playerAmount) {
        this.id = team.getId();
        this.name = team.getName();
        this.createdDate = team.getCreatedDate();
        this.playerAmount = playerAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(Integer playerAmount) {
        this.playerAmount = playerAmount;
    }
}
