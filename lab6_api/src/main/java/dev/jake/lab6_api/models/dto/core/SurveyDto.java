package dev.jake.lab6_api.models.dto.core;

import dev.jake.lab6_api.models.state.SurveyState;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SurveyDto(
        Long id,
        @NotNull String title,
        List<SurveyItemDto> items,
        SurveyState state
) {
}
