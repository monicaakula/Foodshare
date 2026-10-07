import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/takePickup")
public class TakePickupServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String requestIdParameter = request.getParameter("request_id");

        if (requestIdParameter == null) {
            response.getWriter().println(
                "<h2>Request ID is missing.</h2>"
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

        // Temporary volunteer ID for testing
        int volunteerId = 3;

        String sql =
            "INSERT INTO pickups " +
            "(request_id, volunteer_id, pickup_status) " +
            "VALUES (?, ?, 'ASSIGNED')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setInt(1, requestId);
            statement.setInt(2, volunteerId);

            int result = statement.executeUpdate();

            if (result > 0) {

                response.getWriter().println(
                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head><title>Pickup Assigned</title></head>" +
                    "<body>" +

                    "<h1>🍱 Food Share</h1>" +

                    "<h2>Pickup Assigned Successfully! 🎉</h2>" +

                    "<p>Request #" + requestId +
                    " has been assigned to you.</p>" +

                    "<p>Status: <b>ASSIGNED</b></p>" +

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
                    "<h2>Pickup assignment failed.</h2>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Error assigning pickup.</h2>"
            );
        }
    }
}

