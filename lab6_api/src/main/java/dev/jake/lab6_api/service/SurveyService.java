package dev.jake.lab6_api.service;

import dev.jake.lab6_api.exceptions.DuplicateResourceException;
import dev.jake.lab6_api.exceptions.ResourceNotFoundException;
import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyInstance;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.SurveyItemInstance;
import dev.jake.lab6_api.models.dto.core.*;
import dev.jake.lab6_api.models.dto.http.AddItemToSurveyRequest;
import dev.jake.lab6_api.models.dto.http.CreateSurveyForUserRequest;
import dev.jake.lab6_api.models.dto.http.SubmitAnswerRequest;
import dev.jake.lab6_api.models.state.SurveyInstanceState;
import dev.jake.lab6_api.models.state.SurveyItemInstanceState;
import dev.jake.lab6_api.repos.SurveyInstanceRepository;
import dev.jake.lab6_api.repos.SurveyItemInstanceRepository;
import dev.jake.lab6_api.repos.SurveyItemRepository;
import dev.jake.lab6_api.repos.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.jake.lab6_api.models.dto.core.MapperUtil.toDto;

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
    public SurveyItem createSurveyItem(SurveyItemDto item) {

        return surveyItemRepository.save(MapperUtil.fromDto(item));
    }

    // 2
    public Survey createSurvey(SurveyDto survey) {
        return surveyRepository.save(MapperUtil.fromDto(survey));
    }

    // 3
    public SurveyItem addItemToSurvey(Long surveyId, AddItemToSurveyRequest request) {
        Long itemId = request.itemId();

        // Fetch survey and item (will throw 404 if not found)
        Survey survey = getSurveyById(surveyId);
        SurveyItem item = getItemById(itemId);

        // Check if the item is already on this survey
        if (survey.getItems().stream().anyMatch(i -> i.getId().equals(itemId))) {
            throw new DuplicateResourceException(
                    "Item with id " + itemId + " is already part of survey " + surveyId
            );
        }

        // Add and save
        survey.addItem(item);
        surveyRepository.save(survey);

        return item;
    }


    // 4
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    // 5 (many-to-many retrieves associated items)
    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Survey with " +
                "id: " + id + " not found"));
    }

    // 6: create a survey instance of survey for a user, with associated set of survey item
    // instances
    @Transactional
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

        SurveyInstance saved = surveyInstanceRepository.save(instance);

        // force load items before mapping
        int size = saved.getItemInstances().size();

        return MapperUtil.toDto(saved);


    }

    // 7: accept an answer for a survey item instance on a specific survey instance (patch)
    @Transactional
    public SurveyItemInstanceDto acceptAnswer(SubmitAnswerRequest request) {
        // Find survey instance by username
        SurveyInstance instance = surveyInstanceRepository.findByUsername(request.username())
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Survey instance not found for user " + request.username()
                ));

        // Load all item instances for this survey instance
        List<SurveyItemInstance> allItems = surveyItemInstanceRepository.findBySurveyInstanceId(instance.getId());

        // Find the specific item instance by SurveyItem.id
        SurveyItemInstance itemInstance = allItems.stream()
                .filter(item -> item.getSurveyItem().getId().equals(request.itemId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item " + request.itemId() + " not found in survey instance " + instance.getId()
                ));

        // Prevent double-answering
        if (itemInstance.getState() == SurveyItemInstanceState.COMPLETE) {
            throw new DuplicateResourceException(
                    "Item " + request.itemId() + " has already been answered"
            );
        }

        // Update chosen answer, correctness, and state
        itemInstance.setChosenAnswer(request.userAnswer());
        String correctAnswer = itemInstance.getSurveyItem().getCorrectAnswer();
        itemInstance.setIsCorrect(itemInstance.getChosenAnswer().equals(correctAnswer));
        itemInstance.setState(SurveyItemInstanceState.COMPLETE);

        // Update overall survey instance state
        boolean allComplete = allItems.stream()
                .allMatch(item -> item.getState() == SurveyItemInstanceState.COMPLETE);

        instance.setState(allComplete ? SurveyInstanceState.COMPLETED : SurveyInstanceState.IN_PROGRESS);

        // Persist and return
        surveyInstanceRepository.save(instance); // saves both because of cascade
        return MapperUtil.toDto(itemInstance);
    }


    // 8
    public List<SurveyInstanceDto> findSurveysByState(String state) {
        if (state == null) {
            return surveyInstanceRepository.findAll().stream().map(MapperUtil::toDto).toList();
        }

        // throws illegal state exception
        SurveyInstanceState targetState = SurveyInstanceState.valueOf(state);

        return surveyInstanceRepository.findByState(targetState).stream().map(MapperUtil::toDto).toList();
    }

    // 9
    public SurveyInstanceDto getSurveyInstance(Long id) {
        return toDto(surveyInstanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Survey instance not found with id" + id)));
    }

    // 10: delete specific survey
    public void deleteSurvey(Long id) {
        // check if survey exists
        Survey target = surveyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No " +
                "survey exists with ID: " + id));

        surveyRepository.delete(target);
    }


    // extra for testing
    public List<SurveyItem> getAllSurveyItems() {
        return surveyItemRepository.findAll();
    }

    public SurveyItem getItemById(Long id) {
        return surveyItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item " +
                "with id " + id + " does not exist"));
    }



}
