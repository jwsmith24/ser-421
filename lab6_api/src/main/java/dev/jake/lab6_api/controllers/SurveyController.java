package dev.jake.lab6_api.controllers;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.SurveyItem;
import dev.jake.lab6_api.models.dto.core.SurveyDto;
import dev.jake.lab6_api.models.dto.core.SurveyInstanceDto;
import dev.jake.lab6_api.models.dto.core.SurveyItemDto;
import dev.jake.lab6_api.models.dto.core.SurveyItemInstanceDto;
import dev.jake.lab6_api.models.dto.http.*;
import dev.jake.lab6_api.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Operation(summary = "Create a new survey")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Survey created",
                    content = @Content(schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping()
    public ResponseEntity<Survey> createSurvey(@Valid @RequestBody SurveyDto survey) {

        Survey newSurvey = surveyService.createSurvey(survey);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSurvey.getId())
                .toUri();
        return ResponseEntity.created(location).body(newSurvey);

    }

    @Operation(summary = "Get all surveys")
    @ApiResponse(responseCode = "200", description = "List of surveys")
    @GetMapping()
    public ResponseEntity<List<Survey>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }

    @Operation(summary = "Get a survey by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey found",
                    content = @Content(schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Survey> getSurveyById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }


    @Operation(summary = "Create a new survey item")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Survey item created",
                    content = @Content(schema = @Schema(implementation = SurveyItemDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Database constraint violation",
            content = @Content(schema = @Schema(implementation =
                    ApiErrorResponse.class)))
    })
    @PostMapping("/items")
    public ResponseEntity<SurveyItem> createSurveyItem(@Valid @RequestBody SurveyItemDto item) {
        SurveyItem newItem = surveyService.createSurveyItem(item);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newItem.getId())
                .toUri();

        return ResponseEntity.created(location).body(newItem);
    }

    @Operation(summary = "Get all survey items")
    @ApiResponse(responseCode = "200", description = "List of survey items")
    @GetMapping("/items")
    public ResponseEntity<List<SurveyItem>> getAllItems() {
        return ResponseEntity.ok(surveyService.getAllSurveyItems());
    }

    @Operation(summary = "Get a survey item by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey item found",
                    content = @Content(schema = @Schema(implementation = SurveyItemDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey item not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/items/{id}")
    public ResponseEntity<SurveyItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(surveyService.getItemById(id));
    }


    @Operation(summary = "Add an item to a survey")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added to survey",
                    content = @Content(schema = @Schema(implementation = SurveyItemDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey or item not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Item already exists in survey",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/{surveyId}/items")
    public ResponseEntity<SurveyItem> addItemToSurvey(
            @PathVariable Long surveyId,
            @Valid @RequestBody AddItemToSurveyRequest request) {

        SurveyItem newItem = surveyService.addItemToSurvey(surveyId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("id")
                .buildAndExpand(newItem.getId())
                .toUri();

        return ResponseEntity.created(location).body(newItem);
    }

    @Operation(summary = "Create a survey instance for a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Survey instance created",
                    content = @Content(schema = @Schema(implementation = SurveyInstanceDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping("/instances")
    public ResponseEntity<SurveyInstanceDto> createSurveyInstanceForUser(@Valid @RequestBody CreateSurveyForUserRequest request) {

        SurveyInstanceDto newService = surveyService.createSurveyInstanceForUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newService.id())
                .toUri();

        return ResponseEntity.created(location).body(newService);
    }


    @Operation(summary = "Get a survey instance by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey instance found",
                    content = @Content(schema = @Schema(implementation = SurveyInstanceDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey instance not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/instances/{id}")
    public ResponseEntity<SurveyInstanceDto> getSurveyInstance(@PathVariable Long id) {

        return ResponseEntity.ok(surveyService.getSurveyInstance(id));
    }


    @Operation(summary = "Submit an answer to a survey item instance")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Answer accepted",
                    content = @Content(schema = @Schema(implementation = SurveyItemInstanceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PatchMapping("/instances/answer")
    public ResponseEntity<SurveyItemInstanceDto> acceptAnswerFromUser(@Valid @RequestBody SubmitAnswerRequest request) {
        return ResponseEntity.ok(surveyService.acceptAnswer(request));
    }

    @Operation(summary = "Get survey instances by state")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey instances found",
                    content = @Content(schema = @Schema(implementation = SurveyInstanceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid state parameter",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/instances")
    public ResponseEntity<List<SurveyInstanceDto>> getAllSurveyInstancesByState(@Valid @RequestBody(required = false) FindSurveyByStateRequest request) {
        if (request == null) {
            return ResponseEntity.ok(surveyService.findSurveysByState(null));
        }

        return ResponseEntity.ok(surveyService.findSurveysByState(request.state()));
    }

    @Operation(summary = "Delete a survey by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Survey deleted"),
            @ApiResponse(responseCode = "404", description = "Survey not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }


}
