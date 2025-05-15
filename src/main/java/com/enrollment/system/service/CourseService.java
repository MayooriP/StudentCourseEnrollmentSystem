package com.enrollment.system.service;

import com.enrollment.system.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseById(Long id);
    CourseDTO getCourseByCourseCode(String courseCode);
    CourseDTO createCourse(CourseDTO courseDTO);
    CourseDTO updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
    List<CourseDTO> getEnrolledCoursesByStudentId(Long studentId);
    List<CourseDTO> getAvailableCoursesByStudentId(Long studentId);
    void addPrerequisite(String courseCode, String prerequisiteCode);
    void removePrerequisite(String courseCode, String prerequisiteCode);
}
