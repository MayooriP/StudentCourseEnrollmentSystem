package com.enrollment.system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "course_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "Enrollment date is required")
    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Enrollment status is required")
    private EnrollmentStatus status;

    @Column(length = 500)
    private String notes;

    // Enum for enrollment status
    public enum EnrollmentStatus {
        ENROLLED, DROPPED, COMPLETED, WAITLISTED
    }
}
