package lab.features.sessions.queries;

public final class SessionQueries {
    public static final String FIND_SESSIONS_BY_EXECUTION = """
            SELECT id_sesija, datum, pocetak, zavrsetak, status, COALESCE(napomena, '') AS napomena
            FROM SESIJA
            WHERE id_izvodjenje = ?
            ORDER BY datum, pocetak
            """;

    public static final String FIND_PARTICIPATION_BY_SESSION = """
            SELECT
                isp.id_ispitanik,
                CONCAT(isp.ime, ' ', isp.prezime) AS ispitanik,
                isp.pol,
                isp.obrazovanje,
                u.prisutan,
                u.ostvario_nagradu
            FROM UCESCE u
            JOIN ISPITANIK isp ON u.id_ispitanik = isp.id_ispitanik
            WHERE u.id_sesija = ?
            ORDER BY isp.prezime, isp.ime
            """;

    public static final String FIND_RESULTS_BY_SESSION = """
            SELECT
                CONCAT(isp.ime, ' ', isp.prezime) AS ispitanik,
                up.naziv AS upitnik,
                ru.rezultat,
                ru.datum_vreme,
                COALESCE(ru.napomena, '') AS napomena
            FROM REZULTAT_UPITNIKA ru
            JOIN ISPITANIK isp ON ru.id_ispitanik = isp.id_ispitanik
            JOIN UPITNIK up ON ru.id_upitnik = up.id_upitnik
            WHERE ru.id_sesija = ?
            ORDER BY isp.prezime, isp.ime, up.naziv
            """;

    public static final String FIND_USED_RESOURCES_BY_SESSION = """
            SELECT
                r.id_resursa,
                r.naziv,
                ur.kolicina_iskoriscenog,
                r.jedinica_mere
            FROM UPOTREBA_RESURSA ur
            JOIN RESURS r ON ur.id_resursa = r.id_resursa
            WHERE ur.id_sesija = ?
            ORDER BY r.naziv
            """;

    public static final String FIND_USED_TOOLS_BY_SESSION = """
            SELECT
                a.id_alat,
                ta.naziv AS tip_alata,
                ua.ispravan,
                COALESCE(ua.napomena, '') AS napomena
            FROM UPOTREBA_ALATA ua
            JOIN ALAT a ON ua.id_alat = a.id_alat
            JOIN TIP_ALATA ta ON a.id_tip = ta.id_tip
            WHERE ua.id_sesija = ?
            ORDER BY ta.naziv, a.id_alat
            """;

    public static final String UPDATE_SESSION_STATUS = """
            UPDATE SESIJA s
            SET s.status = ?
            WHERE s.id_sesija = ?
              AND EXISTS (
                  SELECT 1
                  FROM TIM_IZVODJACA ti
                  WHERE ti.id_izvodjenje = s.id_izvodjenje
                    AND ti.id_istrazivac = ?
              )
            """;

    public static final String CAN_DELETE_SESSION = """
            SELECT COUNT(*) AS dozvoljeno
            FROM SESIJA s
            JOIN TIM_IZVODJACA ti ON s.id_izvodjenje = ti.id_izvodjenje
            WHERE s.id_sesija = ?
              AND ti.id_istrazivac = ?
            """;

    public static final String DELETE_SESSION = """
            DELETE FROM SESIJA
            WHERE id_sesija = ?
            """;

    private SessionQueries() {
    }
}
