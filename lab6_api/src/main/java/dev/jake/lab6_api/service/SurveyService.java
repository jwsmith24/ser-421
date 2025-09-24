package dev.jake.lab6_api.service;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.dto.http.AddItemToSurveyRequest;
import dev.jake.lab6_api.repos.SurveyItemRepository;
import dev.jake.lab6_api.repos.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyItemRepository surveyItemRepository;

    public SurveyService(SurveyRepository surveyRepository, SurveyItemRepository surveyItemRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyItemRepository = surveyItemRepository;
    }

    public Survey createSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    public List<Survey> getAll() {
        return surveyRepository.findAll();
    }

    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey with " +
                "id: " + id + " not found"));
    }



    public SurveyItem createSurveyItem(SurveyItem item) {
        return surveyItemRepository.save(item);
    }

    public List<SurveyItem> getAllSurveyItems() {
        return surveyItemRepository.findAll();
    }

    public SurveyItem getItemById(Long id) {
        return surveyItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item " +
                "with id " + id + " does not exist"));
    }

    public Survey addItemToSurvey(AddItemToSurveyRequest request) {
        Long surveyId =  request.surveyId();
        Long itemId = request.itemId();

        Survey survey = getSurveyById(surveyId);
        SurveyItem item = getItemById(itemId);

        // make sure item isn't already on the survey
        // handle that error

        survey.addItem(item);

        return surveyRepository.save(survey);
    }


}
