import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/foodshare";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

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

    public static void main(String[] args) {
        getConnection();
    }
}