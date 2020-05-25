package com.mcw.football.controller;

import com.mcw.football.domain.Team;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponse;
import com.mcw.football.domain.dto.StudentUpdateRequest;
import com.mcw.football.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','STUDENT')")
    @GetMapping
    public String getTeamList(Model model) {
        model.addAttribute("teams", teamService.getTeamList());
        return "teams";
    }

    @PreAuthorize("hasAuthority('TEAM_LEADER')")
    @PostMapping("/add")
    public String createTeam(@AuthenticationPrincipal User user,
                             @Validated Team team, Model model) {
        teamService.createTeam(team, user);
        model.addAttribute("teams", teamService.getTeamList());
        return "teams";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','STUDENT')")
    @GetMapping("/{teamId}/players")
    public String getPlayerList(Model model, @PathVariable Long teamId) {
       fillModel(teamId, model);
        return "players";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','ADMIN')")
    @GetMapping("/{teamId}/remove_player/{playerId}")
    public String removePlayerFromTeam(@PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        teamService.removePlayerFromTeam(playerId);
        fillModel(teamId, model);
        return "players";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','ADMIN')")
    @GetMapping("/{teamId}/add_player/{playerId}")
    public String addPlayerToTeam(@PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        teamService.addPStudentToAnotherTeam(teamId, playerId);
        fillModel(teamId, model);
        return "players";
    }

    @GetMapping("/player/{playerId}")
    public String getPlayerProfile(@PathVariable Long playerId, Model model) {
        StudentResponse player = teamService.getPlayer(playerId);
        model.addAttribute("player", player);
        return "player";
    }

    @PostMapping("/player/{playerId}")
    public String updatePlayerProfile(@PathVariable Long playerId, @RequestParam Map<String, String> form, StudentUpdateRequest updatePlayer, Model model) {
        StudentResponse player = teamService.updatePlayer(playerId, updatePlayer, form);
        model.addAttribute("player", player);
        return "player";
    }

    private void fillModel(Long teamId, Model model) {
        model.addAttribute("players", teamService.getPlayersByTeamId(teamId));
        model.addAttribute("possiblePlayers", teamService.getAllPlayersWhichAreNotInTeam(teamId));
        model.addAttribute("teamId", teamId);
    }
}
