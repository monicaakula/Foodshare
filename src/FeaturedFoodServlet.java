import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/featuredFood")
public class FeaturedFoodServlet extends HttpServlet {

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "")
                .replace("\n", " ");
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        String sql =
            "SELECT food_id, food_name, quantity, location, " +
            "donation_type, available_until " +
            "FROM food_items " +
            "WHERE status = 'AVAILABLE' " +
            "ORDER BY available_until ASC, created_at DESC " +
            "LIMIT 3";

        StringBuilder json = new StringBuilder();

        json.append("[");

        boolean first = true;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;

                int foodId =
                    result.getInt("food_id");

                String foodName =
                    escapeJson(result.getString("food_name"));

                int quantity =
                    result.getInt("quantity");

                String location =
                    escapeJson(result.getString("location"));

                String donationType =
                    escapeJson(result.getString("donation_type"));

                String availableUntil =
                    escapeJson(result.getString("available_until"));

                json.append("{");

                json.append("\"food_id\":")
                    .append(foodId)
                    .append(",");

                json.append("\"food_name\":\"")
                    .append(foodName)
                    .append("\",");

                json.append("\"quantity\":")
                    .append(quantity)
                    .append(",");

                json.append("\"location\":\"")
                    .append(location)
                    .append("\",");

                json.append("\"donation_type\":\"")
                    .append(donationType)
                    .append("\",");

                json.append("\"available_until\":\"")
                    .append(availableUntil)
                    .append("\"");

                json.append("}");
            }

            json.append("]");

            response.getWriter().println(
                json.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println("[]");
        }
    }
}
