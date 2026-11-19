-- Schéma de la base de données DentalSoft.
CREATE SCHEMA IF NOT EXISTS dentalsoft
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE dentalsoft;

DROP TABLE IF EXISTS certificats;
DROP TABLE IF EXISTS prescriptions;
DROP TABLE IF EXISTS medicaments;
DROP TABLE IF EXISTS ordonnances;
DROP TABLE IF EXISTS ligne_consultations;
DROP TABLE IF EXISTS actes;
DROP TABLE IF EXISTS factures;
DROP TABLE IF EXISTS consultations;
DROP TABLE IF EXISTS rdv;
DROP TABLE IF EXISTS antecedents;
DROP TABLE IF EXISTS dossier_medical;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id           INT          NOT NULL AUTO_INCREMENT,
    nom          VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role         VARCHAR(20)  NOT NULL,
    specialite   VARCHAR(100) NULL, -- Uniquement pour role = 'MEDECIN'
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'MEDECIN', 'SECRETAIRE'))
);

CREATE TABLE patients (
    id_p     INT          NOT NULL AUTO_INCREMENT,
    nom_p    VARCHAR(100) NOT NULL,
    prenom_p VARCHAR(100) NOT NULL,
    age      INT          NOT NULL,
    email    VARCHAR(150) NULL,
    tel      VARCHAR(30)  NULL,
    photo    VARCHAR(255) NULL,
    CONSTRAINT pk_patients PRIMARY KEY (id_p),
    CONSTRAINT uq_patients_email UNIQUE (email),
    CONSTRAINT chk_patients_age CHECK (age >= 0)
);

CREATE TABLE dossier_medical (
    id_dm               INT  NOT NULL AUTO_INCREMENT,
    date_creation       DATE NOT NULL,
    id_patient          INT  NOT NULL,
    id_medecin_traitant INT  NOT NULL,
    CONSTRAINT pk_dossier_medical PRIMARY KEY (id_dm),
    CONSTRAINT uq_dossier_patient UNIQUE (id_patient),
    CONSTRAINT fk_dm_patient FOREIGN KEY (id_patient) REFERENCES patients(id_p) ON DELETE RESTRICT,
    CONSTRAINT fk_dm_medecin FOREIGN KEY (id_medecin_traitant) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE antecedents (
    id          INT          NOT NULL AUTO_INCREMENT,
    type        VARCHAR(100) NOT NULL,
    description TEXT         NULL,
    id_dm       INT          NOT NULL,
    CONSTRAINT pk_antecedents PRIMARY KEY (id),
    CONSTRAINT fk_antecedent_dm FOREIGN KEY (id_dm) REFERENCES dossier_medical(id_dm) ON DELETE CASCADE
);

CREATE TABLE rdv (
    id_rdv        INT          NOT NULL AUTO_INCREMENT,
    date          DATE         NOT NULL,
    heure         TIME         NOT NULL,
    motif         VARCHAR(255) NULL,
    statut        VARCHAR(20)  NOT NULL DEFAULT 'PLANIFIE',
    id_secretaire INT          NOT NULL,
    id_medecin    INT          NOT NULL,
    id_dm         INT          NOT NULL,
    CONSTRAINT pk_rdv PRIMARY KEY (id_rdv),
    CONSTRAINT chk_rdv_statut CHECK (statut IN ('PLANIFIE', 'CONFIRME', 'ANNULE', 'TERMINE')),
    CONSTRAINT fk_rdv_secretaire FOREIGN KEY (id_secretaire) REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_rdv_medecin    FOREIGN KEY (id_medecin)    REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_rdv_dm         FOREIGN KEY (id_dm)         REFERENCES dossier_medical(id_dm) ON DELETE RESTRICT
);

CREATE INDEX idx_rdv_medecin_date_heure ON rdv (id_medecin, date, heure);

CREATE TABLE consultations (
    id_c         INT  NOT NULL AUTO_INCREMENT,
    date_c       DATE NOT NULL,
    note_medecin TEXT NULL,
    id_dm        INT  NOT NULL,
    id_rdv       INT  NOT NULL,
    CONSTRAINT pk_consultations PRIMARY KEY (id_c),
    CONSTRAINT uq_consultations_rdv UNIQUE (id_rdv),
    CONSTRAINT fk_consult_dm  FOREIGN KEY (id_dm)  REFERENCES dossier_medical(id_dm) ON DELETE RESTRICT,
    CONSTRAINT fk_consult_rdv FOREIGN KEY (id_rdv) REFERENCES rdv(id_rdv) ON DELETE RESTRICT
);

CREATE TABLE factures (
    id              INT            NOT NULL AUTO_INCREMENT,
    montant_total   DECIMAL(10, 2) NOT NULL,
    montant_paye    DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    date            DATE           NOT NULL,
    statut          VARCHAR(20)    NOT NULL DEFAULT 'NON_PAYEE',
    id_consultation INT            NOT NULL,
    CONSTRAINT pk_factures PRIMARY KEY (id),
    CONSTRAINT uq_factures_consultation UNIQUE (id_consultation),
    CONSTRAINT chk_factures_statut CHECK (statut IN ('NON_PAYEE', 'PARTIELLE', 'PAYEE')),
    CONSTRAINT chk_factures_montants CHECK (montant_total >= 0 AND montant_paye >= 0 AND montant_paye <= montant_total),
    CONSTRAINT fk_facture_consult FOREIGN KEY (id_consultation) REFERENCES consultations(id_c) ON DELETE RESTRICT
);

CREATE TABLE actes (
    id_a         INT            NOT NULL AUTO_INCREMENT,
    nom          VARCHAR(150)   NOT NULL,
    categorie    VARCHAR(100)   NOT NULL,
    prix_de_base DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_actes PRIMARY KEY (id_a),
    CONSTRAINT chk_actes_prix CHECK (prix_de_base >= 0)
);

CREATE TABLE ligne_consultations (
    id              INT            NOT NULL AUTO_INCREMENT,
    prix_patient    DECIMAL(10, 2) NOT NULL,
    num_dent        INT            NULL,
    id_consultation INT            NOT NULL,
    id_acte         INT            NOT NULL,
    CONSTRAINT pk_ligne_consultations PRIMARY KEY (id),
    CONSTRAINT chk_lc_prix CHECK (prix_patient >= 0),
    CONSTRAINT chk_lc_num_dent CHECK (num_dent IS NULL OR num_dent > 0),
    CONSTRAINT fk_lc_consult FOREIGN KEY (id_consultation) REFERENCES consultations(id_c) ON DELETE CASCADE,
    CONSTRAINT fk_lc_acte    FOREIGN KEY (id_acte)         REFERENCES actes(id_a) ON DELETE RESTRICT
);

CREATE TABLE ordonnances (
    id              INT  NOT NULL AUTO_INCREMENT,
    date            DATE NOT NULL,
    id_consultation INT  NOT NULL,
    CONSTRAINT pk_ordonnances PRIMARY KEY (id),
    CONSTRAINT fk_ordonnance_consult FOREIGN KEY (id_consultation) REFERENCES consultations(id_c) ON DELETE RESTRICT
);

CREATE TABLE medicaments (
    id          INT            NOT NULL AUTO_INCREMENT,
    nom         VARCHAR(150)   NOT NULL,
    fournisseur VARCHAR(150)   NULL,
    prix_estime DECIMAL(10, 2) NOT NULL,
    CONSTRAINT pk_medicaments PRIMARY KEY (id),
    CONSTRAINT chk_medicaments_prix CHECK (prix_estime >= 0)
);

CREATE TABLE prescriptions (
    id            INT          NOT NULL AUTO_INCREMENT,
    posologie     VARCHAR(255) NOT NULL,
    id_ordonnance INT          NOT NULL,
    id_medicament INT          NOT NULL,
    CONSTRAINT pk_prescriptions PRIMARY KEY (id),
    CONSTRAINT fk_presc_ordonnance FOREIGN KEY (id_ordonnance) REFERENCES ordonnances(id) ON DELETE CASCADE,
    CONSTRAINT fk_presc_medicament FOREIGN KEY (id_medicament) REFERENCES medicaments(id) ON DELETE RESTRICT
);

CREATE TABLE certificats (
    id              INT  NOT NULL AUTO_INCREMENT,
    date            DATE NOT NULL,
    contenu         TEXT NOT NULL,
    duree_repos     INT  NOT NULL,
    id_consultation INT  NOT NULL,
    CONSTRAINT pk_certificats PRIMARY KEY (id),
    CONSTRAINT chk_certificats_duree CHECK (duree_repos >= 0),
    CONSTRAINT fk_certificat_consult FOREIGN KEY (id_consultation) REFERENCES consultations(id_c) ON DELETE RESTRICT
);
