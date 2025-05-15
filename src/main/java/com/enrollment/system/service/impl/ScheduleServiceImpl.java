package com.enrollment.system.service.impl;

import com.enrollment.system.dto.ScheduleDTO;
import com.enrollment.system.exception.EnrollmentException;
import com.enrollment.system.exception.ResourceNotFoundException;
import com.enrollment.system.model.Course;
import com.enrollment.system.model.Schedule;
import com.enrollment.system.model.Student;
import com.enrollment.system.repository.CourseRepository;
import com.enrollment.system.repository.ScheduleRepository;
import com.enrollment.system.repository.StudentRepository;
import com.enrollment.system.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ScheduleServiceImpl(
            ScheduleRepository scheduleRepository,
            CourseRepository courseRepository,
            StudentRepository studentRepository) {
        this.scheduleRepository = scheduleRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        return convertToDTO(schedule);
    }

    @Override
    public List<ScheduleDTO> getSchedulesByCourseId(Long courseId) {
        return scheduleRepository.findByCourseId(courseId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getSchedulesBySemester(String semester) {
        return scheduleRepository.findBySemester(semester).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleDTO> getStudentSchedule(String studentId, String semester) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        
        return scheduleRepository.findStudentScheduleByStudentIdAndSemester(student.getId(), semester).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Course course = courseRepository.findByCourseCode(scheduleDTO.getCourseCode())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + scheduleDTO.getCourseCode()));
        
        // Validate time range
        if (scheduleDTO.getStartTime().isAfter(scheduleDTO.getEndTime())) {
            throw new EnrollmentException("Start time must be before end time");
        }
        
        Schedule schedule = convertToEntity(scheduleDTO, course);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(savedSchedule);
    }

    @Override
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        Course course = courseRepository.findByCourseCode(scheduleDTO.getCourseCode())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with course code: " + scheduleDTO.getCourseCode()));
        
        // Validate time range
        if (scheduleDTO.getStartTime().isAfter(scheduleDTO.getEndTime())) {
            throw new EnrollmentException("Start time must be before end time");
        }
        
        schedule.setCourse(course);
        schedule.setDayOfWeek(scheduleDTO.getDayOfWeek());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setRoom(scheduleDTO.getRoom());
        schedule.setSemester(scheduleDTO.getSemester());
        
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return convertToDTO(updatedSchedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }
    
    // Helper method to convert Schedule entity to ScheduleDTO
    private ScheduleDTO convertToDTO(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(schedule.getId());
        dto.setCourseCode(schedule.getCourse().getCourseCode());
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setRoom(schedule.getRoom());
        dto.setSemester(schedule.getSemester());
        return dto;
    }
    
    // Helper method to convert ScheduleDTO to Schedule entity
    private Schedule convertToEntity(ScheduleDTO dto, Course course) {
        Schedule schedule = new Schedule();
        schedule.setId(dto.getId());
        schedule.setCourse(course);
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setRoom(dto.getRoom());
        schedule.setSemester(dto.getSemester());
        return schedule;
    }
}
