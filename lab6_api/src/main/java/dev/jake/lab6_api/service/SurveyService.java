package dev.jake.lab6_api.service;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyInstance;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.SurveyItemInstance;
import dev.jake.lab6_api.models.dto.core.SurveyInstanceDto;
import dev.jake.lab6_api.models.dto.core.SurveyItemInstanceDto;
import dev.jake.lab6_api.models.dto.http.AddItemToSurveyRequest;
import dev.jake.lab6_api.models.dto.http.CreateSurveyForUserRequest;
import dev.jake.lab6_api.models.dto.http.FindSurveyByStateRequest;
import dev.jake.lab6_api.models.dto.http.SubmitAnswerRequest;
import dev.jake.lab6_api.models.state.SurveyInstanceState;
import dev.jake.lab6_api.models.state.SurveyItemInstanceState;
import dev.jake.lab6_api.repos.SurveyInstanceRepository;
import dev.jake.lab6_api.repos.SurveyItemInstanceRepository;
import dev.jake.lab6_api.repos.SurveyItemRepository;
import dev.jake.lab6_api.repos.SurveyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyItemRepository surveyItemRepository;
    private final SurveyInstanceRepository surveyInstanceRepository;
    private final SurveyItemInstanceRepository surveyItemInstanceRepository;

    public SurveyService(SurveyRepository surveyRepository,
                         SurveyItemRepository surveyItemRepository, SurveyInstanceRepository surveyInstanceRepository, SurveyItemInstanceRepository surveyItemInstanceRepository) {
        this.surveyRepository = surveyRepository;
        this.surveyItemRepository = surveyItemRepository;
        this.surveyInstanceRepository = surveyInstanceRepository;
        this.surveyItemInstanceRepository = surveyItemInstanceRepository;
    }


    // 1
    public SurveyItem createSurveyItem(SurveyItem item) {
        return surveyItemRepository.save(item);
    }

    // 2
    public Survey createSurvey(Survey survey) {
        return surveyRepository.save(survey);
    }

    // 3
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


    // 4
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    // 5 (many-to-many retrieves associated items)
    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("Survey with " +
                "id: " + id + " not found"));
    }

    // 6: create a survey instance of survey for a user, with associated set of survey item
    // instances
    public SurveyInstanceDto createSurveyInstanceForUser(CreateSurveyForUserRequest request) {

        // make sure concrete survey exists (it'll throw an error if not found)
        Survey survey = getSurveyById(request.surveyId());

        // populate the instance
        SurveyInstance instance = new SurveyInstance();
        instance.setUsername(request.username());
        instance.setSurvey(survey);

        survey.getItems().forEach((surveyItem -> {
            SurveyItemInstance itemInstance = new SurveyItemInstance();
            itemInstance.setSurveyItem(surveyItem);
            itemInstance.setState(SurveyItemInstanceState.NOT_COMPLETED);
            instance.addItemInstance(itemInstance);

        }));


        return toDto(surveyInstanceRepository.save(instance));

    }

    // 7: accept an answer for a survey item instance on a specific survey instance (patch)
    public SurveyItemInstanceDto acceptAnswer(SubmitAnswerRequest request) {
        // try to find survey instance for user (throws no such element exception)
        SurveyInstance instance = surveyInstanceRepository.findByUsername(request.username()).getFirst();

        // try to find item instance
        SurveyItemInstance itemInstance =
                surveyItemInstanceRepository
                        .findBySurveyInstanceId(instance.getId())
                        .stream()
                        .filter((item -> item.getId().equals(request.itemId())))
                        .toList().getFirst();


        // update chosen answer, check correct and update, update state
        itemInstance.setChosenAnswer(request.userAnswer());

        String correctAnswer = itemInstance.getSurveyItem().getCorrectAnswer();
        itemInstance.setIsCorrect(itemInstance.getChosenAnswer().equals(correctAnswer));

        itemInstance.setState(SurveyItemInstanceState.COMPLETE);

        // check if entire survey instance is complete
        List<SurveyItemInstance> unanswered = surveyItemInstanceRepository
                .findBySurveyInstanceId(instance.getId())
                .stream().filter((item) -> item.getState().equals(SurveyItemInstanceState.NOT_COMPLETED))
                .toList();

        if (unanswered.isEmpty()) {
            instance.setState(SurveyInstanceState.COMPLETED);
        } else {
            instance.setState(SurveyInstanceState.IN_PROGRESS);
        }

        return toDto(surveyItemInstanceRepository.save(itemInstance));

    }

    // 8
    public List<SurveyInstanceDto> findSurveysByState(String state) {
        if (state == null) {
            return surveyInstanceRepository.findAll().stream().map(this::toDto).toList();
        }

        // throws illegal state exception
        SurveyInstanceState targetState = SurveyInstanceState.valueOf(state);

        return surveyInstanceRepository.findByState(targetState).stream().map(this::toDto).toList();
    }

    // 9
    public SurveyInstanceDto getSurveyInstance(Long id) {
        return toDto(surveyInstanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Survey instance not found with id" + id)));
    }

    // 10: delete specific survey
    public void deleteSurvey(Long id) {
        // check if survey exists
        Survey target = surveyRepository.findById(id).orElseThrow(() -> new RuntimeException("No " +
                "survey exists with ID: " + id));

        surveyRepository.delete(target);
    }


    // extra for testing
    public List<SurveyItem> getAllSurveyItems() {
        return surveyItemRepository.findAll();
    }

    public SurveyItem getItemById(Long id) {
        return surveyItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item " +
                "with id " + id + " does not exist"));
    }





    // mapping helpers
    private SurveyInstanceDto toDto(SurveyInstance instance) {
        List<SurveyItemInstanceDto> items = instance.getItemInstances().stream()
                .map(item -> new SurveyItemInstanceDto(
                        item.getId(),
                        item.getSurveyItem().getId(),
                        item.getSurveyItem().getQuestionStem(),
                        item.getChosenAnswer(),
                        item.getIsCorrect(),
                        item.getState()
                ))
                .toList();

        return new SurveyInstanceDto(
                instance.getId(),
                instance.getUsername(),
                instance.getSurvey().getTitle(),
                items,
                instance.getState()
        );
    }

    private SurveyItemInstanceDto toDto(SurveyItemInstance itemInstance) {
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
