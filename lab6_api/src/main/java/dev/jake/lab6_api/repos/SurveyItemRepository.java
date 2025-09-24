package dev.jake.lab6_api.repos;

import dev.jake.lab6_api.models.SurveyItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyItemRepository extends JpaRepository<SurveyItem, Long> {
}
