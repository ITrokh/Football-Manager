package com.mcw.football.domain.dto;

import com.mcw.football.domain.Team;
import lombok.Data;

import java.time.LocalDate;
@Data
public class TeamResponseDto {
    private Long id;
    private String name;
    private LocalDate createdDate;
    private Integer studentAmount;
    private String leader;

    public TeamResponseDto(Team team, int studentAmount) {
        this.id = team.getId();
        this.name = team.getName();
        this.createdDate = team.getCreatedDate();
        this.studentAmount = studentAmount;
        this.leader=team.getLeader().getFullName();
    }
}
