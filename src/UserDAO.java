import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDAO {

    public boolean registerUser(User user) {

        String sql = "INSERT INTO users " +
                     "(name, email, password, role, phone, location) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getPhone());
            statement.setString(6, user.getLocation());

            int result = statement.executeUpdate();

            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}