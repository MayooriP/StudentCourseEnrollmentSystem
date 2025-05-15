package com.enrollment.system.service;

import com.enrollment.system.dto.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    List<ScheduleDTO> getAllSchedules();
    ScheduleDTO getScheduleById(Long id);
    List<ScheduleDTO> getSchedulesByCourseId(Long courseId);
    List<ScheduleDTO> getSchedulesBySemester(String semester);
    List<ScheduleDTO> getStudentSchedule(String studentId, String semester);
    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);
    ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO);
    void deleteSchedule(Long id);
}
