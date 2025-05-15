package com.enrollment.system.service;

import com.enrollment.system.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentDTO> getAllEnrollments();
    EnrollmentDTO getEnrollmentById(Long id);
    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);
    List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId);
    EnrollmentDTO enrollStudentInCourse(String studentId, String courseCode);
    EnrollmentDTO dropCourse(String studentId, String courseCode);
    boolean checkPrerequisites(String studentId, String courseCode);
    boolean checkTimeConflict(String studentId, String courseCode, String semester);
    boolean checkCourseCapacity(String courseCode);
}
