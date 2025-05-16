import api from './api';

const scheduleService = {
  // Get all schedules
  getAllSchedules: async () => {
    const response = await api.get('/schedules');
    return response.data;
  },

  // Get schedule by ID
  getScheduleById: async (id) => {
    const response = await api.get(`/schedules/${id}`);
    return response.data;
  },

  // Get schedules by course ID
  getSchedulesByCourseId: async (courseId) => {
    const response = await api.get(`/schedules/course/${courseId}`);
    return response.data;
  },

  // Get schedules by semester
  getSchedulesBySemester: async (semester) => {
    const response = await api.get(`/schedules/semester/${semester}`);
    return response.data;
  },

  // Get student schedule
  getStudentSchedule: async (studentId, semester) => {
    const response = await api.get(`/schedules/student/${studentId}/semester/${semester}`);
    return response.data;
  },

  // Create a new schedule
  createSchedule: async (scheduleData) => {
    const response = await api.post('/schedules', scheduleData);
    return response.data;
  },

  // Update a schedule
  updateSchedule: async (id, scheduleData) => {
    const response = await api.put(`/schedules/${id}`, scheduleData);
    return response.data;
  },

  // Delete a schedule
  deleteSchedule: async (id) => {
    await api.delete(`/schedules/${id}`);
    return true;
  },
};

export default scheduleService;
