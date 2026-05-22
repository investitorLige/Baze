package lab.features.auth.queries;

public final class AuthQueries {
    public static final String FIND_RESEARCHER_BY_USERNAME = """
            SELECT
                id_istrazivac,
                ime,
                prezime,
                datum_rodjenja,
                email,
                tel,
                korisnicko_ime,
                titula,
                specijalizacija,
                godine_iskustva,
                institucija
            FROM ISTRAZIVAC
            WHERE korisnicko_ime = ?
            """;

    public static final String REGISTER_RESEARCHER = """
            INSERT INTO ISTRAZIVAC (
                ime,
                prezime,
                datum_rodjenja,
                email,
                tel,
                korisnicko_ime,
                titula,
                specijalizacija,
                godine_iskustva,
                institucija
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private AuthQueries() {
    }
}
