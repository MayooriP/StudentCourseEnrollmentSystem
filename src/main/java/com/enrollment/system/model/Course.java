package com.enrollment.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course code is required")
    @Column(unique = true, nullable = false)
    private String courseCode;

    @NotBlank(message = "Course name is required")
    private String name;

    @NotBlank(message = "Course description is required")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Credit hours are required")
    @Min(value = 1, message = "Credit hours must be at least 1")
    private Integer creditHours;

    @NotNull(message = "Maximum capacity is required")
    @Min(value = 1, message = "Maximum capacity must be at least 1")
    private Integer maxCapacity;

    @ManyToMany
    @JoinTable(
        name = "course_prerequisites",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private Set<Course> prerequisites = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schedule> schedules = new HashSet<>();

    // Helper method to add enrollment
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    // Helper method to remove enrollment
    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }

    // Helper method to add prerequisite
    public void addPrerequisite(Course prerequisite) {
        prerequisites.add(prerequisite);
    }

    // Helper method to remove prerequisite
    public void removePrerequisite(Course prerequisite) {
        prerequisites.remove(prerequisite);
    }

    // Helper method to add schedule
    public void addSchedule(Schedule schedule) {
        schedules.add(schedule);
        schedule.setCourse(this);
    }

    // Helper method to remove schedule
    public void removeSchedule(Schedule schedule) {
        schedules.remove(schedule);
        schedule.setCourse(null);
    }
}
