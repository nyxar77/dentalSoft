package ma.dentalsoft.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {}

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                requiredSetting("DENTALSOFT_DB_URL"),
                requiredSetting("DENTALSOFT_DB_USER"),
                requiredSetting("DENTALSOFT_DB_PASSWORD"));
    }

    private static String requiredSetting(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing environment variable: " + name);
        }
        return value;
    }
}
