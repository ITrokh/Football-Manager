package com.mcw.football.domain;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DP_STUDENT")
@Data
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_name")
    private String group;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<Mark> marks = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name="team_id")
    private Team team;

    public Student(User user) {
        this.user=user;
        if(user.getStudent()==null || Strings.isNullOrEmpty(user.getStudent().getGroup())){
            this.group="";
        }
    }
}

