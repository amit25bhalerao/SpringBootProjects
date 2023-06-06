package com.example.ManyToManyRelationship.repository;

import com.example.ManyToManyRelationship.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByFeeLessThan(double fee);
}
