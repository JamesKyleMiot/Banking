import java.sql.Connection;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        try {
            DatabaseConnection.ensureMySqlDriverLoaded();
            try (Connection conn = DatabaseConnection.getConnection()) {
                System.out.println("Database connected successfully: " + conn.getCatalog());
            }
        } catch (Exception ex) {
            System.out.println("Database connection failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
