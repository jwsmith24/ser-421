ALTER TABLE survey_item DROP COLUMN possible_answers;

CREATE TABLE survey_item_possible_answers (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    survey_item_id BIGINT NOT NULL REFERENCES survey_item(id) ON DELETE CASCADE,
    answer TEXT NOT NULL
)