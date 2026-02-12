package com.lms.lmssystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Schema(description = "Курс")
public class CourseDto {

    @Schema(description = "ID курса", example = "1")
    private Long id;

    @Schema(description = "Название курса", example = "Java Developer")
    private String name;

    @Schema(description = "Описание курса", example = "Базовый курс по Java")
    private String description;

    @Schema(description = "Дата создания курса")
    private LocalDateTime createTime;

    @Schema(description = "Дата последнего обновления курса")
    private LocalDateTime updateTime;
}
