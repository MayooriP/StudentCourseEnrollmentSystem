import api from './api';

const enrollmentService = {
  // Get all enrollments
  getAllEnrollments: async () => {
    const response = await api.get('/enrollments');
    return response.data;
  },

  // Get enrollment by ID
  getEnrollmentById: async (id) => {
    const response = await api.get(`/enrollments/${id}`);
    return response.data;
  },

  // Get enrollments by student ID
  getEnrollmentsByStudentId: async (studentId) => {
    const response = await api.get(`/enrollments/student/${studentId}`);
    return response.data;
  },

  // Get enrollments by course ID
  getEnrollmentsByCourseId: async (courseId) => {
    const response = await api.get(`/enrollments/course/${courseId}`);
    return response.data;
  },

  // Enroll student in course
  enrollStudentInCourse: async (studentId, courseCode) => {
    const response = await api.post('/enrollments/enroll', { studentId, courseCode });
    return response.data;
  },

  // Drop course
  dropCourse: async (studentId, courseCode) => {
    const response = await api.post('/enrollments/drop', { studentId, courseCode });
    return response.data;
  },

  // Check prerequisites
  checkPrerequisites: async (studentId, courseCode) => {
    const response = await api.get(`/enrollments/check-prerequisites?studentId=${studentId}&courseCode=${courseCode}`);
    return response.data;
  },

  // Check time conflict
  checkTimeConflict: async (studentId, courseCode, semester) => {
    const response = await api.get(`/enrollments/check-time-conflict?studentId=${studentId}&courseCode=${courseCode}&semester=${semester}`);
    return response.data;
  },

  // Check course capacity
  checkCourseCapacity: async (courseCode) => {
    const response = await api.get(`/enrollments/check-capacity?courseCode=${courseCode}`);
    return response.data;
  },
};

export default enrollmentService;
