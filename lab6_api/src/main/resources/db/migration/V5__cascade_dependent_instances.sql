ALTER TABLE survey_instance
    DROP CONSTRAINT survey_instance_survey_id_fkey,
    ADD CONSTRAINT survey_instance_survey_id_fkey
        FOREIGN KEY (survey_id) REFERENCES survey (id) ON DELETE CASCADE;