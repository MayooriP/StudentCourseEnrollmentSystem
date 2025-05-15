package com.enrollment.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    
    @NotBlank(message = "Course code is required")
    private String courseCode;
    
    @NotBlank(message = "Course name is required")
    private String name;
    
    @NotBlank(message = "Course description is required")
    private String description;
    
    @NotNull(message = "Credit hours are required")
    @Min(value = 1, message = "Credit hours must be at least 1")
    private Integer creditHours;
    
    @NotNull(message = "Maximum capacity is required")
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    private Integer maxCapacity;
    
    private Set<String> prerequisiteCodes = new HashSet<>();
    private int currentEnrollment;
}
