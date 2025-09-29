package dev.jake.lab6_api.controllers;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyInstance;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.dto.core.SurveyInstanceDto;
import dev.jake.lab6_api.models.dto.core.SurveyItemInstanceDto;
import dev.jake.lab6_api.models.dto.http.AddItemToSurveyRequest;
import dev.jake.lab6_api.models.dto.http.CreateSurveyForUserRequest;
import dev.jake.lab6_api.models.dto.http.FindSurveyByStateRequest;
import dev.jake.lab6_api.models.dto.http.SubmitAnswerRequest;
import dev.jake.lab6_api.service.SurveyService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok(surveyService.createSurvey(survey));

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
        return ResponseEntity.ok(surveyService.createSurveyItem(item));
    }

    @GetMapping("/items")
    public ResponseEntity<List<SurveyItem>> getAllItems() {
        return ResponseEntity.ok(surveyService.getAllSurveyItems());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<SurveyItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getItemById(id));
    }

    @PostMapping("/map")
    public ResponseEntity<Survey> addItemToSurvey(@RequestBody AddItemToSurveyRequest request) {

        return ResponseEntity.ok(surveyService.addItemToSurvey(request));
    }

    @PostMapping("/newInstance")
    public ResponseEntity<SurveyInstanceDto> createSurveyInstanceForUser(@RequestBody CreateSurveyForUserRequest request) {
        return ResponseEntity.ok(surveyService.createSurveyInstanceForUser(request));
    }

    @GetMapping("/instance/{id}")
    public ResponseEntity<SurveyInstanceDto> getSurveyInstance(@PathVariable Long id) {

        return ResponseEntity.ok(surveyService.getSurveyInstance(id));
    }

    @PatchMapping("/instance/answer")
    public ResponseEntity<SurveyItemInstanceDto> acceptAnswerFromUser(@RequestBody SubmitAnswerRequest request) {
        return ResponseEntity.ok(surveyService.acceptAnswer(request));
    }

    @GetMapping("/instance")
    public ResponseEntity<List<SurveyInstanceDto>> getAllSurveyInstancesByState(@RequestBody(required = false) FindSurveyByStateRequest request) {
        if (request == null) {
            return ResponseEntity.ok(surveyService.findSurveysByState(null));
        }

        return ResponseEntity.ok(surveyService.findSurveysByState(request.state()));
    }


}
