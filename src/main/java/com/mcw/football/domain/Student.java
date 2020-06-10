package com.mcw.football.domain;

import com.google.common.base.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DP_STUDENT")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_name")
    private String group;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<Mark> marks = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    public Student() {
    }
    public Student(User user) {
        this.user=user;
        if(user.getStudent()==null || Strings.isNullOrEmpty(user.getStudent().getGroup())){
            this.group="";
        }
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }
}

