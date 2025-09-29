package dev.jake.lab6_api.models.dto.http;

public record SubmitAnswerRequest(
        String userAnswer,
        String username,
        Long itemId
) {
}
