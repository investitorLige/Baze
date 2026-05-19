package lab.features.auth.queries;

public final class AuthQueries {
    public static final String CREATE_RESEARCHERS_TABLE = """
            CREATE TABLE IF NOT EXISTS ISTRAZIVAC (
                id_istrazivac INT PRIMARY KEY AUTO_INCREMENT,
                ime VARCHAR(100) NOT NULL,
                prezime VARCHAR(100) NOT NULL,
                datum_rodjenja DATE NOT NULL,
                email VARCHAR(150) NOT NULL,
                tel VARCHAR(50) NULL,
                korisnicko_ime VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(100) NOT NULL,
                titula VARCHAR(100) NULL,
                specijalizacija VARCHAR(255) NULL,
                godine_iskustva INT NULL,
                institucija VARCHAR(255) NULL
            )
            """;

    public static final String LOGIN_RESEARCHER = """
            SELECT id_istrazivac
            FROM ISTRAZIVAC
            WHERE korisnicko_ime = ? AND password = ?
            """;

    public static final String REGISTER_RESEARCHER = """
            INSERT INTO ISTRAZIVAC (
                ime,
                prezime,
                datum_rodjenja,
                email,
                tel,
                korisnicko_ime,
                password,
                titula,
                specijalizacija,
                godine_iskustva,
                institucija
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private AuthQueries() {
    }
}
