package com.lms.lmssystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Schema(description = "Глава курса")
public class ChapterDto {

    @Schema(description = "ID главы", example = "10")
    private Long id;

    @Schema(description = "Название главы", example = "Введение в Java")
    private String name;

    @Schema(description = "Описание главы", example = "Основы синтаксиса Java")
    private String description;

    @Schema(description = "Порядковый номер главы", example = "1")
    private int chapterOrder;

    @Schema(description = "ID курса", example = "1")
    private Long courseId;

    @Schema(description = "Дата создания главы")
    private LocalDateTime createdTime;

    @Schema(description = "Дата последнего обновления главы")
    private LocalDateTime updateTime;
}