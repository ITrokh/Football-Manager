package com.mcw.football.domain.dto;

import com.mcw.football.domain.Player;

public class PlayerResponse {

    private Long id;
    private String name;
    private String teamName;
    private Long teamId;
    private Double salary;
    private String state;
    private String position;

    public PlayerResponse(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public PlayerResponse(Player player) {
        this.id = player.getId();
        this.name = player.getUser().getUsername();
        if (player.getTeam() != null) {
            this.teamId = player.getTeam().getId();
            this.teamName = player.getTeam().getName();
        } else {
            this.teamName = "";
        }
        this.salary = player.getSalary() != null ? player.getSalary() : 0.0;
        this.state = player.getState() != null ? player.getState() : "";
        this.position = player.getPosition() != null ? player.getPosition() : "";
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
