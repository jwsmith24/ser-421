package dev.jake.lab6_api.controllers;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.dto.http.AddItemToSurveyRequest;
import dev.jake.lab6_api.service.SurveyService;
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
        return ResponseEntity.ok(surveyService.getAll());
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

}
