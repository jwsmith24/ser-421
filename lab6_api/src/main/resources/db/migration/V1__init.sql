CREATE TABLE survey_item (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    question_stem TEXT NOT NULL,
    possible_answers TEXT[] NOT NULL,
    correct_answer TEXT NOT NULL
);

CREATE TABLE survey (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title TEXT NOT NULL,
    state TEXT NOT NULL
);


CREATE TABLE survey_items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    survey_id BIGINT NOT NULL REFERENCES survey(id),
    item_id BIGINT NOT NULL REFERENCES survey_item(id)

);

CREATE TABLE survey_instance (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username TEXT NOT NULL,
    survey_id BIGINT NOT NULL REFERENCES survey(id),
    survey_instance_id BIGINT NOT NULL REFERENCES survey_instance(id),
    chosen_answer TEXT,
    is_correct BOOLEAN,
    state TEXT NOT NULL
);