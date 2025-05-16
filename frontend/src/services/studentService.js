import api from './api';

const studentService = {
  // Get all students
  getAllStudents: async () => {
    const response = await api.get('/students');
    return response.data;
  },

  // Get student by ID
  getStudentById: async (id) => {
    const response = await api.get(`/students/${id}`);
    return response.data;
  },

  // Get student by student ID
  getStudentByStudentId: async (studentId) => {
    const response = await api.get(`/students/studentId/${studentId}`);
    return response.data;
  },

  // Create a new student
  createStudent: async (studentData) => {
    const response = await api.post('/students', studentData);
    return response.data;
  },

  // Update a student
  updateStudent: async (id, studentData) => {
    const response = await api.put(`/students/${id}`, studentData);
    return response.data;
  },

  // Delete a student
  deleteStudent: async (id) => {
    await api.delete(`/students/${id}`);
    return true;
  },
};

export default studentService;
