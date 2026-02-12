package com.lms.lmssystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Лекция")
public class LessonDto {

    @Schema(description = "ID лекции", example = "1")
    private Long id;

    @Schema(description = "Название лекции", example = "Java Basics")
    private String name;

    @Schema(description = "Описание лекции", example = "Введение в синтаксис Java")
    private String description;

    @Schema(description = "Контент лекции", example = "Содержимое лекции")
    private String content;

    @Schema(description = "Порядковый номер лекции", example = "1")
    private Integer lessonOrder;

    @Schema(description = "ID главы, к которой относится лекция", example = "3")
    private Long chapterId;

    @Schema(description = "Дата создания лекции")
    private LocalDateTime createTime;

    @Schema(description = "Дата последнего обновления лекции")
    private LocalDateTime updateTime;
}