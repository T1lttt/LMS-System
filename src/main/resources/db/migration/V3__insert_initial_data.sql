INSERT INTO course(name, description, "createdTime", "updateTime")
VALUES ('Java Developer', 'basic java course', now(), now()),
       ('Python Developer', 'basic python course', now(), now());

INSERT INTO chapter(name, description, chapter_order, course_id, created_time, update_time)
VALUES ('Java Introduction', 'basic syntax', 1, 1, now(), now()),
       ('First Project', 'Spring, SQL', 1, 2, now(), now());
