-- V3__Create_DiscoveredPoi_Table.sql
-- Creează o tabelă separată pentru a persista POI-urile care au fost doar descoperite (fără revendicare/puncte)

CREATE TABLE discovered_poi (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL,
    poi_id BIGINT NOT NULL,
    discovered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Nu poți descoperi același POI de două ori
    CONSTRAINT uk_discovered_poi UNIQUE (team_id, poi_id),

    CONSTRAINT fk_discovered_team FOREIGN KEY (team_id) REFERENCES team(id),
    CONSTRAINT fk_discovered_poi FOREIGN KEY (poi_id) REFERENCES poi(id)
);