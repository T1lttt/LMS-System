CREATE TABLE course
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255),
    description   TEXT,
    "createdTime" TIMESTAMP,
    "updateTime"  TIMESTAMP
);

CREATE TABLE chapter
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255),
    description   TEXT,
    chapter_order INT,
    course_id     BIGINT,
    created_time  TIMESTAMP,
    update_time   TIMESTAMP,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course (id)
);
