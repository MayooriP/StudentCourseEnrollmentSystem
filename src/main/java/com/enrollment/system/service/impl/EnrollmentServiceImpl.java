package com.enrollment.system.service.impl;

import com.enrollment.system.dto.EnrollmentDTO;
import com.enrollment.system.exception.EnrollmentException;
import com.enrollment.system.exception.ResourceNotFoundException;
import com.enrollment.system.model.Course;
import com.enrollment.system.model.Enrollment;
import com.enrollment.system.model.Enrollment.EnrollmentStatus;
import com.enrollment.system.model.Schedule;
import com.enrollment.system.model.Student;
import com.enrollment.system.repository.CourseRepository;
import com.enrollment.system.repository.EnrollmentRepository;
import com.enrollment.system.repository.ScheduleRepository;
import com.enrollment.system.repository.StudentRepository;
import com.enrollment.system.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            ScheduleRepository scheduleRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentDTO getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        return convertToDTO(enrollment);
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EnrollmentDTO enrollStudentInCourse(String studentId, String courseCode) {
        // Get student and course
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        // Check if student is already enrolled in the course
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new EnrollmentException("Student is already enrolled in this course");
        }
        
        // Check prerequisites
        if (!checkPrerequisites(studentId, courseCode)) {
            throw new EnrollmentException("Student does not meet prerequisites for this course");
        }
        
        // Check course capacity
        if (!checkCourseCapacity(courseCode)) {
            throw new EnrollmentException("Course has reached maximum capacity");
        }
        
        // Check for time conflicts with current semester
        List<Schedule> courseSchedules = scheduleRepository.findByCourseId(course.getId());
        if (!courseSchedules.isEmpty()) {
            String semester = courseSchedules.get(0).getSemester();
            if (!checkTimeConflict(studentId, courseCode, semester)) {
                throw new EnrollmentException("Course has time conflict with student's schedule");
            }
        }
        
        // Create new enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(savedEnrollment);
    }

    @Override
    @Transactional
    public EnrollmentDTO dropCourse(String studentId, String courseCode) {
        // Get student and course
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        // Find enrollment
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                .orElseThrow(() -> new EnrollmentException("Student is not enrolled in this course"));
        
        // Update enrollment status
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(updatedEnrollment);
    }

    @Override
    public boolean checkPrerequisites(String studentId, String courseCode) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        // If no prerequisites, return true
        if (course.getPrerequisites() == null || course.getPrerequisites().isEmpty()) {
            return true;
        }
        
        // Get all courses the student has completed
        List<Course> completedCourses = courseRepository.findEnrolledCoursesByStudentId(student.getId());
        
        // Check if all prerequisites are in the completed courses
        return course.getPrerequisites().stream()
                .allMatch(prerequisite -> completedCourses.contains(prerequisite));
    }

    @Override
    public boolean checkTimeConflict(String studentId, String courseCode, String semester) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        // Get schedules for the course
        List<Schedule> courseSchedules = scheduleRepository.findByCourseId(course.getId());
        
        // If no schedules, return true (no conflicts)
        if (courseSchedules.isEmpty()) {
            return true;
        }
        
        // Check each schedule for conflicts
        for (Schedule schedule : courseSchedules) {
            if (scheduleRepository.hasTimeConflict(
                    student.getId(), 
                    schedule.getDayOfWeek(), 
                    schedule.getStartTime(), 
                    schedule.getEndTime(), 
                    semester)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public boolean checkCourseCapacity(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + courseCode));
        
        int currentEnrollment = enrollmentRepository.countEnrolledStudentsByCourseId(course.getId());
        
        return currentEnrollment < course.getMaxCapacity();
    }
    
    // Helper method to convert Enrollment entity to EnrollmentDTO
    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getStudentId());
        dto.setCourseCode(enrollment.getCourse().getCourseCode());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus());
        dto.setNotes(enrollment.getNotes());
        return dto;
    }
}
