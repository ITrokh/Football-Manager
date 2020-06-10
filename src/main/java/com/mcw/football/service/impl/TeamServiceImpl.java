package com.mcw.football.service.impl;

import com.mcw.football.domain.Mark;
import com.mcw.football.domain.Student;
import com.mcw.football.domain.Team;
import com.mcw.football.domain.User;
import com.mcw.football.domain.dto.StudentResponseDto;
import com.mcw.football.domain.dto.StudentUpdateRequestDto;
import com.mcw.football.domain.dto.TeamResponseDto;
import com.mcw.football.domain.util.CommonMethods;
import com.mcw.football.repository.MarkRepository;
import com.mcw.football.repository.StudentRepository;
import com.mcw.football.repository.TeamRepository;
import com.mcw.football.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        team.setCreatedDate(LocalDate.now());
        teamRepository.saveAndFlush(team);
    }
    @Override
    @Transactional
    public List<TeamResponseDto> getTeamList(User user) {
        List<Team> teams = teamRepository.findAll();
        if(user.isTeamLeader()){
            teams.removeIf(team -> !team.getLeader().getId().equals(user.getId()));
        }else{
            teams.removeIf(team -> team.getStudentList().stream().noneMatch(student -> student.getUser().getId().equals(user.getId())));
        }
        List<TeamResponseDto> response = new ArrayList<>();
        teams.forEach(team -> {
            if(team.getStudentList()==null){
                team.setStudentList(new ArrayList<>());
            }
            response.add(new TeamResponseDto(team, team.getStudentList().size()));
        });

        return response;
    }
    @Override
    public List<StudentResponseDto> getStudentByTeamId(Long teamId) {
        List<Student> allByTeamId = studentRepository.findAllByTeamId(teamId);
        allByTeamId.forEach(student ->student.setMarks(student.getMarks().stream()
                .filter(CommonMethods.distinctByKey(Mark::getId)).collect(Collectors.toList())));
        List<StudentResponseDto> response = new ArrayList<>();
        allByTeamId.forEach(student -> response.add(new StudentResponseDto(student)));
        return response;
    }
    @Override
    public List<StudentResponseDto> getAllStudentsWhichAreNotInTeam(Long teamId) {
        List<Student> allStudents = studentRepository.findAll();
        List<Student> studentsNotFromThisTeam = allStudents.stream().filter(student -> student.getTeam() == null
                || !student.getTeam().getId().equals(teamId)).collect(Collectors.toList());
        List<StudentResponseDto> response = new ArrayList<>();
        for (Student student : studentsNotFromThisTeam) {
            response.add(new StudentResponseDto(student));
        }
        return response;
    }
    @Override
    public void addStudentToAnotherTeam(Long teamId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setTeam(teamRepository.findById(teamId).orElseThrow(NullPointerException::new));
        studentRepository.saveAndFlush(student);
    }
    @Override
    public void removeStudentFromTeam(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setTeam(null);
        studentRepository.saveAndFlush(student);
    }
    @Override
    public StudentResponseDto getStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(NullPointerException::new);
        student.setMarks(student.getMarks().stream().filter(CommonMethods.distinctByKey(Mark::getId)).collect(Collectors.toList()));
        return new StudentResponseDto(student);
    }
    @Override
    public StudentResponseDto updateStudent(Long studentId, StudentUpdateRequestDto updateStudent, Map<String, String> form) {
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
        return new StudentResponseDto(studentRepository.saveAndFlush(student));
    }
}
