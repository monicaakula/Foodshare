import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/foodStats")
public class FoodStatsServlet extends HttpServlet {


protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/json;charset=UTF-8");

    int availableDonations = 0;
    int completedDeliveries = 0;
    int peopleHelped = 0;

    String availableSql =
        "SELECT COUNT(*) AS total " +
        "FROM food_items " +
        "WHERE status = 'AVAILABLE'";

    String completedSql =
        "SELECT COUNT(*) AS total " +
        "FROM pickups " +
        "WHERE pickup_status = 'DELIVERED'";

    String peopleHelpedSql =
        "SELECT COUNT(DISTINCT request_id) AS total " +
        "FROM pickups " +
        "WHERE pickup_status = 'DELIVERED'";

    try (Connection connection = DBConnection.getConnection()) {

        try (PreparedStatement statement =
                 connection.prepareStatement(availableSql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                availableDonations =
                    result.getInt("total");
            }
        }

        try (PreparedStatement statement =
                 connection.prepareStatement(completedSql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                completedDeliveries =
                    result.getInt("total");
            }
        }

        try (PreparedStatement statement =
                 connection.prepareStatement(peopleHelpedSql);
             ResultSet result = statement.executeQuery()) {

            if (result.next()) {
                peopleHelped =
                    result.getInt("total");
            }
        }

        response.getWriter().println(
            "{"
            + "\"available\":" + availableDonations + ","
            + "\"completed\":" + completedDeliveries + ","
            + "\"helped\":" + peopleHelped
            + "}"
        );

    } catch (Exception e) {

        e.printStackTrace();

        response.getWriter().println(
            "{"
            + "\"available\":0,"
            + "\"completed\":0,"
            + "\"helped\":0"
            + "}"
        );
    }
}


}
