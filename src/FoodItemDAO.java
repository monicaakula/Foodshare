import java.sql.Connection;
import java.sql.PreparedStatement;

public class FoodItemDAO {

    public boolean addFood(FoodItem food) {

        String sql = "INSERT INTO food_items " +
                     "(donor_id, food_name, quantity, food_type, description, " +
                     "image_path, location, available_until, donation_type, price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, food.getDonorId());
            statement.setString(2, food.getFoodName());
            statement.setInt(3, food.getQuantity());
            statement.setString(4, food.getFoodType());
            statement.setString(5, food.getDescription());
            statement.setString(6, food.getImagePath());
            statement.setString(7, food.getLocation());

            statement.setString(8, food.getAvailableUntil());

            statement.setString(9, food.getDonationType());
            statement.setDouble(10, food.getPrice());

            int result = statement.executeUpdate();

            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}