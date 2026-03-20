import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectMysqlconnectortoxampp {
	private ConnectMysqlconnectortoxampp() {
		// Utility class
	}

	public static Connection getConnection() throws SQLException {
		DatabaseConnection.ensureMySqlDriverLoaded();
		return DatabaseConnection.getConnection();
	}

	public static boolean testConnection() {
		try (Connection connection = getConnection()) {
			return connection != null && !connection.isClosed();
		} catch (SQLException exception) {
			System.out.println(
				"MySQL connection failed to database '" + DatabaseConnection.getDatabaseName() + "': " + exception.getMessage()
			);
			return false;
		}
	}
}
