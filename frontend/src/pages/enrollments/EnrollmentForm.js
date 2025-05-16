import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  MenuItem,
  Divider,
  Stepper,
  Step,
  StepLabel,
  Alert,
  CircularProgress,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ErrorIcon from '@mui/icons-material/Error';
import PageHeader from '../../components/common/PageHeader';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import AlertMessage from '../../components/common/AlertMessage';
import studentService from '../../services/studentService';
import courseService from '../../services/courseService';
import enrollmentService from '../../services/enrollmentService';

const steps = ['Select Student', 'Select Course', 'Confirm Enrollment'];

const EnrollmentForm = () => {
  const navigate = useNavigate();
  
  const [activeStep, setActiveStep] = useState(0);
  const [loading, setLoading] = useState(true);
  const [students, setStudents] = useState([]);
  const [courses, setCourses] = useState([]);
  const [availableCourses, setAvailableCourses] = useState([]);
  const [selectedStudent, setSelectedStudent] = useState('');
  const [selectedCourse, setSelectedCourse] = useState('');
  const [semester, setSemester] = useState('Fall 2023');
  const [checkingPrerequisites, setCheckingPrerequisites] = useState(false);
  const [checkingTimeConflict, setCheckingTimeConflict] = useState(false);
  const [checkingCapacity, setCheckingCapacity] = useState(false);
  const [prerequisites, setPrerequisites] = useState({ checked: false, passed: false });
  const [timeConflict, setTimeConflict] = useState({ checked: false, passed: false });
  const [capacity, setCapacity] = useState({ checked: false, passed: false });
  const [alert, setAlert] = useState({ open: false, message: '', severity: 'success' });

  useEffect(() => {
    fetchInitialData();
  }, []);

  useEffect(() => {
    if (selectedStudent) {
      fetchAvailableCourses(selectedStudent);
    }
  }, [selectedStudent]);

  const fetchInitialData = async () => {
    try {
      setLoading(true);
      const [studentsData, coursesData] = await Promise.all([
        studentService.getAllStudents(),
        courseService.getAllCourses(),
      ]);
      setStudents(studentsData);
      setCourses(coursesData);
    } catch (error) {
      console.error('Error fetching initial data:', error);
      setAlert({
        open: true,
        message: 'Failed to load data',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchAvailableCourses = async (studentId) => {
    try {
      setLoading(true);
      const data = await courseService.getAvailableCoursesByStudentId(studentId);
      setAvailableCourses(data);
    } catch (error) {
      console.error('Error fetching available courses:', error);
      setAlert({
        open: true,
        message: 'Failed to load available courses',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleStudentChange = (e) => {
    setSelectedStudent(e.target.value);
    setSelectedCourse('');
    resetChecks();
  };

  const handleCourseChange = (e) => {
    setSelectedCourse(e.target.value);
    resetChecks();
  };

  const resetChecks = () => {
    setPrerequisites({ checked: false, passed: false });
    setTimeConflict({ checked: false, passed: false });
    setCapacity({ checked: false, passed: false });
  };

  const handleNext = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    
    if (activeStep === 1) {
      checkEnrollmentRequirements();
    }
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const checkEnrollmentRequirements = async () => {
    try {
      // Check prerequisites
      setCheckingPrerequisites(true);
      const student = students.find(s => s.id === selectedStudent);
      const course = courses.find(c => c.id === selectedCourse);
      
      const prereqResult = await enrollmentService.checkPrerequisites(
        student.studentId,
        course.courseCode
      );
      setPrerequisites({ checked: true, passed: prereqResult });
      setCheckingPrerequisites(false);
      
      // Check time conflict
      setCheckingTimeConflict(true);
      const conflictResult = await enrollmentService.checkTimeConflict(
        student.studentId,
        course.courseCode,
        semester
      );
      setTimeConflict({ checked: true, passed: !conflictResult });
      setCheckingTimeConflict(false);
      
      // Check capacity
      setCheckingCapacity(true);
      const capacityResult = await enrollmentService.checkCourseCapacity(
        course.courseCode
      );
      setCapacity({ checked: true, passed: capacityResult });
      setCheckingCapacity(false);
    } catch (error) {
      console.error('Error checking enrollment requirements:', error);
      setAlert({
        open: true,
        message: 'Failed to check enrollment requirements',
        severity: 'error',
      });
      setCheckingPrerequisites(false);
      setCheckingTimeConflict(false);
      setCheckingCapacity(false);
    }
  };

  const handleEnroll = async () => {
    try {
      setLoading(true);
      const student = students.find(s => s.id === selectedStudent);
      const course = courses.find(c => c.id === selectedCourse);
      
      await enrollmentService.enrollStudentInCourse(
        student.studentId,
        course.courseCode
      );
      
      setAlert({
        open: true,
        message: 'Student enrolled successfully',
        severity: 'success',
      });
      
      // Reset form
      setSelectedStudent('');
      setSelectedCourse('');
      setActiveStep(0);
      resetChecks();
    } catch (error) {
      console.error('Error enrolling student:', error);
      setAlert({
        open: true,
        message: error.response?.data?.message || 'Failed to enroll student',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleAlertClose = () => {
    setAlert({ ...alert, open: false });
  };

  const canProceedToStep2 = selectedStudent !== '';
  const canProceedToStep3 = selectedCourse !== '';
  const canEnroll = prerequisites.passed && timeConflict.passed && capacity.passed;

  if (loading && activeStep !== 2) {
    return <LoadingSpinner />;
  }

  return (
    <Box>
      <PageHeader title="New Enrollment" />

      <Paper sx={{ p: 3, mb: 3 }}>
        <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>

        {activeStep === 0 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Select Student
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <TextField
                  select
                  fullWidth
                  label="Student"
                  value={selectedStudent}
                  onChange={handleStudentChange}
                  helperText="Please select a student to enroll"
                  required
                >
                  {students.map((student) => (
                    <MenuItem key={student.id} value={student.id}>
                      {student.firstName} {student.lastName} ({student.studentId})
                    </MenuItem>
                  ))}
                </TextField>
              </Grid>
            </Grid>
            
            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 3 }}>
              <Button
                variant="outlined"
                startIcon={<ArrowBackIcon />}
                onClick={() => navigate('/enrollments')}
                sx={{ mr: 1 }}
              >
                Cancel
              </Button>
              <Button
                variant="contained"
                onClick={handleNext}
                disabled={!canProceedToStep2}
              >
                Next
              </Button>
            </Box>
          </Box>
        )}

        {activeStep === 1 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Select Course
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            <Grid container spacing={3}>
              <Grid item xs={12}>
                <TextField
                  select
                  fullWidth
                  label="Course"
                  value={selectedCourse}
                  onChange={handleCourseChange}
                  helperText="Please select a course to enroll in"
                  required
                >
                  {availableCourses.length > 0 ? (
                    availableCourses.map((course) => (
                      <MenuItem key={course.id} value={course.id}>
                        {course.name} ({course.courseCode}) - {course.creditHours} credits
                      </MenuItem>
                    ))
                  ) : (
                    <MenuItem disabled>No available courses</MenuItem>
                  )}
                </TextField>
              </Grid>
              
              <Grid item xs={12}>
                <TextField
                  select
                  fullWidth
                  label="Semester"
                  value={semester}
                  onChange={(e) => setSemester(e.target.value)}
                  required
                >
                  <MenuItem value="Fall 2023">Fall 2023</MenuItem>
                  <MenuItem value="Spring 2024">Spring 2024</MenuItem>
                  <MenuItem value="Summer 2024">Summer 2024</MenuItem>
                </TextField>
              </Grid>
            </Grid>
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 3 }}>
              <Button variant="outlined" onClick={handleBack}>
                Back
              </Button>
              <Button
                variant="contained"
                onClick={handleNext}
                disabled={!canProceedToStep3}
              >
                Next
              </Button>
            </Box>
          </Box>
        )}

        {activeStep === 2 && (
          <Box>
            <Typography variant="h6" gutterBottom>
              Confirm Enrollment
            </Typography>
            <Divider sx={{ mb: 2 }} />
            
            <Grid container spacing={3}>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1" gutterBottom>
                  Student
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {students.find(s => s.id === selectedStudent)?.firstName}{' '}
                  {students.find(s => s.id === selectedStudent)?.lastName}{' '}
                  ({students.find(s => s.id === selectedStudent)?.studentId})
                </Typography>
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle1" gutterBottom>
                  Course
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {courses.find(c => c.id === selectedCourse)?.name}{' '}
                  ({courses.find(c => c.id === selectedCourse)?.courseCode})
                </Typography>
              </Grid>
              
              <Grid item xs={12}>
                <Typography variant="subtitle1" gutterBottom>
                  Enrollment Checks
                </Typography>
                
                <Box sx={{ mb: 1, display: 'flex', alignItems: 'center' }}>
                  {checkingPrerequisites ? (
                    <CircularProgress size={20} sx={{ mr: 1 }} />
                  ) : prerequisites.checked ? (
                    prerequisites.passed ? (
                      <CheckCircleIcon color="success" sx={{ mr: 1 }} />
                    ) : (
                      <ErrorIcon color="error" sx={{ mr: 1 }} />
                    )
                  ) : null}
                  <Typography>
                    Prerequisites: {checkingPrerequisites
                      ? 'Checking...'
                      : prerequisites.checked
                      ? prerequisites.passed
                        ? 'Passed'
                        : 'Not met'
                      : 'Not checked'}
                  </Typography>
                </Box>
                
                <Box sx={{ mb: 1, display: 'flex', alignItems: 'center' }}>
                  {checkingTimeConflict ? (
                    <CircularProgress size={20} sx={{ mr: 1 }} />
                  ) : timeConflict.checked ? (
                    timeConflict.passed ? (
                      <CheckCircleIcon color="success" sx={{ mr: 1 }} />
                    ) : (
                      <ErrorIcon color="error" sx={{ mr: 1 }} />
                    )
                  ) : null}
                  <Typography>
                    Schedule Conflict: {checkingTimeConflict
                      ? 'Checking...'
                      : timeConflict.checked
                      ? timeConflict.passed
                        ? 'No conflicts'
                        : 'Conflict detected'
                      : 'Not checked'}
                  </Typography>
                </Box>
                
                <Box sx={{ mb: 1, display: 'flex', alignItems: 'center' }}>
                  {checkingCapacity ? (
                    <CircularProgress size={20} sx={{ mr: 1 }} />
                  ) : capacity.checked ? (
                    capacity.passed ? (
                      <CheckCircleIcon color="success" sx={{ mr: 1 }} />
                    ) : (
                      <ErrorIcon color="error" sx={{ mr: 1 }} />
                    )
                  ) : null}
                  <Typography>
                    Course Capacity: {checkingCapacity
                      ? 'Checking...'
                      : capacity.checked
                      ? capacity.passed
                        ? 'Available'
                        : 'Course full'
                      : 'Not checked'}
                  </Typography>
                </Box>
              </Grid>
              
              {prerequisites.checked && !prerequisites.passed && (
                <Grid item xs={12}>
                  <Alert severity="error">
                    Student does not meet the prerequisites for this course.
                  </Alert>
                </Grid>
              )}
              
              {timeConflict.checked && !timeConflict.passed && (
                <Grid item xs={12}>
                  <Alert severity="error">
                    This course conflicts with another course in the student's schedule.
                  </Alert>
                </Grid>
              )}
              
              {capacity.checked && !capacity.passed && (
                <Grid item xs={12}>
                  <Alert severity="error">
                    This course has reached its maximum capacity.
                  </Alert>
                </Grid>
              )}
            </Grid>
            
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 3 }}>
              <Button variant="outlined" onClick={handleBack}>
                Back
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={handleEnroll}
                disabled={!canEnroll || checkingPrerequisites || checkingTimeConflict || checkingCapacity}
              >
                Enroll
              </Button>
            </Box>
          </Box>
        )}
      </Paper>

      <AlertMessage
        open={alert.open}
        message={alert.message}
        severity={alert.severity}
        onClose={handleAlertClose}
      />
    </Box>
  );
};

export default EnrollmentForm;
