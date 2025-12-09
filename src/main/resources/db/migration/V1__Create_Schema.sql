-- 1. Echipe
CREATE TABLE team (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    login_code VARCHAR(50) UNIQUE NOT NULL,
    score INTEGER DEFAULT 0,
    current_lat DOUBLE PRECISION,
    current_lng DOUBLE PRECISION,
    is_frozen BOOLEAN DEFAULT FALSE
);

-- 2. Utilizatori (Admin / Supraveghetor)
CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- 3. Asignare Supraveghetor -> Echipă
CREATE TABLE supervisor_assignment (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    team_id BIGINT,
    CONSTRAINT fk_assignment_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_assignment_team FOREIGN KEY (team_id) REFERENCES team(id)
);

-- 4. Cartonașe
CREATE TABLE card (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    image_url VARCHAR(255),
    type VARCHAR(50) NOT NULL,
    drop_rate_weight INTEGER DEFAULT 10
);

-- 5. Inventar Echipe
CREATE TABLE team_inventory (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    acquired_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inventory_team FOREIGN KEY (team_id) REFERENCES team(id),
    CONSTRAINT fk_inventory_card FOREIGN KEY (card_id) REFERENCES card(id)
);

-- 6. Efecte Active
CREATE TABLE active_effect (
    id BIGSERIAL PRIMARY KEY,
    target_team_id BIGINT NOT NULL,
    attacker_team_id BIGINT,
    card_id BIGINT NOT NULL,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_effect_target FOREIGN KEY (target_team_id) REFERENCES team(id),
    CONSTRAINT fk_effect_attacker FOREIGN KEY (attacker_team_id) REFERENCES team(id),
    CONSTRAINT fk_effect_card FOREIGN KEY (card_id) REFERENCES card(id)
);

-- 7. Puncte de Interes (POI)
CREATE TABLE poi (
    id BIGSERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    radius INTEGER DEFAULT 50,
    challenge_text TEXT,
    claim_code VARCHAR(50),
    reward_points INTEGER DEFAULT 100,
    is_intermediate BOOLEAN DEFAULT FALSE
);

-- 8. Progres Echipă (Istoric locații vizitate)
CREATE TABLE team_progress (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL,
    poi_id BIGINT NOT NULL,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_progress_team FOREIGN KEY (team_id) REFERENCES team(id),
    CONSTRAINT fk_progress_poi FOREIGN KEY (poi_id) REFERENCES poi(id)
);