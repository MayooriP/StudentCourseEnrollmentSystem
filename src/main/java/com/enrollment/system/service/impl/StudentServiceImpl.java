package com.enrollment.system.service.impl;

import com.enrollment.system.dto.StudentDTO;
import com.enrollment.system.exception.EnrollmentException;
import com.enrollment.system.exception.ResourceNotFoundException;
import com.enrollment.system.model.Student;
import com.enrollment.system.repository.StudentRepository;
import com.enrollment.system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return convertToDTO(student);
    }

    @Override
    public StudentDTO getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with student ID: " + studentId));
        return convertToDTO(student);
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Check if student ID already exists
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new EnrollmentException("Student ID already exists: " + studentDTO.getStudentId());
        }
        
        // Check if email already exists
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new EnrollmentException("Email already exists: " + studentDTO.getEmail());
        }
        
        Student student = convertToEntity(studentDTO);
        Student savedStudent = studentRepository.save(student);
        return convertToDTO(savedStudent);
    }

    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        
        // Check if student ID already exists for another student
        if (!student.getStudentId().equals(studentDTO.getStudentId()) && 
                studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new EnrollmentException("Student ID already exists: " + studentDTO.getStudentId());
        }
        
        // Check if email already exists for another student
        if (!student.getEmail().equals(studentDTO.getEmail()) && 
                studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new EnrollmentException("Email already exists: " + studentDTO.getEmail());
        }
        
        student.setStudentId(studentDTO.getStudentId());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setPhoneNumber(studentDTO.getPhoneNumber());
        
        Student updatedStudent = studentRepository.save(student);
        return convertToDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
    
    // Helper method to convert Student entity to StudentDTO
    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPhoneNumber(student.getPhoneNumber());
        return dto;
    }
    
    // Helper method to convert StudentDTO to Student entity
    private Student convertToEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPhoneNumber(dto.getPhoneNumber());
        return student;
    }
}
