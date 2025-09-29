package dev.jake.lab6_api.models.dto.http;

public record CreateSurveyForUserRequest (
        Long surveyId,
        String username
){
}
