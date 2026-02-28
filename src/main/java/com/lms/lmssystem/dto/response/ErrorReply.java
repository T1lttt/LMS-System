package com.lms.lmssystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Schema(description = "Ответ с ошибкой")
public class ErrorReply {

    @Schema(example = "Курс не найден")
    private String message;

    @Schema(example = "2026-01-21T08:30:00")
    private LocalDateTime timestamp;

    public ErrorReply(String message){
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }


}
