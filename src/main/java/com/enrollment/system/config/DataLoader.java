package com.enrollment.system.config;

import com.enrollment.system.model.Course;
import com.enrollment.system.model.Enrollment;
import com.enrollment.system.model.Schedule;
import com.enrollment.system.model.Student;
import com.enrollment.system.repository.CourseRepository;
import com.enrollment.system.repository.EnrollmentRepository;
import com.enrollment.system.repository.ScheduleRepository;
import com.enrollment.system.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

// Disabled by default - only used for initial data loading if needed
// @Component
public class DataLoader implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public DataLoader(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository,
            ScheduleRepository scheduleRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void run(String... args) {
        // This will only run when the dev-data-loader profile is active
        // Create students
        Student student1 = new Student();
        student1.setStudentId("S001");
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("john.doe@example.com");
        student1.setPhoneNumber("1234567890");
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setStudentId("S002");
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane.smith@example.com");
        student2.setPhoneNumber("9876543210");
        studentRepository.save(student2);

        // Create courses
        Course course1 = new Course();
        course1.setCourseCode("CS101");
        course1.setName("Introduction to Programming");
        course1.setDescription("Basic programming concepts using Java");
        course1.setCreditHours(3);
        course1.setMaxCapacity(30);
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("CS102");
        course2.setName("Data Structures");
        course2.setDescription("Fundamental data structures and algorithms");
        course2.setCreditHours(4);
        course2.setMaxCapacity(25);
        courseRepository.save(course2);

        Course course3 = new Course();
        course3.setCourseCode("CS201");
        course3.setName("Database Systems");
        course3.setDescription("Introduction to database design and SQL");
        course3.setCreditHours(3);
        course3.setMaxCapacity(20);
        courseRepository.save(course3);

        // Set prerequisites
        course2.addPrerequisite(course1);
        courseRepository.save(course2);

        course3.addPrerequisite(course1);
        courseRepository.save(course3);

        // Create schedules
        Schedule schedule1 = new Schedule();
        schedule1.setCourse(course1);
        schedule1.setDayOfWeek(DayOfWeek.MONDAY);
        schedule1.setStartTime(LocalTime.of(9, 0));
        schedule1.setEndTime(LocalTime.of(10, 30));
        schedule1.setRoom("Room 101");
        schedule1.setSemester("Fall 2023");
        scheduleRepository.save(schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setCourse(course1);
        schedule2.setDayOfWeek(DayOfWeek.WEDNESDAY);
        schedule2.setStartTime(LocalTime.of(9, 0));
        schedule2.setEndTime(LocalTime.of(10, 30));
        schedule2.setRoom("Room 101");
        schedule2.setSemester("Fall 2023");
        scheduleRepository.save(schedule2);

        Schedule schedule3 = new Schedule();
        schedule3.setCourse(course2);
        schedule3.setDayOfWeek(DayOfWeek.TUESDAY);
        schedule3.setStartTime(LocalTime.of(13, 0));
        schedule3.setEndTime(LocalTime.of(14, 30));
        schedule3.setRoom("Room 102");
        schedule3.setSemester("Fall 2023");
        scheduleRepository.save(schedule3);

        Schedule schedule4 = new Schedule();
        schedule4.setCourse(course2);
        schedule4.setDayOfWeek(DayOfWeek.THURSDAY);
        schedule4.setStartTime(LocalTime.of(13, 0));
        schedule4.setEndTime(LocalTime.of(14, 30));
        schedule4.setRoom("Room 102");
        schedule4.setSemester("Fall 2023");
        scheduleRepository.save(schedule4);

        Schedule schedule5 = new Schedule();
        schedule5.setCourse(course3);
        schedule5.setDayOfWeek(DayOfWeek.FRIDAY);
        schedule5.setStartTime(LocalTime.of(10, 0));
        schedule5.setEndTime(LocalTime.of(11, 30));
        schedule5.setRoom("Room 103");
        schedule5.setSemester("Fall 2023");
        scheduleRepository.save(schedule5);

        // Create enrollments
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudent(student1);
        enrollment1.setCourse(course1);
        enrollment1.setEnrollmentDate(LocalDateTime.now().minusDays(10));
        enrollment1.setStatus(Enrollment.EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudent(student2);
        enrollment2.setCourse(course1);
        enrollment2.setEnrollmentDate(LocalDateTime.now().minusDays(8));
        enrollment2.setStatus(Enrollment.EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(enrollment2);
    }
}

