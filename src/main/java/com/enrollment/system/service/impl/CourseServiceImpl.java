package com.enrollment.system.service.impl;

import com.enrollment.system.dto.CourseDTO;
import com.enrollment.system.exception.EnrollmentException;
import com.enrollment.system.exception.ResourceNotFoundException;
import com.enrollment.system.model.Course;
import com.enrollment.system.repository.CourseRepository;
import com.enrollment.system.repository.EnrollmentRepository;
import com.enrollment.system.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return convertToDTO(course);
    }

    @Override
    public CourseDTO getCourseByCourseCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        return convertToDTO(course);
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        // Check if course code already exists
        if (courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new EnrollmentException("Course code already exists: " + courseDTO.getCourseCode());
        }
        
        Course course = convertToEntity(courseDTO);
        
        // Add prerequisites if any
        if (courseDTO.getPrerequisiteCodes() != null && !courseDTO.getPrerequisiteCodes().isEmpty()) {
            for (String prerequisiteCode : courseDTO.getPrerequisiteCodes()) {
                Course prerequisite = courseRepository.findByCourseCode(prerequisiteCode)
                        .orElseThrow(() -> new ResourceNotFoundException("Prerequisite course not found with code: " + prerequisiteCode));
                course.addPrerequisite(prerequisite);
            }
        }
        
        Course savedCourse = courseRepository.save(course);
        return convertToDTO(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        
        // Check if course code already exists for another course
        if (!course.getCourseCode().equals(courseDTO.getCourseCode()) && 
                courseRepository.existsByCourseCode(courseDTO.getCourseCode())) {
            throw new EnrollmentException("Course code already exists: " + courseDTO.getCourseCode());
        }
        
        course.setCourseCode(courseDTO.getCourseCode());
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setCreditHours(courseDTO.getCreditHours());
        course.setMaxCapacity(courseDTO.getMaxCapacity());
        
        Course updatedCourse = courseRepository.save(course);
        return convertToDTO(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Override
    public List<CourseDTO> getEnrolledCoursesByStudentId(Long studentId) {
        return courseRepository.findEnrolledCoursesByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDTO> getAvailableCoursesByStudentId(Long studentId) {
        return courseRepository.findAvailableCoursesByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addPrerequisite(String courseCode, String prerequisiteCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        Course prerequisite = courseRepository.findByCourseCode(prerequisiteCode)
                .orElseThrow(() -> new ResourceNotFoundException("Prerequisite course not found with code: " + prerequisiteCode));
        
        course.addPrerequisite(prerequisite);
        courseRepository.save(course);
    }

    @Override
    public void removePrerequisite(String courseCode, String prerequisiteCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        Course prerequisite = courseRepository.findByCourseCode(prerequisiteCode)
                .orElseThrow(() -> new ResourceNotFoundException("Prerequisite course not found with code: " + prerequisiteCode));
        
        course.removePrerequisite(prerequisite);
        courseRepository.save(course);
    }
    
    // Helper method to convert Course entity to CourseDTO
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCreditHours(course.getCreditHours());
        dto.setMaxCapacity(course.getMaxCapacity());
        
        // Set prerequisites
        if (course.getPrerequisites() != null && !course.getPrerequisites().isEmpty()) {
            dto.setPrerequisiteCodes(course.getPrerequisites().stream()
                    .map(Course::getCourseCode)
                    .collect(Collectors.toSet()));
        }
        
        // Set current enrollment count
        if (course.getId() != null) {
            dto.setCurrentEnrollment(enrollmentRepository.countEnrolledStudentsByCourseId(course.getId()));
        }
        
        return dto;
    }
    
    // Helper method to convert CourseDTO to Course entity
    private Course convertToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setCourseCode(dto.getCourseCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setCreditHours(dto.getCreditHours());
        course.setMaxCapacity(dto.getMaxCapacity());
        return course;
    }
}
