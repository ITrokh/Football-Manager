package com.mcw.football.domain.dto;

import com.mcw.football.domain.Mark;
import com.mcw.football.domain.Student;
import lombok.Data;

import java.util.List;

@Data
public class StudentResponse {

    private Long id;
    private String name;
    private String userName;
    private String group;
    private Long teamId;
    private Double salary;
    private String state;
    private String position;
    private List<Mark> marks;
    private Integer total;
    public StudentResponse(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getUser().getFullName();
        this.group=student.getGroup();
        this.userName=student.getUser().getUsername();
        this.marks= student.getMarks();
        if(this.marks.size()<25){
            int size = marks.size();
            for(int i=size; i<25; i++){
                marks.add(i, new Mark(student,false));
            }
        }
        this.total=marks.stream().mapToInt(mark->{
            if(mark.isMark()) return 4;
            return 0;
        }).sum();
        /*this.salary = student.getSalary() != null ? student.getSalary() : 0.0;
        this.state = student.getState() != null ? student.getState() : "";
        this.position = student.getPosition() != null ? student.getPosition() : "";*/
    }
}
