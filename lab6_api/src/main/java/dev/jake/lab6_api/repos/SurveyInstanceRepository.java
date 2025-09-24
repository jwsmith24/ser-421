package dev.jake.lab6_api.repos;

import dev.jake.lab6_api.models.SurveyInstance;
import dev.jake.lab6_api.models.state.SurveyInstanceState;

import java.util.List;

public interface SurveyInstanceRepository {
    // get all instances by state
    List<SurveyInstance> findByState(SurveyInstanceState state);

    // get all instances for a username
    List<SurveyInstance> findByUsername(String username);
}
