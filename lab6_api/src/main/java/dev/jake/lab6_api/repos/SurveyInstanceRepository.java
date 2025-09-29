package dev.jake.lab6_api.repos;

import dev.jake.lab6_api.models.SurveyInstance;
import dev.jake.lab6_api.models.state.SurveyInstanceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyInstanceRepository extends JpaRepository<SurveyInstance, Long> {
    // get all instances by state
    List<SurveyInstance> findByState(SurveyInstanceState state);

    // get all instances for a username
    List<SurveyInstance> findByUsername(String username);
}
