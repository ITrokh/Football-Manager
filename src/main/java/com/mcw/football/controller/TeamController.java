package com.mcw.football.controller;

import com.mcw.football.domain.Player;
import com.mcw.football.domain.Team;
import com.mcw.football.domain.dto.PlayerResponse;
import com.mcw.football.domain.dto.PlayerUpdateRequest;
import com.mcw.football.service.TeamService;
import org.aspectj.apache.bcel.util.Play;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','PLAYER')")
    @GetMapping
    public String getTeamList(Model model) {
        model.addAttribute("teams", teamService.getTeamList());
        return "teams";
    }

    @PreAuthorize("hasAuthority('TEAM_LEADER')")
    @PostMapping("/add")
    public String createTeam(Team team, Model model) {
        teamService.createTeam(team);
        model.addAttribute("teams", teamService.getTeamList());
        return "teams";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','PLAYER')")
    @GetMapping("/{teamId}/players")
    public String getPlayerList(Model model, @PathVariable Long teamId) {
       fillModel(teamId, model);
        return "players";
    }

    @PreAuthorize("hasAuthority('TEAM_LEADER')")
    @GetMapping("/{teamId}/remove_player/{playerId}")
    public String removePlayerFromTeam(@PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        teamService.removePlayerFromTeam(playerId);
        fillModel(teamId, model);
        return "players";
    }

    @PreAuthorize("hasAuthority('TEAM_LEADER')")
    @GetMapping("/{teamId}/add_player/{playerId}")
    public String addPlayerToTeam(@PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        teamService.addPlayerToAnotherTeam(teamId, playerId);
        fillModel(teamId, model);
        return "players";
    }

    @GetMapping("/player/{playerId}")
    public String getPlayerProfile(@PathVariable Long playerId, Model model) {
        PlayerResponse player = teamService.getPlayer(playerId);
        model.addAttribute("player", player);
        return "player";
    }

    @PostMapping("/player/{playerId}")
    public String updatePlayerProfile(@PathVariable Long playerId, PlayerUpdateRequest updatePlayer, Model model) {
        PlayerResponse player = teamService.updatePlayer(playerId, updatePlayer);
        model.addAttribute("player", player);
        return "player";
    }

    private void fillModel(Long teamId, Model model) {
        model.addAttribute("players", teamService.getPlayersByTeamId(teamId));
        model.addAttribute("possiblePlayers", teamService.getAllPlayersWhichAreNotInTeam(teamId));
        model.addAttribute("teamId", teamId);
    }
}
