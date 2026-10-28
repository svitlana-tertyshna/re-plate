
CREATE TABLE recipes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    original_text TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE adaptations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id UUID NOT NULL
        REFERENCES recipes(id) ON DELETE CASCADE,
    goal VARCHAR(32) NOT NULL
        CHECK (goal IN ('VEGAN', 'CALORIE_CONSCIOUS')),
    generated_recipe JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_adaptations_recipe_id
    ON adaptations(recipe_id);
