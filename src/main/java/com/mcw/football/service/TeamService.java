package com.mcw.football.service;

import com.mcw.football.domain.Team;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponseDto;
import com.mcw.football.domain.dto.StudentUpdateRequestDto;
import com.mcw.football.domain.dto.TeamResponseDto;

import java.util.List;
import java.util.Map;

public interface TeamService {

    void createTeam(Team team, User user);

    List<TeamResponseDto> getTeamList(User user);

    List<StudentResponseDto> getStudentByTeamId(Long teamId);

    List<StudentResponseDto> getAllStudentsWhichAreNotInTeam(Long teamId);

    void addStudentToAnotherTeam(Long teamId, Long studentId);

    void removeStudentFromTeam(Long studentId);

    StudentResponseDto getStudent(Long studentId);

    StudentResponseDto updateStudent(Long studentId, StudentUpdateRequestDto updateStudent, Map<String, String> form);
}
