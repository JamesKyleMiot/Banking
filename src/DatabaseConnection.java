import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final String DB_HOST = resolveString("bank.db.host", "BANK_DB_HOST", "localhost");
    private static final int DB_PORT = resolvePort("bank.db.port", "BANK_DB_PORT", 3306);
    // You can change this in NetBeans VM options using: -Dbank.db.name=jameskylebank
    private static final String DB_NAME = resolveString("bank.db.name", "BANK_DB_NAME", "jameskylebank");
    private static final String DB_USER = resolveString("bank.db.user", "BANK_DB_USER", "root");
    private static final String DB_PASSWORD = resolveString("bank.db.password", "BANK_DB_PASSWORD", "");
    private static final String SERVER_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/?useSSL=false&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        ensureDatabaseExists();
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static String getDatabaseName() {
        return DB_NAME;
    }

    public static String getDatabaseHost() {
        return DB_HOST;
    }

    public static int getDatabasePort() {
        return DB_PORT;
    }

    public static String getDatabaseUser() {
        return DB_USER;
    }

    private static String resolveString(String propertyKey, String envKey, String fallback) {
        String fromProperty = System.getProperty(propertyKey);
        if (fromProperty != null && !fromProperty.trim().isEmpty()) {
            return fromProperty.trim();
        }

        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.trim().isEmpty()) {
            return fromEnv.trim();
        }

        return fallback;
    }

    private static int resolvePort(String propertyKey, String envKey, int fallback) {
        String candidate = System.getProperty(propertyKey);
        if (candidate == null || candidate.trim().isEmpty()) {
            candidate = System.getenv(envKey);
        }

        if (candidate == null || candidate.trim().isEmpty()) {
            return fallback;
        }

        try {
            return Integer.parseInt(candidate.trim());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    public static void ensureMySqlDriverLoaded() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                "MySQL JDBC driver not found. Add mysql-connector-j-9.6.0.jar to project libraries.",
                ex
            );
        }
    }

    private static void ensureDatabaseExists() throws SQLException {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "`");
        }
    }
}
