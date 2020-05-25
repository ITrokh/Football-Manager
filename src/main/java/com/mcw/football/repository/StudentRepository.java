package com.mcw.football.repository;

import com.mcw.football.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByTeamId(Long teamId);
    Student findByUserId(Long id);

}
