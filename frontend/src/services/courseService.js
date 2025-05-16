import api from './api';

const courseService = {
  // Get all courses
  getAllCourses: async () => {
    const response = await api.get('/courses');
    return response.data;
  },

  // Get course by ID
  getCourseById: async (id) => {
    const response = await api.get(`/courses/${id}`);
    return response.data;
  },

  // Get course by course code
  getCourseByCourseCode: async (courseCode) => {
    const response = await api.get(`/courses/code/${courseCode}`);
    return response.data;
  },

  // Get enrolled courses by student ID
  getEnrolledCoursesByStudentId: async (studentId) => {
    const response = await api.get(`/courses/student/${studentId}`);
    return response.data;
  },

  // Get available courses by student ID
  getAvailableCoursesByStudentId: async (studentId) => {
    const response = await api.get(`/courses/available/student/${studentId}`);
    return response.data;
  },

  // Create a new course
  createCourse: async (courseData) => {
    const response = await api.post('/courses', courseData);
    return response.data;
  },

  // Update a course
  updateCourse: async (id, courseData) => {
    const response = await api.put(`/courses/${id}`, courseData);
    return response.data;
  },

  // Delete a course
  deleteCourse: async (id) => {
    await api.delete(`/courses/${id}`);
    return true;
  },

  // Add prerequisite to a course
  addPrerequisite: async (courseCode, prerequisiteCode) => {
    await api.post(`/courses/${courseCode}/prerequisites/${prerequisiteCode}`);
    return true;
  },

  // Remove prerequisite from a course
  removePrerequisite: async (courseCode, prerequisiteCode) => {
    await api.delete(`/courses/${courseCode}/prerequisites/${prerequisiteCode}`);
    return true;
  },
};

export default courseService;
