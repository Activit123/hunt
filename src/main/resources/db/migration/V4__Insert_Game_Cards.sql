-- V4__Insert_Game_Cards.sql
-- Inserează cele 21 de cartonașe în tabela 'card'

-- NOTĂ: Înlocuiește 'YOUR_CLOUDINARY_PLACEHOLDER_URL/' cu URL-ul real.
-- EX: 'https://res.cloudinary.com/dsetewmpw/image/upload/v1678888888/'

INSERT INTO card (name, description, image_url, type, drop_rate_weight) VALUES
('PEEK', 'Cu această putere puteți vedea toate puterile existente pentru fiecare echipă', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/peek.png', 'ADVANTAGE', 10),
('MINI BOSS', 'Cineva din echipă trebuie să joace șah sau table cu cineva din parcul de lângă Policlinică', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/mini_boss.png', 'DISADVANTAGE', 10),
('CARD THIEF', 'Cu această putere puteți fura un avantaj de la o altă echipă', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/card_thief.png', 'ADVANTAGE', 5),
('NO CONNECTION', 'Echipa nu are voie să folosească internetul până nu termină următoarea provocare', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/no_connection.png', 'DISADVANTAGE', 15),
('ALL TOGETHER', 'Echipa trebuie să se țină de mână până la următoarea locație', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/all_together.png', 'DISADVANTAGE', 10),
('DEAFENING SILENCE', 'Până la următoarea locație echipa nu va avea voie să vorbească. Dacă cineva din echipă vorbește, toată echipa trebuie să stea pe loc 5 minute', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/deafening_silence.png', 'DISADVANTAGE', 10),
('SEEK KNOWLEDGE', 'Puteți folosi internetul în timpul unei provocări', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/seek_knowledge.png', 'ADVANTAGE', 15),
('D4C', 'Acest cartonas poate fi folosit pentru a putea folosi o carte din mana de doua ori (exceptie fiind cartile Legendare)', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/d4c.png', 'LEGENDARY', 1),
('A WAY OUT', 'Puteți trece peste o provocare fără a trebui să o completați. (Nu aveți voie să treceți peste ultima provocare)', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/a_way_out.png', 'LEGENDARY', 1),
('ONE-LEGGED LEAP', 'Până la următoarea locație, cel puțin un membru al echipei trebuie să meargă într-un picior/țopăie', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/one_legged_leap.png', 'DISADVANTAGE', 10),
('BLINDFOLDED ESCORT', 'Până la următoarea locație, un membru din echipă trebuie să fie legat la ochi și să fie ajutat de ceilalți membri să ajungă până acolo', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/blindfolded_escort.png', 'DISADVANTAGE', 10),
('POETIC TRIAD', 'Echipa trebuie să facă o strofă de poezie pe baza a 3 cuvinte stabilite de supraveghetor', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/poetic_triad.png', 'DISADVANTAGE', 10),
('REVERSE', 'Atunci când primiţi un blestem sau provocare obligatorie, puteţi folosi această carte să inversaţi blestemul către echipa respectivă', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/reverse.png', 'ADVANTAGE', 5),
('DENY', 'Cu această putere puteţi nega un blestem sau un dezavantaj', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/deny.png', 'ADVANTAGE', 5),
('DEFLECT', 'Cu această putere puteţi pasa la alta echipă un blestem primit sau un dezavantaj', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/deflect.png', 'ADVANTAGE', 5),
('CURRENT STATE', 'Cu această putere puteţi vedea câte task-uri mai au de făcut echipele adverse', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/current_state.png', 'ADVANTAGE', 10),
('HACKER MAN', 'Un crypto-bro te-a hack-uit. -2 puncte', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/hacker_man.png', 'DISADVANTAGE', 8),
('TREASURE', '+3 Puncte', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/treasure.png', 'ADVANTAGE', 12),
('GOBLIN THIEF', 'Vei fura un punct de la echipa de pe primul loc dar vei pierde 1 punct daca echipa ta este pe primul loc', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/goblin_thief.png', 'ADVANTAGE', 5),
('GOLD COINS', '+2 Puncte', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/gold_coins.png', 'ADVANTAGE', 15),
('MAGIC LAMP', '+4 Puncte', 'YOUR_CLOUDINARY_PLACEHOLDER_URL/magic_lamp.png', 'ADVANTAGE', 5);