-- Seed data for students
INSERT INTO students (first_name, last_name, student_number)
VALUES ('John', 'Doe', 'S001'),
       ('Jane', 'Smith', 'S002'),
       ('Alice', 'Johnson', 'S003'),
       ('Bob', 'Williams', 'S004'),
       ('Charlie', 'Brown', 'S005');

-- Seed data for tests
INSERT INTO tests (name, duration, start_time, end_time)
VALUES ('Math 101 Final', 120, '2024-06-01 09:00:00', '2024-06-01 11:00:00'),
       ('English Literature Midterm', 90, '2024-04-15 14:00:00', '2024-04-15 15:30:00'),
       ('Computer Science Basics', 60, '2024-05-10 10:00:00', '2024-05-10 11:00:00');

-- Seed data for questions
INSERT INTO questions (test_id, content, points)
VALUES (1, 'What is 2 + 2?', 5),
       (1, 'Solve for x: 2x + 5 = 13', 10),
       (1, 'What is the area of a circle with radius 3?', 15),
       (2, 'Who wrote "Romeo and Juliet"?', 5),
       (2, 'What is the main theme of "To Kill a Mockingbird"?', 10),
       (3, 'What does CPU stand for?', 5),
       (3, 'What is the purpose of a loop in programming?', 10);

-- Seed data for answers
INSERT INTO choices (question_id, content, is_correct)
VALUES (1, '3', false),
       (1, '4', true),
       (1, '5', false),
       (1, '6', false),
       (2, 'x = 4', true),
       (2, 'x = 5', false),
       (2, 'x = 6', false),
       (2, 'x = 7', false),
       (3, '9π', true),
       (3, '6π', false),
       (3, '3π', false),
       (3, '12π', false),
       (4, 'William Shakespeare', true),
       (4, 'Charles Dickens', false),
       (4, 'Jane Austen', false),
       (4, 'Mark Twain', false),
       (5, 'Racism and injustice', true),
       (5, 'Love and romance', false),
       (5, 'War and peace', false),
       (5, 'Nature and environment', false),
       (6, 'Central Processing Unit', true),
       (6, 'Computer Personal Unit', false),
       (6, 'Central Program Unit', false),
       (6, 'Control Processing Unit', false),
       (7, 'To repeat a block of code', true),
       (7, 'To define a function', false),
       (7, 'To declare variables', false),
       (7, 'To import libraries', false);

-- Seed data for student_tests
INSERT INTO test_participations (student_id, test_id, start_time, end_time, score, status)
VALUES (1, 1, '2024-06-01 09:00:00', '2024-06-01 10:45:00', 30, 'COMPLETED'),
       (2, 1, '2024-06-01 09:05:00', '2024-06-01 10:55:00', 15, 'COMPLETED'),
       (3, 2, '2024-04-15 14:00:00', '2024-04-15 15:20:00', 15, 'COMPLETED'),
       (4, 3, '2024-05-10 10:00:00', '2024-05-10 10:50:00', 15, 'COMPLETED'),
       (5, 3, '2024-05-10 10:02:00', NULL, NULL, 'IN_PROGRESS');

-- Seed data for student_answers
INSERT INTO student_answers (test_participation_id, question_id, selected_choice_id, is_correct, points_earned)
VALUES (1, 1, 2, true, 5),
       (1, 2, 5, true, 10),
       (1, 3, 9, true, 15),
       (2, 1, 2, true, 5),
       (2, 2, 5, true, 10),
       (2, 3, 10, false, 0),
       (3, 4, 13, true, 5),
       (3, 5, 17, true, 10),
       (4, 6, 21, true, 5),
       (4, 7, 25, true, 10);