package com.mcw.football.controller;

import com.mcw.football.domain.Team;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponseDto;
import com.mcw.football.domain.dto.StudentUpdateRequestDto;
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
    public String getTeamList(Model model,
                              @AuthenticationPrincipal User user) {
        model.addAttribute("teams", teamService.getTeamList(user));
        return "teams";
    }

    @PreAuthorize("hasAuthority('TEAM_LEADER')")
    @PostMapping("/add")
    public String createTeam(@AuthenticationPrincipal User user,
                             @Validated Team team, Model model) {
        teamService.createTeam(team, user);
        model.addAttribute("teams", teamService.getTeamList(user));
        return "teams";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','STUDENT')")
    @GetMapping("/{teamId}/students")
    public String getStudentList(Model model, @PathVariable Long teamId) {
       fillModel(teamId, model);
        return "students";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','ADMIN')")
    @GetMapping("/{teamId}/remove_student/{studentId}")
    public String removeStudentFromTeam(@PathVariable Long teamId, @PathVariable Long studentId, Model model) {
        teamService.removeStudentFromTeam(studentId);
        fillModel(teamId, model);
        return "students";
    }

    @PreAuthorize("hasAnyAuthority('TEAM_LEADER','ADMIN')")
    @GetMapping("/{teamId}/add_student/{studentId}")
    public String addStudentToTeam(@PathVariable Long teamId, @PathVariable Long studentId, Model model) {
        teamService.addStudentToAnotherTeam(teamId, studentId);
        fillModel(teamId, model);
        return "students";
    }

    @GetMapping("/student/{studentId}")
    public String getStudentProfile(@PathVariable Long studentId, Model model) {
        StudentResponseDto student = teamService.getStudent(studentId);
        model.addAttribute("student", student);
        return "student";
    }

    @PostMapping("/student/{studentId}")
    public String updateStudentProfile(@PathVariable Long studentId, @RequestParam Map<String, String> form, StudentUpdateRequestDto updateStudent, Model model) {
        StudentResponseDto student = teamService.updateStudent(studentId, updateStudent, form);
        model.addAttribute("student", student);
        return "student";
    }

    private void fillModel(Long teamId, Model model) {
        model.addAttribute("students", teamService.getStudentByTeamId(teamId));
        model.addAttribute("possibleStudents", teamService.getAllStudentsWhichAreNotInTeam(teamId));
        model.addAttribute("teamId", teamId);
    }
}
