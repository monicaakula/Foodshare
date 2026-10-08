import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }

        return connection;
    }
}