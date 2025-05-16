import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link as RouterLink } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Grid,
  Divider,
  Button,
  Chip,
  Card,
  CardContent,
  CardHeader,
  List,
  ListItem,
  ListItemText,
  Tab,
  Tabs,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SchoolIcon from '@mui/icons-material/School';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import PageHeader from '../../components/common/PageHeader';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import AlertMessage from '../../components/common/AlertMessage';
import studentService from '../../services/studentService';
import courseService from '../../services/courseService';
import enrollmentService from '../../services/enrollmentService';

const StudentDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [loading, setLoading] = useState(true);
  const [student, setStudent] = useState(null);
  const [enrollments, setEnrollments] = useState([]);
  const [courses, setCourses] = useState([]);
  const [tabValue, setTabValue] = useState(0);
  const [alert, setAlert] = useState({ open: false, message: '', severity: 'success' });

  useEffect(() => {
    fetchStudentData();
  }, [id]);

  const fetchStudentData = async () => {
    try {
      setLoading(true);
      const studentData = await studentService.getStudentById(id);
      setStudent(studentData);
      
      const enrollmentsData = await enrollmentService.getEnrollmentsByStudentId(id);
      setEnrollments(enrollmentsData);
      
      const coursesData = await courseService.getEnrolledCoursesByStudentId(id);
      setCourses(coursesData);
    } catch (error) {
      console.error('Error fetching student data:', error);
      setAlert({
        open: true,
        message: 'Failed to load student data',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };

  const handleDropCourse = async (courseCode) => {
    try {
      setLoading(true);
      await enrollmentService.dropCourse(student.studentId, courseCode);
      
      // Refresh data
      fetchStudentData();
      
      setAlert({
        open: true,
        message: 'Course dropped successfully',
        severity: 'success',
      });
    } catch (error) {
      console.error('Error dropping course:', error);
      setAlert({
        open: true,
        message: 'Failed to drop course',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleAlertClose = () => {
    setAlert({ ...alert, open: false });
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!student) {
    return (
      <Box>
        <Typography variant="h5" color="error">
          Student not found
        </Typography>
        <Button
          variant="contained"
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/students')}
          sx={{ mt: 2 }}
        >
          Back to Students
        </Button>
      </Box>
    );
  }

  return (
    <Box>
      <PageHeader title="Student Details" />

      <Paper sx={{ p: 3, mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h5">
            {student.firstName} {student.lastName}
          </Typography>
          <Box>
            <Button
              variant="outlined"
              startIcon={<ArrowBackIcon />}
              onClick={() => navigate('/students')}
              sx={{ mr: 1 }}
            >
              Back
            </Button>
            <Button
              variant="contained"
              startIcon={<EditIcon />}
              component={RouterLink}
              to={`/students/edit/${id}`}
            >
              Edit
            </Button>
          </Box>
        </Box>

        <Divider sx={{ mb: 3 }} />

        <Grid container spacing={3}>
          <Grid item xs={12} md={6}>
            <Typography variant="subtitle1" gutterBottom>
              Student ID
            </Typography>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {student.studentId}
            </Typography>

            <Typography variant="subtitle1" gutterBottom>
              Email
            </Typography>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {student.email}
            </Typography>
          </Grid>

          <Grid item xs={12} md={6}>
            <Typography variant="subtitle1" gutterBottom>
              Full Name
            </Typography>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {student.firstName} {student.lastName}
            </Typography>

            <Typography variant="subtitle1" gutterBottom>
              Phone Number
            </Typography>
            <Typography variant="body1" sx={{ mb: 2 }}>
              {student.phoneNumber || 'Not provided'}
            </Typography>
          </Grid>
        </Grid>
      </Paper>

      <Paper sx={{ p: 3 }}>
        <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
          <Tabs value={tabValue} onChange={handleTabChange} aria-label="student tabs">
            <Tab label="Enrolled Courses" icon={<SchoolIcon />} iconPosition="start" />
            <Tab label="Enrollment History" icon={<CalendarMonthIcon />} iconPosition="start" />
          </Tabs>
        </Box>

        {/* Enrolled Courses Tab */}
        <Box role="tabpanel" hidden={tabValue !== 0} sx={{ mt: 2 }}>
          {courses.length > 0 ? (
            <Grid container spacing={2}>
              {courses.map((course) => (
                <Grid item xs={12} md={6} key={course.id}>
                  <Card>
                    <CardHeader
                      title={course.name}
                      subheader={course.courseCode}
                      action={
                        <Button
                          size="small"
                          color="error"
                          onClick={() => handleDropCourse(course.courseCode)}
                        >
                          Drop
                        </Button>
                      }
                    />
                    <CardContent>
                      <Typography variant="body2" color="text.secondary" paragraph>
                        {course.description}
                      </Typography>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1 }}>
                        <Chip
                          label={`${course.creditHours} Credits`}
                          size="small"
                          color="primary"
                        />
                        <Chip
                          label={`${course.currentEnrollment}/${course.maxCapacity} Students`}
                          size="small"
                          color={
                            course.currentEnrollment >= course.maxCapacity
                              ? 'error'
                              : 'success'
                          }
                        />
                      </Box>
                    </CardContent>
                  </Card>
                </Grid>
              ))}
            </Grid>
          ) : (
            <Typography variant="body1" sx={{ py: 2 }}>
              No courses enrolled
            </Typography>
          )}
        </Box>

        {/* Enrollment History Tab */}
        <Box role="tabpanel" hidden={tabValue !== 1} sx={{ mt: 2 }}>
          {enrollments.length > 0 ? (
            <List>
              {enrollments.map((enrollment) => (
                <ListItem key={enrollment.id} divider>
                  <ListItemText
                    primary={enrollment.courseCode}
                    secondary={`Enrolled: ${new Date(
                      enrollment.enrollmentDate
                    ).toLocaleDateString()} | Status: ${enrollment.status}`}
                  />
                  <Chip
                    label={enrollment.status}
                    color={
                      enrollment.status === 'ENROLLED'
                        ? 'success'
                        : enrollment.status === 'DROPPED'
                        ? 'error'
                        : 'default'
                    }
                    size="small"
                  />
                </ListItem>
              ))}
            </List>
          ) : (
            <Typography variant="body1" sx={{ py: 2 }}>
              No enrollment history
            </Typography>
          )}
        </Box>
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

export default StudentDetail;
