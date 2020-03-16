package com.mcw.football.service;

import com.mcw.football.domain.Team;
import com.mcw.football.domain.dto.PlayerResponse;
import com.mcw.football.domain.dto.PlayerUpdateRequest;
import com.mcw.football.domain.dto.TeamResponse;

import java.util.List;

public interface TeamService {

    void createTeam(Team team);

    List<TeamResponse> getTeamList();

    List<PlayerResponse> getPlayersByTeamId(Long teamId);

    List<PlayerResponse> getAllPlayersWhichAreNotInTeam(Long teamId);

    void addPlayerToAnotherTeam(Long teamId, Long playerId);

    void removePlayerFromTeam(Long playerId);

    PlayerResponse getPlayer(Long playerId);

    PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest updatePlayer);
}
