package com.enrollment.system.dto;

import com.enrollment.system.model.Enrollment.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private String studentId;
    private String courseCode;
    private LocalDateTime enrollmentDate;
    private EnrollmentStatus status;
    private String notes;
}
