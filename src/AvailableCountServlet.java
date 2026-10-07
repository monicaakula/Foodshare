import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/availableCount")
public class AvailableCountServlet extends HttpServlet {


protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/plain;charset=UTF-8");

    String sql =
        "SELECT COUNT(*) AS total " +
        "FROM food_items " +
        "WHERE status = 'AVAILABLE'";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
             connection.prepareStatement(sql);
         ResultSet result = statement.executeQuery()) {

        if (result.next()) {

            int count = result.getInt("total");

            response.getWriter().println(count);

        } else {

            response.getWriter().println("0");
        }

    } catch (Exception e) {

        e.printStackTrace();

        response.getWriter().println("0");
    }
}


}
