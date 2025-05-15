package com.enrollment.system.controller;

import com.enrollment.system.dto.EnrollmentDTO;
import com.enrollment.system.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentDTO> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    @PostMapping("/enroll")
    public ResponseEntity<EnrollmentDTO> enrollStudentInCourse(@RequestBody Map<String, String> enrollmentRequest) {
        String studentId = enrollmentRequest.get("studentId");
        String courseCode = enrollmentRequest.get("courseCode");
        
        return new ResponseEntity<>(
                enrollmentService.enrollStudentInCourse(studentId, courseCode),
                HttpStatus.CREATED);
    }

    @PostMapping("/drop")
    public ResponseEntity<EnrollmentDTO> dropCourse(@RequestBody Map<String, String> dropRequest) {
        String studentId = dropRequest.get("studentId");
        String courseCode = dropRequest.get("courseCode");
        
        return ResponseEntity.ok(enrollmentService.dropCourse(studentId, courseCode));
    }

    @GetMapping("/check-prerequisites")
    public ResponseEntity<Boolean> checkPrerequisites(
            @RequestParam String studentId,
            @RequestParam String courseCode) {
        return ResponseEntity.ok(enrollmentService.checkPrerequisites(studentId, courseCode));
    }

    @GetMapping("/check-time-conflict")
    public ResponseEntity<Boolean> checkTimeConflict(
            @RequestParam String studentId,
            @RequestParam String courseCode,
            @RequestParam String semester) {
        return ResponseEntity.ok(enrollmentService.checkTimeConflict(studentId, courseCode, semester));
    }

    @GetMapping("/check-capacity")
    public ResponseEntity<Boolean> checkCourseCapacity(@RequestParam String courseCode) {
        return ResponseEntity.ok(enrollmentService.checkCourseCapacity(courseCode));
    }
}
