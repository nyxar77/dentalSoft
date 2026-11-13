-- Création de la base de données utilisée par l'application DentalSoft.
CREATE SCHEMA IF NOT EXISTS dentalsoft
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE dentalsoft;

-- Supprime l'ancienne table avant de la recréer avec des données propres.
DROP TABLE IF EXISTS patients;

-- Structure SQL correspondant à la classe Patient :
--   idPatient (Long) -> id_patient BIGINT
--   nom       (String) -> nom VARCHAR
--   age       (Integer) -> age INT
CREATE TABLE IF NOT EXISTS patients (
    id_patient BIGINT NOT NULL AUTO_INCREMENT,
    nom        VARCHAR(100) NOT NULL,
    age        INT NOT NULL,
    CONSTRAINT pk_patients PRIMARY KEY (id_patient),
    CONSTRAINT chk_patients_age CHECK (age >= 0)
);

-- Données initiales de test.
INSERT INTO patients (id_patient, nom, age)
VALUES
    (1,  'Fadoua',  20),
    (2,  'Omar',    22),
    (4,  'Souhail', 23),
    (5,  'Amine',   18),
    (6,  'Tarek',   17),
    (7,  'Amina',   20),
    (8,  'Tanae',   30),
    (9,  'Ayman',   26),
    (10, 'Btihal',  27);
