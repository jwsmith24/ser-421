package dev.jake.lab6_api.models;

import dev.jake.lab6_api.models.state.SurveyState;
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
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    // 0-5 items allowed
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "survey_items",
            joinColumns = @JoinColumn(name = "survey_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<SurveyItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private SurveyState state = SurveyState.CREATED;

    public void addItem(SurveyItem item) {
        this.items.add(item);
    }
}
