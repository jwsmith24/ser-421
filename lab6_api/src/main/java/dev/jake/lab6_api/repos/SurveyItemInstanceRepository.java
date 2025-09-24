package dev.jake.lab6_api.repos;

import dev.jake.lab6_api.models.SurveyItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyItemInstanceRepository extends JpaRepository<SurveyItemInstance, Long> {
    // fetch all survey item instances for a target survey instance
    List<SurveyItemInstance> findBySurveyInstanceId(Long surveyInstanceId);
}
