# Student Course Enrollment System Frontend

This is the frontend application for the Student Course Enrollment System, built with React and Material-UI.

## Features

- **Student Management**: View, add, edit, and delete students
- **Course Management**: View, add, edit, and delete courses
- **Enrollment Management**: Enroll students in courses, view enrollments, drop courses
- **Schedule Viewing**: View student schedules and course timetables
- **Responsive Design**: Works on desktop and mobile devices

## Technologies Used

- **React**: Frontend library for building user interfaces
- **React Router**: For navigation and routing
- **Material-UI**: Component library for consistent design
- **Axios**: HTTP client for API requests

## Project Structure

```
frontend/
├── public/                 # Public assets
├── src/
│   ├── components/         # Reusable UI components
│   │   ├── common/         # Common UI elements
│   │   └── layout/         # Layout components
│   ├── pages/              # Page components
│   │   ├── courses/        # Course-related pages
│   │   ├── enrollments/    # Enrollment-related pages
│   │   ├── schedules/      # Schedule-related pages
│   │   └── students/       # Student-related pages
│   ├── services/           # API service layer
│   ├── App.js              # Main application component
│   └── index.js            # Application entry point
└── package.json            # Project dependencies
```

## Getting Started

### Prerequisites

- Node.js (v14 or higher)
- npm or yarn

### Installation

1. Navigate to the frontend directory:
   ```
   cd frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```
   or
   ```
   yarn install
   ```

3. Start the development server:
   ```
   npm start
   ```
   or
   ```
   yarn start
   ```

4. Open your browser and navigate to `http://localhost:3000`

## API Integration

The frontend communicates with the backend API running on `http://localhost:8081/api`. The proxy is configured in `package.json` to forward API requests.

## Features to Implement

- [ ] Course Form page
- [ ] Course Detail page
- [ ] Enrollment List page
- [ ] Schedule View page
- [ ] Authentication and user roles
- [ ] Dark mode theme
- [ ] Export data to CSV/PDF
- [ ] Advanced filtering and sorting

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
