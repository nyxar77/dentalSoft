package ma.dentalSoft.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Crée la connexion JDBC à partir des variables d'environnement ou de application.properties.
 */
public final class JdbcConnectionFactory {

    private JdbcConnectionFactory() {}

    public static Connection createConnection(Properties properties) {
        var driver = getRequiredSetting(properties, "dataSource.driver", null);
        var url = getRequiredSetting(properties, "dataSource.url", "DENTALSOFT_DB_URL");
        var user = getRequiredSetting(properties, "dataSource.user", "DENTALSOFT_DB_USER");
        var password = getRequiredSetting(properties, "dataSource.pass", "DENTALSOFT_DB_PASSWORD");

        try {
            Class.forName(driver);
            Connection session = DriverManager.getConnection(url, user, password);
            System.out.println("You are successfully connected to database ^_^ | " + url);
            return session;
        } catch (ClassNotFoundException e) {
            System.err.println("Pilote JDBC MySQL introuvable : " + driver + " => " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Impossible de se connecter à MySQL : " + url + " => " + e.getMessage());
        }

        return null;
    }

    private static String getRequiredSetting(Properties properties, String propertyKey, String envKey) {
        if (envKey != null) {
            var envValue = System.getenv(envKey);
            if (envValue != null && !envValue.isBlank()) {
                return envValue.trim();
            }
        }
        var value = properties.getProperty(propertyKey);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Propriété JDBC obligatoire absente : " + propertyKey);
        }
        return value.trim();
    }
}
