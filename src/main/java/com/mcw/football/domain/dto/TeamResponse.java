package com.mcw.football.domain.dto;

import com.mcw.football.domain.Team;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TeamResponse {
    private Long id;
    private String name;
    private LocalDate createdDate;
    private Integer playerAmount;
    private String leader;

    public TeamResponse(Team team, int playerAmount) {
        this.id = team.getId();
        this.name = team.getName();
        this.createdDate = team.getCreatedDate();
        this.playerAmount = playerAmount;
        this.leader=team.getLeader().getFullName();
    }
}
