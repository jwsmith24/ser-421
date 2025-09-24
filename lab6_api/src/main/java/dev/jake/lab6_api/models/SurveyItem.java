package dev.jake.lab6_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SurveyItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionStem;

    @ElementCollection
    @CollectionTable(
            name = "survey_item_possible_answers",
            joinColumns = @JoinColumn(name = "survey_item_id")
    )
    @Column(name = "answer")
    private List<String> possibleAnswers;

    private String correctAnswer;
}
