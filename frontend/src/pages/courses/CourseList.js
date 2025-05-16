import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
  Box,
  Paper,
  Grid,
  Card,
  CardContent,
  CardActions,
  Typography,
  Button,
  Chip,
  TextField,
  InputAdornment,
  IconButton,
  Tooltip,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import VisibilityIcon from '@mui/icons-material/Visibility';
import SearchIcon from '@mui/icons-material/Search';
import PageHeader from '../../components/common/PageHeader';
import LoadingSpinner from '../../components/common/LoadingSpinner';
import AlertMessage from '../../components/common/AlertMessage';
import ConfirmDialog from '../../components/common/ConfirmDialog';
import courseService from '../../services/courseService';

const CourseList = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredCourses, setFilteredCourses] = useState([]);
  const [alert, setAlert] = useState({ open: false, message: '', severity: 'success' });
  const [confirmDialog, setConfirmDialog] = useState({
    open: false,
    title: '',
    message: '',
    id: null,
  });

  useEffect(() => {
    fetchCourses();
  }, []);

  useEffect(() => {
    if (searchTerm) {
      const filtered = courses.filter(
        (course) =>
          course.courseCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
          course.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          course.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredCourses(filtered);
    } else {
      setFilteredCourses(courses);
    }
  }, [searchTerm, courses]);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      const data = await courseService.getAllCourses();
      setCourses(data);
      setFilteredCourses(data);
    } catch (error) {
      console.error('Error fetching courses:', error);
      setAlert({
        open: true,
        message: 'Failed to load courses',
        severity: 'error',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteClick = (id) => {
    const course = courses.find((c) => c.id === id);
    setConfirmDialog({
      open: true,
      title: 'Delete Course',
      message: `Are you sure you want to delete ${course.name} (${course.courseCode})?`,
      id,
    });
  };

  const handleDeleteConfirm = async () => {
    try {
      await courseService.deleteCourse(confirmDialog.id);
      setCourses(courses.filter((course) => course.id !== confirmDialog.id));
      setAlert({
        open: true,
        message: 'Course deleted successfully',
        severity: 'success',
      });
    } catch (error) {
      console.error('Error deleting course:', error);
      setAlert({
        open: true,
        message: 'Failed to delete course',
        severity: 'error',
      });
    } finally {
      setConfirmDialog({ ...confirmDialog, open: false });
    }
  };

  const handleAlertClose = () => {
    setAlert({ ...alert, open: false });
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <Box>
      <PageHeader
        title="Courses"
        buttonText="Add Course"
        buttonLink="/courses/new"
        buttonIcon={<AddIcon />}
      />

      <Paper sx={{ p: 2, mb: 3 }}>
        <TextField
          fullWidth
          variant="outlined"
          placeholder="Search courses..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
          sx={{ mb: 3 }}
        />

        <Grid container spacing={3}>
          {filteredCourses.length > 0 ? (
            filteredCourses.map((course) => (
              <Grid item xs={12} sm={6} md={4} key={course.id}>
                <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                  <CardContent sx={{ flexGrow: 1 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                      <Typography variant="h6" component="div">
                        {course.name}
                      </Typography>
                      <Chip
                        label={course.courseCode}
                        color="primary"
                        size="small"
                        sx={{ fontWeight: 'bold' }}
                      />
                    </Box>
                    <Typography
                      variant="body2"
                      color="text.secondary"
                      sx={{
                        mb: 2,
                        overflow: 'hidden',
                        textOverflow: 'ellipsis',
                        display: '-webkit-box',
                        WebkitLineClamp: 3,
                        WebkitBoxOrient: 'vertical',
                      }}
                    >
                      {course.description}
                    </Typography>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 1 }}>
                      <Chip
                        label={`${course.creditHours} Credits`}
                        size="small"
                        variant="outlined"
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
                  <CardActions>
                    <Box sx={{ display: 'flex', width: '100%', justifyContent: 'space-between' }}>
                      <Button
                        size="small"
                        component={RouterLink}
                        to={`/courses/${course.id}`}
                        startIcon={<VisibilityIcon />}
                      >
                        View
                      </Button>
                      <Box>
                        <Tooltip title="Edit">
                          <IconButton
                            component={RouterLink}
                            to={`/courses/edit/${course.id}`}
                            color="primary"
                            size="small"
                          >
                            <EditIcon />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title="Delete">
                          <IconButton
                            onClick={() => handleDeleteClick(course.id)}
                            color="error"
                            size="small"
                          >
                            <DeleteIcon />
                          </IconButton>
                        </Tooltip>
                      </Box>
                    </Box>
                  </CardActions>
                </Card>
              </Grid>
            ))
          ) : (
            <Grid item xs={12}>
              <Typography variant="body1" align="center" sx={{ py: 4 }}>
                No courses found
              </Typography>
            </Grid>
          )}
        </Grid>
      </Paper>

      <AlertMessage
        open={alert.open}
        message={alert.message}
        severity={alert.severity}
        onClose={handleAlertClose}
      />

      <ConfirmDialog
        open={confirmDialog.open}
        title={confirmDialog.title}
        message={confirmDialog.message}
        onConfirm={handleDeleteConfirm}
        onCancel={() => setConfirmDialog({ ...confirmDialog, open: false })}
      />
    </Box>
  );
};

export default CourseList;
