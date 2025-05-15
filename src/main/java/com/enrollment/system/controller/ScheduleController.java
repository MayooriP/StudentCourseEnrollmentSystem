package com.enrollment.system.controller;

import com.enrollment.system.dto.ScheduleDTO;
import com.enrollment.system.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByCourseId(courseId));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesBySemester(@PathVariable String semester) {
        return ResponseEntity.ok(scheduleService.getSchedulesBySemester(semester));
    }

    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<List<ScheduleDTO>> getStudentSchedule(
            @PathVariable String studentId,
            @PathVariable String semester) {
        return ResponseEntity.ok(scheduleService.getStudentSchedule(studentId, semester));
    }

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        return new ResponseEntity<>(scheduleService.createSchedule(scheduleDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
