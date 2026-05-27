CREATE TABLE IF NOT EXISTS LABORATORIJA (
    id_lab INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(100) NOT NULL,
    opis_lokacije VARCHAR(255),
    max_kapacitet INT NOT NULL,
    PRIMARY KEY (id_lab),
    UNIQUE KEY uq_lab_naziv (naziv),
    CONSTRAINT ck_lab_kapacitet CHECK (max_kapacitet > 0)
);

CREATE TABLE IF NOT EXISTS RESURS (
    id_resursa INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(100) NOT NULL,
    opis TEXT,
    jedinica_mere VARCHAR(30) NOT NULL,
    PRIMARY KEY (id_resursa),
    UNIQUE KEY uq_resurs_naziv (naziv)
);

CREATE TABLE IF NOT EXISTS TIP_ALATA (
    id_tip INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(100) NOT NULL,
    opis TEXT,
    PRIMARY KEY (id_tip),
    UNIQUE KEY uq_tip_naziv (naziv)
);

CREATE TABLE IF NOT EXISTS ISTRAZIVAC (
    id_istrazivac INT NOT NULL AUTO_INCREMENT,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    datum_rodjenja DATE NOT NULL,
    email VARCHAR(100) NOT NULL,
    tel VARCHAR(30),
    korisnicko_ime VARCHAR(50) NOT NULL,
    titula VARCHAR(50),
    specijalizacija VARCHAR(100),
    godine_iskustva INT,
    institucija VARCHAR(150),
    PRIMARY KEY (id_istrazivac),
    UNIQUE KEY uq_istr_email (email),
    UNIQUE KEY uq_istr_korime (korisnicko_ime),
    CONSTRAINT ck_istr_iskustvo CHECK (godine_iskustva IS NULL OR godine_iskustva >= 0)
);

CREATE TABLE IF NOT EXISTS TEORIJA (
    id_teorije INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(100) NOT NULL,
    oblast VARCHAR(100) NOT NULL,
    autor_glavni VARCHAR(100),
    godina YEAR,
    opis TEXT,
    PRIMARY KEY (id_teorije),
    UNIQUE KEY uq_teorija_naziv (naziv)
);

CREATE TABLE IF NOT EXISTS UPITNIK (
    id_upitnik INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(150) NOT NULL,
    opis TEXT,
    autor VARCHAR(100),
    godina YEAR,
    tip_skale ENUM('Likert_5','Likert_7','binarno','VAS','numericko','slobodan_odgovor') NOT NULL,
    broj_pitanja INT NOT NULL,
    konstrukt VARCHAR(100) NOT NULL,
    jezik VARCHAR(50) NOT NULL DEFAULT 'srpski',
    PRIMARY KEY (id_upitnik),
    UNIQUE KEY uq_upitnik_naziv (naziv),
    CONSTRAINT ck_upitnik_pitanja CHECK (broj_pitanja >= 1)
);

CREATE TABLE IF NOT EXISTS ETICKI_ODBOR (
    id_odbor INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(150) NOT NULL,
    institucija VARCHAR(150) NOT NULL,
    kontakt_email VARCHAR(100),
    PRIMARY KEY (id_odbor),
    UNIQUE KEY uq_odbor_naziv (naziv),
    UNIQUE KEY uq_odbor_email (kontakt_email)
);

CREATE TABLE IF NOT EXISTS ISPITANIK (
    id_ispitanik INT NOT NULL AUTO_INCREMENT,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    datum_rodjenja DATE NOT NULL,
    pol ENUM('M','Z','drugo') NOT NULL,
    obrazovanje VARCHAR(100),
    jezik VARCHAR(50),
    dominantna_ruka ENUM('leva','desna','obe'),
    datum_pristanka DATE NOT NULL,
    aktivan BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_ispitanik)
);

CREATE TABLE IF NOT EXISTS ALAT (
    id_alat INT NOT NULL AUTO_INCREMENT,
    id_tip INT NOT NULL,
    id_lab INT NOT NULL,
    nabavljen DATE NOT NULL,
    proizveden DATE,
    PRIMARY KEY (id_alat),
    CONSTRAINT fk_alat_tip FOREIGN KEY (id_tip) REFERENCES TIP_ALATA(id_tip)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_alat_lab FOREIGN KEY (id_lab) REFERENCES LABORATORIJA(id_lab)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_alat_datum CHECK (proizveden IS NULL OR proizveden <= nabavljen)
);

CREATE TABLE IF NOT EXISTS KVALIFIKACIJA (
    id_kvalifikacije INT NOT NULL AUTO_INCREMENT,
    id_istrazivac INT NOT NULL,
    naziv VARCHAR(150) NOT NULL,
    oblast VARCHAR(100),
    opis TEXT,
    datum DATE,
    ustanova VARCHAR(150),
    PRIMARY KEY (id_kvalifikacije),
    CONSTRAINT fk_kval_istr FOREIGN KEY (id_istrazivac) REFERENCES ISTRAZIVAC(id_istrazivac)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS EKSPERIMENT (
    id_eksperiment INT NOT NULL AUTO_INCREMENT,
    naziv VARCHAR(150) NOT NULL,
    opis TEXT,
    hipoteza TEXT NOT NULL,
    zavisne_var VARCHAR(255),
    nezavisne_var VARCHAR(255),
    ciljna_populacija VARCHAR(150),
    min_isp INT NOT NULL,
    max_isp INT NOT NULL,
    trajanje_min INT NOT NULL,
    id_teorije INT NOT NULL,
    PRIMARY KEY (id_eksperiment),
    CONSTRAINT fk_eksp_teorija FOREIGN KEY (id_teorije) REFERENCES TEORIJA(id_teorije)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_eksp_isp CHECK (min_isp >= 1 AND min_isp <= max_isp),
    CONSTRAINT ck_eksp_trajanje CHECK (trajanje_min > 0)
);

CREATE TABLE IF NOT EXISTS PROTOKOL (
    id_protokol INT NOT NULL AUTO_INCREMENT,
    id_eksperiment INT NOT NULL,
    naziv_faze VARCHAR(100) NOT NULL,
    redosled INT NOT NULL,
    trajanje_min INT NOT NULL,
    uputstvo TEXT,
    PRIMARY KEY (id_protokol),
    UNIQUE KEY uq_protokol_redosled (id_eksperiment, redosled),
    CONSTRAINT fk_prot_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT ck_prot_redosled CHECK (redosled >= 1),
    CONSTRAINT ck_prot_trajanje CHECK (trajanje_min > 0)
);

CREATE TABLE IF NOT EXISTS IZVODJENJE (
    id_izvodjenje INT NOT NULL AUTO_INCREMENT,
    id_eksperiment INT NOT NULL,
    id_lab INT NOT NULL,
    datum DATE NOT NULL,
    status ENUM('planirano','zapoceto','otkazano','zavrseno_uspesno','zavrseno_neuspesno') NOT NULL DEFAULT 'planirano',
    napomena TEXT,
    PRIMARY KEY (id_izvodjenje),
    CONSTRAINT fk_izv_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_izv_lab FOREIGN KEY (id_lab) REFERENCES LABORATORIJA(id_lab)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ODOBRENJE (
    id_odobrenje INT NOT NULL AUTO_INCREMENT,
    id_eksperiment INT NOT NULL,
    id_odbor INT NOT NULL,
    datum_podnosenja DATE NOT NULL,
    datum_odluke DATE,
    status ENUM('na_cekanju','odobreno','odbijeno','uslovljeno') NOT NULL DEFAULT 'na_cekanju',
    napomena TEXT,
    PRIMARY KEY (id_odobrenje),
    CONSTRAINT fk_odobr_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_odobr_odbor FOREIGN KEY (id_odbor) REFERENCES ETICKI_ODBOR(id_odbor)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_odobr_datum CHECK (datum_odluke IS NULL OR datum_odluke >= datum_podnosenja)
);

CREATE TABLE IF NOT EXISTS SESIJA (
    id_sesija INT NOT NULL AUTO_INCREMENT,
    id_izvodjenje INT NOT NULL,
    datum DATE NOT NULL,
    pocetak TIME NOT NULL,
    zavrsetak TIME NOT NULL,
    status ENUM('zakazana','u_toku','zavrsena','otkazana') NOT NULL DEFAULT 'zakazana',
    napomena TEXT,
    PRIMARY KEY (id_sesija),
    CONSTRAINT fk_ses_izv FOREIGN KEY (id_izvodjenje) REFERENCES IZVODJENJE(id_izvodjenje)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT ck_ses_vreme CHECK (pocetak < zavrsetak)
);

CREATE TABLE IF NOT EXISTS POTREBAN_RESURS (
    id_eksperiment INT NOT NULL,
    id_resursa INT NOT NULL,
    potrebna_kolicina INT NOT NULL,
    PRIMARY KEY (id_eksperiment, id_resursa),
    CONSTRAINT fk_potr_res_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_potr_res_res FOREIGN KEY (id_resursa) REFERENCES RESURS(id_resursa)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_potr_res_kol CHECK (potrebna_kolicina > 0)
);

CREATE TABLE IF NOT EXISTS POTREBAN_ALAT (
    id_eksperiment INT NOT NULL,
    id_tip INT NOT NULL,
    PRIMARY KEY (id_eksperiment, id_tip),
    CONSTRAINT fk_potr_alat_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_potr_alat_tip FOREIGN KEY (id_tip) REFERENCES TIP_ALATA(id_tip)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS KORISCEN_UPITNIK (
    id_eksperiment INT NOT NULL,
    id_upitnik INT NOT NULL,
    PRIMARY KEY (id_eksperiment, id_upitnik),
    CONSTRAINT fk_kor_upit_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_kor_upit_upit FOREIGN KEY (id_upitnik) REFERENCES UPITNIK(id_upitnik)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS INVENTAR_LABORATORIJE (
    id_lab INT NOT NULL,
    id_resursa INT NOT NULL,
    kolicina INT NOT NULL,
    rezervisana_kolicina INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id_lab, id_resursa),
    CONSTRAINT fk_inv_lab FOREIGN KEY (id_lab) REFERENCES LABORATORIJA(id_lab)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_inv_res FOREIGN KEY (id_resursa) REFERENCES RESURS(id_resursa)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_inv_kolicina CHECK (kolicina >= 0),
    CONSTRAINT ck_inv_rezervisana CHECK (rezervisana_kolicina >= 0),
    CONSTRAINT ck_inv_rezerv_max CHECK (rezervisana_kolicina <= kolicina)
);

CREATE TABLE IF NOT EXISTS DIZAJNER (
    id_eksperiment INT NOT NULL,
    id_istrazivac INT NOT NULL,
    PRIMARY KEY (id_eksperiment, id_istrazivac),
    CONSTRAINT fk_diz_eksp FOREIGN KEY (id_eksperiment) REFERENCES EKSPERIMENT(id_eksperiment)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_diz_istr FOREIGN KEY (id_istrazivac) REFERENCES ISTRAZIVAC(id_istrazivac)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS TIM_IZVODJACA (
    id_izvodjenje INT NOT NULL,
    id_istrazivac INT NOT NULL,
    uloga VARCHAR(100) NOT NULL,
    beleske VARCHAR(255),
    PRIMARY KEY (id_izvodjenje, id_istrazivac),
    CONSTRAINT fk_tim_izv FOREIGN KEY (id_izvodjenje) REFERENCES IZVODJENJE(id_izvodjenje)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_tim_istr FOREIGN KEY (id_istrazivac) REFERENCES ISTRAZIVAC(id_istrazivac)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS UPOTREBA_RESURSA (
    id_sesija INT NOT NULL,
    id_resursa INT NOT NULL,
    kolicina_iskoriscenog INT NOT NULL,
    PRIMARY KEY (id_sesija, id_resursa),
    CONSTRAINT fk_upotr_res_ses FOREIGN KEY (id_sesija) REFERENCES SESIJA(id_sesija)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_upotr_res_res FOREIGN KEY (id_resursa) REFERENCES RESURS(id_resursa)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_upotr_res_kol CHECK (kolicina_iskoriscenog > 0)
);

CREATE TABLE IF NOT EXISTS UPOTREBA_ALATA (
    id_sesija INT NOT NULL,
    id_alat INT NOT NULL,
    ispravan BOOLEAN NOT NULL DEFAULT TRUE,
    napomena VARCHAR(255),
    PRIMARY KEY (id_sesija, id_alat),
    CONSTRAINT fk_upotr_alat_ses FOREIGN KEY (id_sesija) REFERENCES SESIJA(id_sesija)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_upotr_alat_alat FOREIGN KEY (id_alat) REFERENCES ALAT(id_alat)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS UCESCE (
    id_ispitanik INT NOT NULL,
    id_sesija INT NOT NULL,
    prisutan BOOLEAN NOT NULL DEFAULT FALSE,
    ostvario_nagradu BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id_ispitanik, id_sesija),
    CONSTRAINT fk_ucesce_isp FOREIGN KEY (id_ispitanik) REFERENCES ISPITANIK(id_ispitanik)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_ucesce_ses FOREIGN KEY (id_sesija) REFERENCES SESIJA(id_sesija)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS REZULTAT_UPITNIKA (
    id_ispitanik INT NOT NULL,
    id_sesija INT NOT NULL,
    id_upitnik INT NOT NULL,
    rezultat DECIMAL(6,2) NOT NULL,
    datum_vreme DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    napomena TEXT,
    PRIMARY KEY (id_ispitanik, id_sesija, id_upitnik),
    CONSTRAINT fk_rez_isp FOREIGN KEY (id_ispitanik) REFERENCES ISPITANIK(id_ispitanik)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_rez_ses FOREIGN KEY (id_sesija) REFERENCES SESIJA(id_sesija)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_rez_upit FOREIGN KEY (id_upitnik) REFERENCES UPITNIK(id_upitnik)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT ck_rez_rezultat CHECK (rezultat >= 0)
);

DROP PROCEDURE IF EXISTS seed_random_psihologija_lab;
DELIMITER //
CREATE PROCEDURE seed_random_psihologija_lab()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE seed_id INT;
    WHILE i <= 50 DO
        SET seed_id = 10000 + i;

        INSERT IGNORE INTO LABORATORIJA VALUES (seed_id, CONCAT('Seed laboratorija ', i), CONCAT('Sprat ', 1 + (i % 5), ', soba ', 100 + i), 8 + (i % 18));
        INSERT IGNORE INTO RESURS VALUES (seed_id, CONCAT('Seed resurs ', i), CONCAT('Opis resursa ', i), ELT(1 + (i % 4), 'kom', 'ml', 'g', 'paket'));
        INSERT IGNORE INTO TIP_ALATA VALUES (seed_id, CONCAT('Seed tip alata ', i), CONCAT('Psiholoski alat za merenje ', i));
        INSERT IGNORE INTO ISTRAZIVAC VALUES (seed_id, CONCAT('Ime', i), CONCAT('Prezime', i), DATE_ADD('1975-01-01', INTERVAL i * 97 DAY), CONCAT('seed', i, '@lab.test'), CONCAT('+38160100', LPAD(i, 2, '0')), CONCAT('luka_seed_', i), ELT(1 + (i % 3), 'asistent', 'docent', 'profesor'), ELT(1 + (i % 4), 'kognitivna psihologija', 'klinicka psihologija', 'neuropsihologija', 'socijalna psihologija'), i % 20, CONCAT('Seed institut ', 1 + (i % 7)));
        INSERT IGNORE INTO TEORIJA VALUES (seed_id, CONCAT('Seed teorija ', i), ELT(1 + (i % 4), 'paznja', 'pamcenje', 'emocije', 'ucenje'), CONCAT('Autor ', i), 1980 + (i % 40), CONCAT('Opis teorije ', i));
        INSERT IGNORE INTO UPITNIK VALUES (seed_id, CONCAT('Seed upitnik ', i), CONCAT('Opis upitnika ', i), CONCAT('Autor ', i), 1990 + (i % 30), ELT(1 + (i % 6), 'Likert_5','Likert_7','binarno','VAS','numericko','slobodan_odgovor'), 5 + (i % 30), ELT(1 + (i % 4), 'anksioznost', 'motivacija', 'radna memorija', 'empatija'), 'srpski');
        INSERT IGNORE INTO ETICKI_ODBOR VALUES (seed_id, CONCAT('Seed eticki odbor ', i), CONCAT('Institucija ', 1 + (i % 8)), CONCAT('odbor', i, '@lab.test'));
        INSERT IGNORE INTO ISPITANIK VALUES (seed_id, CONCAT('IspitanikIme', i), CONCAT('IspitanikPrezime', i), DATE_ADD('1990-01-01', INTERVAL i * 83 DAY), ELT(1 + (i % 3), 'M', 'Z', 'drugo'), ELT(1 + (i % 4), 'srednja skola', 'osnovne studije', 'master', 'doktorat'), 'srpski', ELT(1 + (i % 3), 'leva', 'desna', 'obe'), DATE_ADD('2023-01-01', INTERVAL i DAY), TRUE);
        INSERT IGNORE INTO ALAT VALUES (seed_id, seed_id, seed_id, DATE_ADD('2021-01-01', INTERVAL i DAY), DATE_ADD('2020-01-01', INTERVAL i DAY));
        INSERT IGNORE INTO KVALIFIKACIJA VALUES (seed_id, seed_id, CONCAT('Seed kvalifikacija ', i), ELT(1 + (i % 4), 'metodologija', 'statistika', 'psihometrija', 'eksperimentalni dizajn'), CONCAT('Opis kvalifikacije ', i), DATE_ADD('2018-01-01', INTERVAL i * 11 DAY), CONCAT('Ustanova ', i));
        INSERT IGNORE INTO EKSPERIMENT VALUES (seed_id, CONCAT('Seed eksperiment ', i), CONCAT('Opis eksperimenta ', i), CONCAT('Hipoteza ', i), 'reakciono vreme', 'tip stimulusa', ELT(1 + (i % 4), 'studenti', 'odrasli', 'adolescenti', 'istrazivaci'), 5, 20 + (i % 20), 30 + (i % 90), seed_id);
        INSERT IGNORE INTO PROTOKOL VALUES (seed_id, seed_id, CONCAT('Seed faza ', i), 1, 10 + (i % 50), CONCAT('Uputstvo za fazu ', i));
        INSERT IGNORE INTO IZVODJENJE VALUES (seed_id, seed_id, seed_id, DATE_ADD('2026-01-01', INTERVAL i DAY), ELT(1 + (i % 5), 'planirano','zapoceto','otkazano','zavrseno_uspesno','zavrseno_neuspesno'), CONCAT('Napomena izvodjenja ', i));
        INSERT IGNORE INTO ODOBRENJE VALUES (seed_id, seed_id, seed_id, DATE_ADD('2025-01-01', INTERVAL i DAY), DATE_ADD('2025-01-01', INTERVAL i + 10 DAY), ELT(1 + (i % 4), 'na_cekanju','odobreno','odbijeno','uslovljeno'), CONCAT('Napomena odobrenja ', i));
        INSERT IGNORE INTO SESIJA VALUES (seed_id, seed_id, DATE_ADD('2026-01-01', INTERVAL i DAY), MAKETIME(8 + (i % 8), 0, 0), MAKETIME(9 + (i % 8), 0, 0), ELT(1 + (i % 4), 'zakazana','u_toku','zavrsena','otkazana'), CONCAT('Napomena sesije ', i));
        INSERT IGNORE INTO POTREBAN_RESURS VALUES (seed_id, seed_id, 1 + (i % 5));
        INSERT IGNORE INTO POTREBAN_ALAT VALUES (seed_id, seed_id);
        INSERT IGNORE INTO KORISCEN_UPITNIK VALUES (seed_id, seed_id);
        INSERT IGNORE INTO INVENTAR_LABORATORIJE VALUES (seed_id, seed_id, 20 + (i % 30), i % 10);
        INSERT IGNORE INTO DIZAJNER VALUES (seed_id, seed_id);
        INSERT IGNORE INTO TIM_IZVODJACA VALUES (seed_id, seed_id, ELT(1 + (i % 3), 'glavni izvodjac', 'asistent', 'koordinator'), CONCAT('Beleska tima ', i));
        INSERT IGNORE INTO UPOTREBA_RESURSA VALUES (seed_id, seed_id, 1 + (i % 4));
        INSERT IGNORE INTO UPOTREBA_ALATA VALUES (seed_id, seed_id, i % 5 <> 0, CONCAT('Napomena alata ', i));
        INSERT IGNORE INTO UCESCE VALUES (seed_id, seed_id, i % 4 <> 0, i % 3 = 0);
        INSERT IGNORE INTO REZULTAT_UPITNIKA VALUES (seed_id, seed_id, seed_id, 10 + (i % 90), DATE_ADD('2026-01-01 10:00:00', INTERVAL i DAY), CONCAT('Napomena rezultata ', i));

        SET i = i + 1;
    END WHILE;
END//
DELIMITER ;

CALL seed_random_psihologija_lab();
DROP PROCEDURE seed_random_psihologija_lab;
