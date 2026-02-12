CREATE TABLE lesson
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255),
    description  TEXT,
    content      TEXT,
    lesson_order INT,
    chapter_id   BIGINT,
    createTime   TIMESTAMP,
    updateTime   TIMESTAMP,
    CONSTRAINT fk_chapter FOREIGN KEY (chapter_id) REFERENCES chapter (id)
);
