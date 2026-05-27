package lab.features.luka.queries;

public final class LukaQueries {
    public static final String RESEARCHER_LOAD_REPORT = """
            SELECT
                report.id_istrazivac,
                report.ime,
                report.prezime,
                report.institucija,
                ROUND(
                    report.broj_izvodjenja * 3
                    + report.zakazane_sesije * 2
                    + report.zavrsene_sesije
                    + report.broj_prisutnih * 0.5
                    + report.broj_rezultata * 0.25,
                    2
                ) AS indeks_opterecenja,
                report.broj_izvodjenja,
                report.zakazane_sesije,
                report.zavrsene_sesije,
                report.broj_prisutnih,
                report.broj_rezultata
            FROM (
                SELECT
                    i.id_istrazivac,
                    i.ime,
                    i.prezime,
                    COALESCE(i.institucija, '') AS institucija,
                    COUNT(DISTINCT ti.id_izvodjenje) AS broj_izvodjenja,
                    COUNT(DISTINCT CASE
                        WHEN s.status IN ('zakazana', 'u_toku') THEN s.id_sesija
                    END) AS zakazane_sesije,
                    COUNT(DISTINCT CASE
                        WHEN s.status = 'zavrsena' THEN s.id_sesija
                    END) AS zavrsene_sesije,
                    COUNT(DISTINCT CASE
                        WHEN u.prisutan = TRUE THEN CONCAT(u.id_ispitanik, '-', u.id_sesija)
                    END) AS broj_prisutnih,
                    COUNT(DISTINCT CONCAT(ru.id_ispitanik, '-', ru.id_sesija, '-', ru.id_upitnik)) AS broj_rezultata
                FROM ISTRAZIVAC i
                JOIN TIM_IZVODJACA ti ON i.id_istrazivac = ti.id_istrazivac
                JOIN IZVODJENJE iz ON ti.id_izvodjenje = iz.id_izvodjenje
                LEFT JOIN SESIJA s ON iz.id_izvodjenje = s.id_izvodjenje
                LEFT JOIN UCESCE u ON s.id_sesija = u.id_sesija
                LEFT JOIN REZULTAT_UPITNIKA ru ON s.id_sesija = ru.id_sesija
                GROUP BY i.id_istrazivac, i.ime, i.prezime, i.institucija
                HAVING COUNT(DISTINCT ti.id_izvodjenje) > 0
            ) report
            ORDER BY
                indeks_opterecenja DESC,
                report.prezime,
                report.ime
            """;

    private LukaQueries() {
    }
}
