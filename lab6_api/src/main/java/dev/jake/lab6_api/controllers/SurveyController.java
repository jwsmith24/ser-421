package dev.jake.lab6_api.controllers;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.dto.core.SurveyInstanceDto;
import dev.jake.lab6_api.models.dto.core.SurveyItemInstanceDto;
import dev.jake.lab6_api.models.dto.http.AddItemToSurveyRequest;
import dev.jake.lab6_api.models.dto.http.CreateSurveyForUserRequest;
import dev.jake.lab6_api.models.dto.http.FindSurveyByStateRequest;
import dev.jake.lab6_api.models.dto.http.SubmitAnswerRequest;
import dev.jake.lab6_api.service.SurveyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping()
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {

        Survey newSurvey = surveyService.createSurvey(survey);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSurvey.getId())
                .toUri();
        return ResponseEntity.created(location).body(newSurvey);

    }

    @GetMapping()
    public ResponseEntity<List<Survey>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }


    @PostMapping("/items")
    public ResponseEntity<SurveyItem> createSurveyItem(@RequestBody SurveyItem item) {
        SurveyItem newItem = surveyService.createSurveyItem(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newItem.getId())
                .toUri();

        return ResponseEntity.created(location).body(newItem);
    }

    @GetMapping("/items")
    public ResponseEntity<List<SurveyItem>> getAllItems() {
        return ResponseEntity.ok(surveyService.getAllSurveyItems());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<SurveyItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getItemById(id));
    }

    @PostMapping("/{surveyId}/items")
    public ResponseEntity<SurveyItem> addItemToSurvey(
            @PathVariable Long surveyId,
            @RequestBody AddItemToSurveyRequest request) {

        SurveyItem newItem = surveyService.addItemToSurvey(surveyId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("id")
                .buildAndExpand(newItem.getId())
                .toUri();

        return ResponseEntity.created(location).body(newItem);
    }

    @PostMapping("/instances")
    public ResponseEntity<SurveyInstanceDto> createSurveyInstanceForUser(@RequestBody CreateSurveyForUserRequest request) {

        SurveyInstanceDto newService = surveyService.createSurveyInstanceForUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newService.id())
                .toUri();

        return ResponseEntity.created(location).body(newService);
    }

    @GetMapping("/instances/{id}")
    public ResponseEntity<SurveyInstanceDto> getSurveyInstance(@PathVariable Long id) {

        return ResponseEntity.ok(surveyService.getSurveyInstance(id));
    }

    @PatchMapping("/instances/answer")
    public ResponseEntity<SurveyItemInstanceDto> acceptAnswerFromUser(@RequestBody SubmitAnswerRequest request) {
        return ResponseEntity.ok(surveyService.acceptAnswer(request));
    }

    @GetMapping("/instances")
    public ResponseEntity<List<SurveyInstanceDto>> getAllSurveyInstancesByState(@RequestBody(required = false) FindSurveyByStateRequest request) {
        if (request == null) {
            return ResponseEntity.ok(surveyService.findSurveysByState(null));
        }

        return ResponseEntity.ok(surveyService.findSurveysByState(request.state()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }


}
