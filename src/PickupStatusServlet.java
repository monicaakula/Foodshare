import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/pickupStatus")
public class PickupStatusServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String requestIdParameter = request.getParameter("request_id");
        String status = request.getParameter("status");

        if (requestIdParameter == null || status == null) {
            response.getWriter().println(
                "<h2>Pickup details are missing.</h2>"
            );
            return;
        }

        int requestId;

        try {
            requestId = Integer.parseInt(requestIdParameter);
        } catch (NumberFormatException e) {
            response.getWriter().println(
                "<h2>Invalid Request ID.</h2>"
            );
            return;
        }

        if (!status.equals("PICKED_UP") &&
            !status.equals("DELIVERED")) {

            response.getWriter().println(
                "<h2>Invalid pickup status.</h2>"
            );
            return;
        }

        String sql =
            "UPDATE pickups " +
            "SET pickup_status = ?, " +
            "pickup_time = CASE " +
            "WHEN ? = 'PICKED_UP' THEN NOW() " +
            "ELSE pickup_time END, " +
            "delivery_time = CASE " +
            "WHEN ? = 'DELIVERED' THEN NOW() " +
            "ELSE delivery_time END " +
            "WHERE request_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setString(2, status);
            statement.setString(3, status);
            statement.setInt(4, requestId);

            int result = statement.executeUpdate();

            if (result > 0) {

                response.getWriter().println(
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head><title>Pickup Status Updated</title></head>" +
                    "<body>" +

                    "<h1>🍱 Food Share</h1>" +

                    "<h2>Pickup Status Updated Successfully! 🎉</h2>" +

                    "<p>Request #" + requestId +
                    "</p>" +

                    "<p>Status: <b>" + status + "</b></p>" +

                    "<br>" +

                    "<a href='volunteerPickups'>" +
                    "<button type='button'>" +
                    "Back to Pickup Requests" +
                    "</button>" +
                    "</a>" +

                    "</body>" +
                    "</html>"
                );

            } else {

                response.getWriter().println(
                    "<h2>Pickup status could not be updated.</h2>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Error updating pickup status.</h2>"
            );
        }
    }
}


