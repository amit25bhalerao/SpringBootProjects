package com.example.XMLtoDatabase.repository;

import com.example.XMLtoDatabase.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository <StudentEntity, Long> {
}
