package dev.jake.lab6_api.repos;

import dev.jake.lab6_api.models.Survey;
import dev.jake.lab6_api.models.state.SurveyState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    // fetch surveys by state (created, completed, deleted)
    List<Survey> findByState(SurveyState state);

}
