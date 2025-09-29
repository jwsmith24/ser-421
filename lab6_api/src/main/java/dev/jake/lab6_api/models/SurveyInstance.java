package dev.jake.lab6_api.models;

import dev.jake.lab6_api.models.state.SurveyInstanceState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SurveyInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // can have multiple instances of a survey
    @ManyToOne
    private Survey survey;

    @OneToMany(mappedBy = "surveyInstance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyItemInstance> itemInstances = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SurveyInstanceState state = SurveyInstanceState.CREATED;

    public void addItemInstance(SurveyItemInstance instance) {
        instance.setSurveyInstance(this); // set back ref
        itemInstances.add(instance);
    }


}
