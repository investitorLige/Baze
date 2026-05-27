package lab.features.experiments.queries;

public final class ExperimentQueries {
    public static final String FIND_LABORATORIES = """
            SELECT id_lab, naziv, opis_lokacije, max_kapacitet
            FROM LABORATORIJA
            ORDER BY naziv
            """;

    public static final String FIND_EXPERIMENTS = """
            SELECT
                e.id_eksperiment,
                e.naziv,
                t.naziv AS teorija,
                e.hipoteza,
                e.ciljna_populacija,
                e.trajanje_min
            FROM EKSPERIMENT e
            JOIN TEORIJA t ON e.id_teorije = t.id_teorije
            ORDER BY e.naziv
            """;

    public static final String FIND_EXECUTIONS = """
            SELECT
                iz.id_izvodjenje,
                e.naziv AS eksperiment,
                l.naziv AS laboratorija,
                iz.datum,
                iz.status,
                COALESCE(iz.napomena, '') AS napomena
            FROM IZVODJENJE iz
            JOIN EKSPERIMENT e ON iz.id_eksperiment = e.id_eksperiment
            JOIN LABORATORIJA l ON iz.id_lab = l.id_lab
            ORDER BY iz.datum ASC, iz.id_izvodjenje ASC
            """;

    public static final String FIND_EXECUTIONS_BY_LAB = """
            SELECT
                iz.id_izvodjenje,
                e.naziv AS eksperiment,
                l.naziv AS laboratorija,
                iz.datum,
                iz.status,
                COALESCE(iz.napomena, '') AS napomena
            FROM IZVODJENJE iz
            JOIN EKSPERIMENT e ON iz.id_eksperiment = e.id_eksperiment
            JOIN LABORATORIJA l ON iz.id_lab = l.id_lab
            WHERE iz.id_lab = ?
            ORDER BY iz.datum ASC, iz.id_izvodjenje ASC
            """;

    public static final String FIND_EXECUTIONS_BY_EXPERIMENT = """
            SELECT
                iz.id_izvodjenje,
                e.naziv AS eksperiment,
                l.naziv AS laboratorija,
                iz.datum,
                iz.status,
                COALESCE(iz.napomena, '') AS napomena
            FROM IZVODJENJE iz
            JOIN EKSPERIMENT e ON iz.id_eksperiment = e.id_eksperiment
            JOIN LABORATORIJA l ON iz.id_lab = l.id_lab
            WHERE iz.id_eksperiment = ?
            ORDER BY iz.datum ASC, iz.id_izvodjenje ASC
            """;

    public static final String FIND_EXECUTIONS_BY_FILTERS = """
            SELECT
                iz.id_izvodjenje,
                e.naziv AS eksperiment,
                l.naziv AS laboratorija,
                iz.datum,
                iz.status,
                COALESCE(iz.napomena, '') AS napomena
            FROM IZVODJENJE iz
            JOIN EKSPERIMENT e ON iz.id_eksperiment = e.id_eksperiment
            JOIN LABORATORIJA l ON iz.id_lab = l.id_lab
            WHERE (? IS NULL OR iz.id_lab = ?)
              AND (? IS NULL OR iz.id_eksperiment = ?)
            ORDER BY iz.datum ASC, iz.id_izvodjenje ASC
            """;

    public static final String FIND_THEORY_BY_EXPERIMENT = """
            SELECT
                t.id_teorije,
                t.naziv,
                t.oblast,
                t.autor_glavni,
                t.godina,
                COALESCE(t.opis, '') AS opis
            FROM EKSPERIMENT e
            JOIN TEORIJA t ON e.id_teorije = t.id_teorije
            WHERE e.id_eksperiment = ?
            """;

    public static final String FIND_PROTOCOL_BY_EXPERIMENT = """
            SELECT
                redosled,
                naziv_faze,
                trajanje_min,
                COALESCE(uputstvo, '') AS uputstvo
            FROM PROTOKOL
            WHERE id_eksperiment = ?
            ORDER BY redosled
            """;

    public static final String FIND_INVENTORY_BY_LAB = """
            SELECT
                r.id_resursa,
                r.naziv,
                r.jedinica_mere,
                il.kolicina,
                il.rezervisana_kolicina,
                il.kolicina - il.rezervisana_kolicina AS dostupno
            FROM INVENTAR_LABORATORIJE il
            JOIN RESURS r ON il.id_resursa = r.id_resursa
            WHERE il.id_lab = ?
            ORDER BY r.naziv
            """;

    public static final String FIND_TOOLS_BY_LAB = """
            SELECT
                a.id_alat,
                ta.naziv AS tip_alata,
                a.nabavljen,
                a.proizveden
            FROM ALAT a
            JOIN TIP_ALATA ta ON a.id_tip = ta.id_tip
            WHERE a.id_lab = ?
            ORDER BY ta.naziv, a.id_alat
            """;

    public static final String FIND_QUESTIONNAIRES_BY_EXPERIMENT = """
            SELECT
                u.id_upitnik,
                u.naziv,
                u.autor,
                u.godina,
                u.tip_skale,
                u.broj_pitanja,
                u.konstrukt,
                u.jezik
            FROM KORISCEN_UPITNIK ku
            JOIN UPITNIK u ON ku.id_upitnik = u.id_upitnik
            WHERE ku.id_eksperiment = ?
            ORDER BY u.naziv
            """;

    public static final String FIND_REQUIRED_RESOURCES_BY_EXPERIMENT = """
            SELECT
                r.id_resursa,
                r.naziv,
                pr.potrebna_kolicina,
                r.jedinica_mere,
                COALESCE(r.opis, '') AS opis
            FROM POTREBAN_RESURS pr
            JOIN RESURS r ON pr.id_resursa = r.id_resursa
            WHERE pr.id_eksperiment = ?
            ORDER BY r.naziv
            """;

    public static final String FIND_REQUIRED_TOOLS_BY_EXPERIMENT = """
            SELECT
                ta.id_tip,
                ta.naziv,
                COALESCE(ta.opis, '') AS opis
            FROM POTREBAN_ALAT pa
            JOIN TIP_ALATA ta ON pa.id_tip = ta.id_tip
            WHERE pa.id_eksperiment = ?
            ORDER BY ta.naziv
            """;

    public static final String FIND_APPROVALS_BY_EXPERIMENT = """
            SELECT
                eo.naziv AS eticki_odbor,
                eo.institucija,
                o.datum_podnosenja,
                o.datum_odluke,
                o.status,
                COALESCE(o.napomena, '') AS napomena
            FROM ODOBRENJE o
            JOIN ETICKI_ODBOR eo ON o.id_odbor = eo.id_odbor
            WHERE o.id_eksperiment = ?
            ORDER BY o.datum_podnosenja DESC
            """;

    public static final String FIND_DESIGNERS_BY_EXPERIMENT = """
            SELECT
                i.id_istrazivac,
                i.ime,
                i.prezime,
                i.titula,
                i.specijalizacija,
                i.institucija
            FROM DIZAJNER d
            JOIN ISTRAZIVAC i ON d.id_istrazivac = i.id_istrazivac
            WHERE d.id_eksperiment = ?
            ORDER BY i.prezime, i.ime
            """;

    public static final String FIND_TEAM_BY_EXECUTION = """
            SELECT
                i.id_istrazivac,
                i.ime,
                i.prezime,
                ti.uloga,
                COALESCE(ti.beleske, '') AS beleske
            FROM TIM_IZVODJACA ti
            JOIN ISTRAZIVAC i ON ti.id_istrazivac = i.id_istrazivac
            WHERE ti.id_izvodjenje = ?
            ORDER BY i.prezime, i.ime
            """;

    public static final String UPDATE_EXECUTION_STATUS = """
            UPDATE IZVODJENJE iz
            SET iz.status = ?
            WHERE iz.id_izvodjenje = ?
              AND (
                  EXISTS (
                      SELECT 1
                      FROM TIM_IZVODJACA ti
                      WHERE ti.id_izvodjenje = iz.id_izvodjenje
                        AND ti.id_istrazivac = ?
                  )
                  OR EXISTS (
                      SELECT 1
                      FROM DIZAJNER d
                      WHERE d.id_eksperiment = iz.id_eksperiment
                        AND d.id_istrazivac = ?
                  )
              )
            """;

    public static final String EXECUTION_REPORT = """
            SELECT
                iz.id_izvodjenje,
                e.naziv AS eksperiment,
                l.naziv AS laboratorija,
                iz.datum,
                iz.status,
                COUNT(DISTINCT s.id_sesija) AS broj_sesija,
                COUNT(DISTINCT CASE WHEN u.prisutan = TRUE THEN CONCAT(u.id_ispitanik, '-', u.id_sesija) END) AS broj_prisutnih,
                COUNT(DISTINCT CONCAT(ru.id_ispitanik, '-', ru.id_sesija, '-', ru.id_upitnik)) AS broj_rezultata,
                COALESCE(SUM(DISTINCT ur.kolicina_iskoriscenog), 0) AS ukupno_resursa,
                COUNT(DISTINCT ua.id_alat) AS broj_koriscenih_alata
            FROM IZVODJENJE iz
            JOIN EKSPERIMENT e ON iz.id_eksperiment = e.id_eksperiment
            JOIN LABORATORIJA l ON iz.id_lab = l.id_lab
            LEFT JOIN SESIJA s ON iz.id_izvodjenje = s.id_izvodjenje
            LEFT JOIN UCESCE u ON s.id_sesija = u.id_sesija
            LEFT JOIN REZULTAT_UPITNIKA ru ON s.id_sesija = ru.id_sesija
            LEFT JOIN UPOTREBA_RESURSA ur ON s.id_sesija = ur.id_sesija
            LEFT JOIN UPOTREBA_ALATA ua ON s.id_sesija = ua.id_sesija
            WHERE iz.id_izvodjenje = ?
            GROUP BY iz.id_izvodjenje, e.naziv, l.naziv, iz.datum, iz.status
            """;

    private ExperimentQueries() {
    }
}
