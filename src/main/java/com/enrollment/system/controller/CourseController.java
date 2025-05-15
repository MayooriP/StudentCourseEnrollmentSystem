package com.enrollment.system.controller;

import com.enrollment.system.dto.CourseDTO;
import com.enrollment.system.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/code/{courseCode}")
    public ResponseEntity<CourseDTO> getCourseByCourseCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCourseCode(courseCode));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CourseDTO>> getEnrolledCoursesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.getEnrolledCoursesByStudentId(studentId));
    }

    @GetMapping("/available/student/{studentId}")
    public ResponseEntity<List<CourseDTO>> getAvailableCoursesByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(courseService.getAvailableCoursesByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
        return new ResponseEntity<>(courseService.createCourse(courseDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseCode}/prerequisites/{prerequisiteCode}")
    public ResponseEntity<Void> addPrerequisite(
            @PathVariable String courseCode,
            @PathVariable String prerequisiteCode) {
        courseService.addPrerequisite(courseCode, prerequisiteCode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{courseCode}/prerequisites/{prerequisiteCode}")
    public ResponseEntity<Void> removePrerequisite(
            @PathVariable String courseCode,
            @PathVariable String prerequisiteCode) {
        courseService.removePrerequisite(courseCode, prerequisiteCode);
        return ResponseEntity.noContent().build();
    }
}
