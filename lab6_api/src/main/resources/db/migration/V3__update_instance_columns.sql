ALTER TABLE survey_instance
    DROP COLUMN survey_instance_id,
    DROP COLUMN chosen_answer,
    DROP COLUMN is_correct;



CREATE TABLE survey_item_instance (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    survey_instance_id BIGINT NOT NULL REFERENCES survey_instance(id) ON DELETE CASCADE ,
    survey_item_id BIGINT NOT NULL REFERENCES survey_item(id),
    chosen_answer TEXT,
    is_correct BOOLEAN
);

ALTER TABLE survey_item_instance
    ADD CONSTRAINT uq_survey_item_instance UNIQUE (survey_instance_id, survey_item_id);

