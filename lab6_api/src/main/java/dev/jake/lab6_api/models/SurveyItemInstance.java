package dev.jake.lab6_api.models;

import dev.jake.lab6_api.models.state.SurveyItemInstanceState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SurveyItemInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SurveyItem surveyItem;

    @ManyToOne
    private SurveyInstance surveyInstance;

    private String chosenAnswer;

    private Boolean isCorrect;

    @Enumerated(EnumType.STRING)
    private SurveyItemInstanceState state = SurveyItemInstanceState.NOT_COMPLETED;
}
