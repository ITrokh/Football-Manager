package com.mcw.football.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "DP_TEAM")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate createdDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LEADER_ID", foreignKey = @ForeignKey(name = "FK_TEAM_LEADER_ID"), nullable = false)
    private User leader;
    @OneToMany(mappedBy = "team")
    private List<Student> studentList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
}
