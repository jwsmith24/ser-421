package dev.jake.lab6_api.models.dto.core;

import dev.jake.lab6_api.models.state.SurveyItemInstanceState;

public record SurveyItemInstanceDto(
        Long id,
        Long surveyItemId,
        String questionStem,
        String chosenAnswer,
        Boolean isCorrect,
        SurveyItemInstanceState state
) {
}
