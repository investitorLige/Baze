package lab.features.auth.queries;

public final class AuthQueries {
    public static final String CREATE_USERS_TABLE = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                first_name VARCHAR(100) NOT NULL,
                last_name VARCHAR(100) NOT NULL,
                username VARCHAR(100) NOT NULL UNIQUE,
                user_role VARCHAR(50) NOT NULL,
                password VARCHAR(100) NOT NULL
            )
            """;

    public static final String LOGIN_USER = """
            SELECT id
            FROM users
            WHERE username = ? AND password = ?
            """;

    public static final String REGISTER_USER = """
            INSERT INTO users (first_name, last_name, username, user_role, password)
            VALUES (?, ?, ?, ?, ?)
            """;

    private AuthQueries() {
    }
}
