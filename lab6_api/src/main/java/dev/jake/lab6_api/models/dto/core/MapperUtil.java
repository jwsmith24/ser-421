package dev.jake.lab6_api.models.dto.core;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyInstance;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.SurveyItemInstance;
import dev.jake.lab6_api.models.state.SurveyState;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MapperUtil {

    //concrete mappers
    public static SurveyItemDto toDto(SurveyItem item) {
        return new SurveyItemDto(
                item.getId(),
                item.getQuestionStem(),
                item.getPossibleAnswers(),
                item.getCorrectAnswer()
        );
    }

    public static SurveyDto toDto(Survey survey) {
        return new SurveyDto(
                survey.getId(),
                survey.getTitle(),
                survey.getItems().stream().map(MapperUtil::toDto).toList(),
                survey.getState()
        );
    }

    public static SurveyItem fromDto(SurveyItemDto dto) {
        return new SurveyItem(
                dto.id(),
                dto.questionStem(),
                dto.possibleAnswers(),
                dto.correctAnswer()
        );
    }

    public static Survey fromDto(SurveyDto dto) {
        return new Survey(
                dto.id(),
                dto.title(),
                Optional.ofNullable(dto.items())
                        .orElseGet(Collections::emptyList)
                        .stream().map(
                        MapperUtil::fromDto
                ).toList(),
                SurveyState.CREATED
        );
    }


    // instance mappers

    public static SurveyInstanceDto toDto(SurveyInstance instance) {
        List<SurveyItemInstanceDto> items = instance.getItemInstances().stream()
                .map(MapperUtil::toDto).toList();

        return new SurveyInstanceDto(
                instance.getId(),
                instance.getUsername(),
                instance.getSurvey().getTitle(),
                items,
                instance.getState()
        );
    }

    public static SurveyItemInstanceDto toDto(SurveyItemInstance itemInstance) {
        return new SurveyItemInstanceDto(
                itemInstance.getId(),
                itemInstance.getSurveyItem().getId(),
                itemInstance.getSurveyItem().getQuestionStem(),
                itemInstance.getChosenAnswer(),
                itemInstance.getIsCorrect(),
                itemInstance.getState()
        );
    }
}
