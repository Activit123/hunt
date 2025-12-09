-- 1. Eliminăm coloana is_frozen din Team
-- Logica s-a mutat în ActiveEffect, deci nu mai avem nevoie de flag pe echipă.
ALTER TABLE team DROP COLUMN IF EXISTS is_frozen;

-- 2. Asigurăm existența tabelului SupervisorAssignment
-- (În caz că ai rulat o versiune inițială de V1 care nu îl avea)
CREATE TABLE IF NOT EXISTS supervisor_assignment (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    team_id BIGINT,
    CONSTRAINT fk_assignment_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_assignment_team FOREIGN KEY (team_id) REFERENCES team(id)
);