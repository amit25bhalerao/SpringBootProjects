package com.example.ManyToManyRelationship.repository;

import com.example.ManyToManyRelationship.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByNameContaining(String name);
}
