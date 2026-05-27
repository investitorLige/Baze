package lab.features.auth.models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

final class UserCredentialsFile {
    private static final Path USERS_FILE = Path.of(envOrDefault(
            "USERS_FILE",
            Path.of("src", "main", "resources", "users.txt").toString()
    ));

    private UserCredentialsFile() {
    }

    static void append(String username, String password) throws IOException {
        Files.createDirectories(USERS_FILE.getParent());

        String line = username + ":" + password + System.lineSeparator();
        Files.writeString(
                USERS_FILE,
                line,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    static boolean contains(String username, String password) throws IOException {
        if (!Files.exists(USERS_FILE)) {
            return false;
        }

        String expectedLine = username + ":" + password;
        List<String> lines = Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8);
        return lines.stream().anyMatch(expectedLine::equals);
    }

    private static String envOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
