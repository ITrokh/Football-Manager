package com.mcw.football.service;

import com.mcw.football.domain.Team;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponse;
import com.mcw.football.domain.dto.StudentUpdateRequest;
import com.mcw.football.domain.dto.TeamResponse;

import java.util.List;
import java.util.Map;

public interface TeamService {

    void createTeam(Team team, User user);

    List<TeamResponse> getTeamList();

    List<StudentResponse> getPlayersByTeamId(Long teamId);

    List<StudentResponse> getAllPlayersWhichAreNotInTeam(Long teamId);

    void addPStudentToAnotherTeam(Long teamId, Long playerId);

    void removePlayerFromTeam(Long playerId);

    StudentResponse getPlayer(Long playerId);

    StudentResponse updatePlayer(Long playerId, StudentUpdateRequest updatePlayer, Map<String, String> form);
}
