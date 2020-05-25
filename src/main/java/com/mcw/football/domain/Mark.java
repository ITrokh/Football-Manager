package com.mcw.football.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DP_MARK")
@Data
@NoArgsConstructor
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean mark;

    private LocalDate createdDate;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_MARK_USER_ID"), nullable = false)
    private Student student;

    public Mark(Student student, boolean mark) {
        this.student=student;
        this.mark=mark;
    }
}
