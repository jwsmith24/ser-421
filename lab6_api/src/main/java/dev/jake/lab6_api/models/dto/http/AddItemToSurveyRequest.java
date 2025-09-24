package dev.jake.lab6_api.models.dto.http;

public record AddItemToSurveyRequest(
        Long surveyId,
        Long itemId
) {
}
