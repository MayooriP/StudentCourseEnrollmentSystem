package com.enrollment.system.repository;

import com.enrollment.system.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByCourseId(Long courseId);
    List<Schedule> findBySemester(String semester);
    
    @Query("SELECT s FROM Schedule s WHERE s.course.id IN " +
           "(SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ENROLLED') " +
           "AND s.semester = :semester")
    List<Schedule> findStudentScheduleByStudentIdAndSemester(Long studentId, String semester);
    
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s " +
           "WHERE s.dayOfWeek = :dayOfWeek AND " +
           "((s.startTime <= :endTime AND s.endTime >= :startTime)) AND " +
           "s.course.id IN (SELECT e.course.id FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'ENROLLED') AND " +
           "s.semester = :semester")
    boolean hasTimeConflict(Long studentId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, String semester);
}
