# Student Course Enrollment System

A full-stack application that allows students to register, enroll in courses, check schedules, and drop classes. The system consists of a Spring Boot backend and a React frontend.

## Features

- Student registration and management
- Course creation and management
- Course enrollment with validation (prerequisites, time clash, capacity)
- Schedule management
- RESTful API for all operations
- Modern React frontend with Material-UI

## Technologies Used

### Backend

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- MySQL Database
- Flyway for database migrations
- Maven

### Frontend

- React 18
- Material-UI
- React Router
- Axios
- React Data Grid

## Project Structure

### Backend

- **Model**: Student, Course, Enrollment, Schedule
- **Repository**: JPA repositories for data access
- **Service**: Business logic layer
- **Controller**: REST API endpoints
- **DTO**: Data Transfer Objects for API communication
- **Exception**: Custom exceptions and global exception handling

### Frontend

- **components**: Reusable UI components
  - **common**: Common UI elements (PageHeader, LoadingSpinner, etc.)
  - **layout**: Layout components (Navbar, Sidebar, Footer)
- **pages**: Page components
  - **students**: Student-related pages (List, Form, Detail)
  - **courses**: Course-related pages
  - **enrollments**: Enrollment-related pages
  - **schedules**: Schedule-related pages
- **services**: API service layer for backend communication

## API Endpoints

### Student API

- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `GET /api/students/studentId/{studentId}` - Get student by student ID
- `POST /api/students` - Create a new student
- `PUT /api/students/{id}` - Update a student
- `DELETE /api/students/{id}` - Delete a student

### Course API

- `GET /api/courses` - Get all courses
- `GET /api/courses/{id}` - Get course by ID
- `GET /api/courses/code/{courseCode}` - Get course by course code
- `GET /api/courses/student/{studentId}` - Get enrolled courses by student ID
- `GET /api/courses/available/student/{studentId}` - Get available courses by student ID
- `POST /api/courses` - Create a new course
- `PUT /api/courses/{id}` - Update a course
- `DELETE /api/courses/{id}` - Delete a course
- `POST /api/courses/{courseCode}/prerequisites/{prerequisiteCode}` - Add prerequisite to a course
- `DELETE /api/courses/{courseCode}/prerequisites/{prerequisiteCode}` - Remove prerequisite from a course

### Enrollment API

- `GET /api/enrollments` - Get all enrollments
- `GET /api/enrollments/{id}` - Get enrollment by ID
- `GET /api/enrollments/student/{studentId}` - Get enrollments by student ID
- `GET /api/enrollments/course/{courseId}` - Get enrollments by course ID
- `POST /api/enrollments/enroll` - Enroll a student in a course
- `POST /api/enrollments/drop` - Drop a course
- `GET /api/enrollments/check-prerequisites` - Check if student meets prerequisites for a course
- `GET /api/enrollments/check-time-conflict` - Check for time conflicts
- `GET /api/enrollments/check-capacity` - Check if course has available capacity

### Schedule API

- `GET /api/schedules` - Get all schedules
- `GET /api/schedules/{id}` - Get schedule by ID
- `GET /api/schedules/course/{courseId}` - Get schedules by course ID
- `GET /api/schedules/semester/{semester}` - Get schedules by semester
- `GET /api/schedules/student/{studentId}/semester/{semester}` - Get student schedule for a semester
- `POST /api/schedules` - Create a new schedule
- `PUT /api/schedules/{id}` - Update a schedule
- `DELETE /api/schedules/{id}` - Delete a schedule

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Node.js 14 or higher
- npm or yarn

### Database Setup

1. Install MySQL if not already installed
2. Create a MySQL user or use the root user (for development only)
3. Update the database configuration in `application.properties` with your MySQL credentials

### Running the Backend

1. Clone the repository
2. Navigate to the project directory
3. Run `mvn spring-boot:run`
4. The backend API will be available at `http://localhost:8081/api`

### Running with Different Profiles

- Development mode: `mvn spring-boot:run -Dspring-boot.run.profiles=dev`
- With sample data loader: `mvn spring-boot:run -Dspring-boot.run.profiles=dev-data-loader`

### Running the Frontend

1. Navigate to the frontend directory: `cd frontend`
2. Install dependencies: `npm install` or `yarn install`
3. Start the development server: `npm start` or `yarn start`
4. Access the application in your browser at `http://localhost:3000`

### Sample Data

The application is pre-loaded with sample data through Flyway migrations:

- 2 students
- 3 courses with prerequisites
- 5 schedules
- 2 enrollments

## Frontend Features

The React frontend provides a modern, responsive user interface for the Student Course Enrollment System:

- **Dashboard**: Overview with statistics and quick actions
- **Student Management**: View, add, edit, and delete students
- **Course Management**: View, add, edit, and delete courses
- **Enrollment Management**: Enroll students in courses with validation
- **Schedule Viewing**: View student schedules and course timetables

### UI/UX Features

- **Responsive Design**: Works on desktop and mobile devices
- **Material Design**: Clean, intuitive interface using Material-UI components
- **Form Validation**: Client-side validation for data integrity
- **Search & Filter**: Easy filtering of data in list views
- **Interactive Elements**: Confirmation dialogs, alerts, and loading indicators
- **Multi-step Forms**: Guided enrollment process with validation at each step

## Screenshots

(Add screenshots of the application here)

## License

This project is licensed under the MIT License.
