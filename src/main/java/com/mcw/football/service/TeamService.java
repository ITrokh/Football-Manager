package com.mcw.football.service;

import com.mcw.football.domain.Player;
import com.mcw.football.domain.Team;
import com.mcw.football.domain.dto.PlayerResponse;
import com.mcw.football.domain.dto.PlayerUpdateRequest;
import com.mcw.football.domain.dto.TeamResponse;
import com.mcw.football.repository.PlayerRepository;
import com.mcw.football.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;



    @Autowired
    private PlayerRepository playerRepository;

    public void createTeam(Team team) {
        teamRepository.saveAndFlush(team);
    }

    public List<TeamResponse> getTeamList() {
        List<Team> teams = teamRepository.findAll();

        List<TeamResponse> response = new ArrayList<>();
        teams.forEach(team -> response.add(new TeamResponse(team, team.getPlayerList().size())));

        return response;
    }

    public List<PlayerResponse> getPlayersByTeamId(Long teamId) {
        List<Player> allByTeamId = playerRepository.findAllByTeamId(teamId);

        List<PlayerResponse> response = new ArrayList<>();
        allByTeamId.forEach(player -> response.add(new PlayerResponse(player)));
        return response;
    }

    public List<PlayerResponse> getAllPlayersWhichAreNotInTeam(Long teamId) {
        List<Player> allPlayers = playerRepository.findAll();
        List<Player> playersNotFromThisTeam = allPlayers.stream().filter(player -> player.getTeam() == null || !player.getTeam().getId().equals(teamId)).collect(Collectors.toList());

        List<PlayerResponse> response = new ArrayList<>();
        for (Player player : playersNotFromThisTeam) {
            response.add(new PlayerResponse(player));
        }
        return response;
    }

    public void addPlayerToAnotherTeam(Long teamId, Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(NullPointerException::new);
        player.setTeam(teamRepository.findById(teamId).orElseThrow(NullPointerException::new));
        playerRepository.saveAndFlush(player);
    }

    public void removePlayerFromTeam(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(NullPointerException::new);
        player.setTeam(null);
        playerRepository.saveAndFlush(player);
    }

    public PlayerResponse getPlayer(Long playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(NullPointerException::new);
        return new PlayerResponse(player);
    }

    public PlayerResponse updatePlayer(Long playerId, PlayerUpdateRequest updatePlayer) {
        Player player = playerRepository.findById(playerId).orElseThrow(NullPointerException::new);
        Optional.ofNullable(updatePlayer.getPosition()).ifPresent(player::setPosition);
        Optional.ofNullable(updatePlayer.getSalary()).ifPresent(player::setSalary);
        Optional.ofNullable(updatePlayer.getState()).ifPresent(player::setState);
//        Optional.ofNullable(updatePlayer.getName()).ifPresent(player::set);
        return new PlayerResponse(playerRepository.saveAndFlush(player));
    }
}
