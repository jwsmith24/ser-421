package dev.jake.lab6_api.models.dto.core;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SurveyItemDto (
        Long id,
        @NotNull String questionStem,
        List<String> possibleAnswers,
        String correctAnswer
){
}
