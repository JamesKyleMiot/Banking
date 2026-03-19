import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jameskylebank?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public DatabaseManager() {
        ensureDriverPresent();
        ensureSchema();
    }

    public List<Bank> loadAccounts() {
        List<Bank> accounts = new ArrayList<>();
        String sql = "SELECT id, account_holder, age, address, gmail, telephone, username, pin, checking_balance, savings_balance, loan_amount FROM accounts ORDER BY id";

        try (Connection conn = openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bank account = new Bank(
                    rs.getInt("id"),
                    rs.getString("account_holder"),
                    rs.getInt("age"),
                    rs.getString("address"),
                    rs.getString("gmail"),
                    rs.getString("telephone"),
                    rs.getString("username"),
                    rs.getDouble("checking_balance"),
                    rs.getDouble("savings_balance"),
                    rs.getDouble("loan_amount"),
                    rs.getInt("pin")
                );
                accounts.add(account);
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot load accounts from MySQL. " + ex.getMessage(), ex);
        }

        return accounts;
    }

    public int insertAccount(Bank account) {
        String sql = "INSERT INTO accounts (account_holder, age, address, gmail, telephone, username, pin, checking_balance, savings_balance, loan_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bindAccountFields(stmt, account);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new IllegalStateException("Account insert succeeded but no generated ID was returned.");
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot insert account into MySQL. " + ex.getMessage(), ex);
        }
    }

    public void updateAccountFinancials(Bank account) {
        String sql = "UPDATE accounts SET checking_balance = ?, savings_balance = ?, loan_amount = ? WHERE id = ?";

        try (Connection conn = openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, account.getBalance());
            stmt.setDouble(2, account.getSavingsBalance());
            stmt.setDouble(3, account.getLoanAmount());
            stmt.setInt(4, account.getAccountId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot update account balances in MySQL. " + ex.getMessage(), ex);
        }
    }

    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM accounts WHERE id = ?";

        try (Connection conn = openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot delete account from MySQL. " + ex.getMessage(), ex);
        }
    }

    private void bindAccountFields(PreparedStatement stmt, Bank account) throws SQLException {
        stmt.setString(1, account.getAccountHolder());
        stmt.setInt(2, account.getAge());
        stmt.setString(3, account.getAddress());
        stmt.setString(4, account.getGmail());
        stmt.setString(5, account.getTelephone());
        stmt.setString(6, account.getAccountUsername());
        stmt.setInt(7, account.getPin());
        stmt.setDouble(8, account.getBalance());
        stmt.setDouble(9, account.getSavingsBalance());
        stmt.setDouble(10, account.getLoanAmount());
    }

    private void ensureSchema() {
        String createTableSql =
            "CREATE TABLE IF NOT EXISTS accounts (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "account_holder VARCHAR(120) NOT NULL," +
            "age INT NOT NULL," +
            "address VARCHAR(255) NOT NULL," +
            "gmail VARCHAR(180) NOT NULL," +
            "telephone VARCHAR(20) NOT NULL," +
            "username VARCHAR(80) NOT NULL UNIQUE," +
            "pin INT NOT NULL," +
            "checking_balance DOUBLE NOT NULL DEFAULT 0," +
            "savings_balance DOUBLE NOT NULL DEFAULT 0," +
            "loan_amount DOUBLE NOT NULL DEFAULT 0" +
            ")";

        try (Connection conn = openConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSql);
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to initialize MySQL schema. Check if XAMPP MySQL is running and database 'jameskylebank' exists. " + ex.getMessage(), ex);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void ensureDriverPresent() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                "MySQL JDBC driver not found. Add mysql-connector-j to your project classpath.",
                ex
            );
        }
    }
}
