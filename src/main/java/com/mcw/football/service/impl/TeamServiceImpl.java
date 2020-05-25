package com.mcw.football.service.impl;

import com.mcw.football.domain.Mark;
import com.mcw.football.domain.Student;
import com.mcw.football.domain.Team;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponse;
import com.mcw.football.domain.dto.StudentUpdateRequest;
import com.mcw.football.domain.dto.TeamResponse;
import com.mcw.football.domain.util.CommonMethods;
import com.mcw.football.repository.MarkRepository;
import com.mcw.football.repository.StudentRepository;
import com.mcw.football.repository.TeamRepository;
import com.mcw.football.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final StudentRepository studentRepository;

    private final MarkRepository markRepository;
    private static final int LESSONS_COUNT = 25;
    @Override
    public void createTeam(Team team, User user) {
        team.setLeader(user);
        teamRepository.saveAndFlush(team);
    }
    @Override
    public List<TeamResponse> getTeamList() {
        List<Team> teams = teamRepository.findAll();

        List<TeamResponse> response = new ArrayList<>();
        teams.forEach(team -> {
            if(team.getStudentList()==null){
                team.setStudentList(new ArrayList<>());
            }
            response.add(new TeamResponse(team, team.getStudentList().size()));
        });

        return response;
    }
    @Override
    public List<StudentResponse> getPlayersByTeamId(Long teamId) {
        List<Student> allByTeamId = studentRepository.findAllByTeamId(teamId);
        allByTeamId.forEach(student ->student.setMarks(student.getMarks().stream()
                .filter(CommonMethods.distinctByKey(Mark::getId)).collect(Collectors.toList())));
        List<StudentResponse> response = new ArrayList<>();
        allByTeamId.forEach(player -> response.add(new StudentResponse(player)));
        return response;
    }
    @Override
    public List<StudentResponse> getAllPlayersWhichAreNotInTeam(Long teamId) {
        List<Student> allStudents = studentRepository.findAll();
        List<Student> studentsNotFromThisTeam = allStudents.stream().filter(student -> student.getTeam() == null
                || !student.getTeam().getId().equals(teamId)).collect(Collectors.toList());
        List<StudentResponse> response = new ArrayList<>();
        for (Student student : studentsNotFromThisTeam) {
            response.add(new StudentResponse(student));
        }
        return response;
    }
    @Override
    public void addPStudentToAnotherTeam(Long teamId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setTeam(teamRepository.findById(teamId).orElseThrow(NullPointerException::new));
        studentRepository.saveAndFlush(student);
    }
    @Override
    public void removePlayerFromTeam(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setTeam(null);
        studentRepository.saveAndFlush(student);
    }
    @Override
    public StudentResponse getPlayer(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setMarks(student.getMarks().stream().filter(CommonMethods.distinctByKey(Mark::getId)).collect(Collectors.toList()));
        return new StudentResponse(student);
    }
    @Override
    public StudentResponse updatePlayer(Long studentId, StudentUpdateRequest updatePlayer, Map<String, String> form) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setMarks(student.getMarks().stream().filter(CommonMethods.distinctByKey(Mark::getId)).collect(Collectors.toList()));
        List<Mark> markList = student.getMarks();
        if(markList.size()<LESSONS_COUNT){
            int size=markList.isEmpty()?0:markList.size()-1;
            for(int i=size; i<LESSONS_COUNT; i++){
                markList.add(new Mark(student, false));
            }
        }
        for(int i=0; i<LESSONS_COUNT;i++){
            String s = form.get(String.valueOf(i));
                    if(s!=null && !markList.get(i).isMark()) {
                        markList.get(i).setMark(true);
                        markList.get(i).setCreatedDate(LocalDate.now());
                    }
        }
                    student.setMarks(markRepository.saveAll(markList));
        /*Optional.ofNullable(updatePlayer.getPosition()).ifPresent(student::setPosition);
        Optional.ofNullable(updatePlayer.getSalary()).ifPresent(student::setSalary);
        Optional.ofNullable(updatePlayer.getState()).ifPresent(student::setState);*/
//        Optional.ofNullable(updatePlayer.getName()).ifPresent(student::set);
        return new StudentResponse(studentRepository.saveAndFlush(student));
    }
}
