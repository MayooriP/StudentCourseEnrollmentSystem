package com.enrollment.system.repository;

import com.enrollment.system.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseCode(String courseCode);
    boolean existsByCourseCode(String courseCode);
    
    @Query("SELECT c FROM Course c WHERE c.id IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ENROLLED')")
    List<Course> findEnrolledCoursesByStudentId(Long studentId);
    
    @Query("SELECT c FROM Course c WHERE c.id NOT IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId)")
    List<Course> findAvailableCoursesByStudentId(Long studentId);
}
