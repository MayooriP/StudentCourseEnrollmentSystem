-- Insert students
INSERT INTO students (student_id, first_name, last_name, email, phone_number)
VALUES 
    ('S001', 'John', 'Doe', 'john.doe@example.com', '1234567890'),
    ('S002', 'Jane', 'Smith', 'jane.smith@example.com', '9876543210');

-- Insert courses
INSERT INTO courses (course_code, name, description, credit_hours, max_capacity)
VALUES 
    ('CS101', 'Introduction to Programming', 'Basic programming concepts using Java', 3, 30),
    ('CS102', 'Data Structures', 'Fundamental data structures and algorithms', 4, 25),
    ('CS201', 'Database Systems', 'Introduction to database design and SQL', 3, 20);

-- Insert course prerequisites
-- First, get the IDs
SET @cs101_id = (SELECT id FROM courses WHERE course_code = 'CS101');
SET @cs102_id = (SELECT id FROM courses WHERE course_code = 'CS102');
SET @cs201_id = (SELECT id FROM courses WHERE course_code = 'CS201');

-- Then insert prerequisites
INSERT INTO course_prerequisites (course_id, prerequisite_id)
VALUES 
    (@cs102_id, @cs101_id),
    (@cs201_id, @cs101_id);

-- Insert schedules
INSERT INTO schedules (course_id, day_of_week, start_time, end_time, room, semester)
VALUES 
    (@cs101_id, 'MONDAY', '09:00:00', '10:30:00', 'Room 101', 'Fall 2023'),
    (@cs101_id, 'WEDNESDAY', '09:00:00', '10:30:00', 'Room 101', 'Fall 2023'),
    (@cs102_id, 'TUESDAY', '13:00:00', '14:30:00', 'Room 102', 'Fall 2023'),
    (@cs102_id, 'THURSDAY', '13:00:00', '14:30:00', 'Room 102', 'Fall 2023'),
    (@cs201_id, 'FRIDAY', '10:00:00', '11:30:00', 'Room 103', 'Fall 2023');

-- Insert enrollments
INSERT INTO enrollments (student_id, course_id, enrollment_date, status, notes)
SELECT 
    s.id as student_id,
    c.id as course_id,
    NOW() as enrollment_date,
    'ENROLLED' as status,
    NULL as notes
FROM 
    students s
CROSS JOIN 
    courses c
WHERE 
    (s.student_id = 'S001' AND c.course_code = 'CS101') OR
    (s.student_id = 'S002' AND c.course_code = 'CS101');
