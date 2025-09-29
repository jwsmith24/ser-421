package dev.jake.lab6_api.models.dto.core;

import dev.jake.lab6_api.models.state.SurveyInstanceState;

import java.util.List;

public record SurveyInstanceDto(
        Long id,
        String username,
        String surveyTitle,
        List<SurveyItemInstanceDto> items,
        SurveyInstanceState state
) {
}
