package lab.features.auth.models;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

final class UserCredentialsFile {
    private static final Path USERS_FILE = Path.of("src", "main", "resources", "users.txt");

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
}
