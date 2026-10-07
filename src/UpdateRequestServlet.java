import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/updateRequest")
public class UpdateRequestServlet extends HttpServlet {


protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");

    String requestIdParameter =
        request.getParameter("request_id");

    String action =
        request.getParameter("action");

    if (requestIdParameter == null || action == null) {

        response.getWriter().println(
            "<h2>Request details are missing.</h2>"
        );

        return;
    }

    int requestId;

    try {

        requestId =
            Integer.parseInt(requestIdParameter);

    } catch (NumberFormatException e) {

        response.getWriter().println(
            "<h2>Invalid request ID.</h2>"
        );

        return;
    }

    if (!"ACCEPTED".equals(action) &&
        !"REJECTED".equals(action)) {

        response.getWriter().println(
            "<h2>Invalid action.</h2>"
        );

        return;
    }

    Connection connection = null;

    try {

        connection = DBConnection.getConnection();

        connection.setAutoCommit(false);

        /*
         * Get request details.
         */
        String requestSql =
            "SELECT food_id, quantity, status " +
            "FROM food_requests " +
            "WHERE request_id = ? " +
            "FOR UPDATE";

        int foodId;
        int requestedQuantity;
        String currentStatus;

        try (PreparedStatement statement =
                 connection.prepareStatement(requestSql)) {

            statement.setInt(1, requestId);

            ResultSet result =
                statement.executeQuery();

            if (!result.next()) {

                connection.rollback();

                response.getWriter().println(
                    "<h2>Request not found.</h2>"
                );

                return;
            }

            foodId =
                result.getInt("food_id");

            requestedQuantity =
                result.getInt("quantity");

            currentStatus =
                result.getString("status");
        }

        /*
         * Only PENDING requests can be processed.
         */
        if (!"PENDING".equals(currentStatus)) {

            connection.rollback();

            response.getWriter().println(
                "<h2>Request already processed.</h2>" +
                "<p>Current Status: " +
                currentStatus +
                "</p>"
            );

            return;
        }

        /*
         * ACCEPT
         */
        if ("ACCEPTED".equals(action)) {

            String foodSql =
                "SELECT quantity, status " +
                "FROM food_items " +
                "WHERE food_id = ? " +
                "FOR UPDATE";

            int currentFoodQuantity;
            String foodStatus;

            try (PreparedStatement statement =
                     connection.prepareStatement(foodSql)) {

                statement.setInt(1, foodId);

                ResultSet result =
                    statement.executeQuery();

                if (!result.next()) {

                    connection.rollback();

                    response.getWriter().println(
                        "<h2>Food item not found.</h2>"
                    );

                    return;
                }

                currentFoodQuantity =
                    result.getInt("quantity");

                foodStatus =
                    result.getString("status");
            }

            /*
             * Make sure enough food is still available.
             */
            if (!"AVAILABLE".equals(foodStatus) ||
                currentFoodQuantity < requestedQuantity) {

                connection.rollback();

                response.getWriter().println(

                    "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>FoodShare - Not Enough Food</title>" +

                    "<style>" +

                    "body{" +
                    "margin:0;" +
                    "min-height:100vh;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "background:#f4f8f4;" +
                    "font-family:Arial,sans-serif;" +
                    "}" +

                    ".card{" +
                    "background:white;" +
                    "padding:40px;" +
                    "border-radius:20px;" +
                    "text-align:center;" +
                    "box-shadow:0 10px 30px rgba(0,0,0,.1);" +
                    "max-width:500px;" +
                    "width:90%;" +
                    "}" +

                    "h1{" +
                    "color:#c62828;" +
                    "}" +

                    ".button{" +
                    "display:inline-block;" +
                    "margin-top:20px;" +
                    "padding:12px 22px;" +
                    "background:#2e7d32;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "border-radius:9px;" +
                    "font-weight:bold;" +
                    "}" +

                    "</style>" +
                    "</head>" +

                    "<body>" +

                    "<div class='card'>" +

                    "<h1>Not Enough Food Available</h1>" +

                    "<p>" +
                    "Requested Quantity: " +
                    requestedQuantity +
                    "</p>" +

                    "<p>" +
                    "Currently Available: " +
                    currentFoodQuantity +
                    "</p>" +

                    "<p>" +
                    "The request remains PENDING." +
                    "</p>" +

                    "<a class='button' href='donationRequests'>" +
                    "Back to Donation Requests" +
                    "</a>" +

                    "</div>" +

                    "</body>" +
                    "</html>"
                );

                return;
            }

            /*
             * This is where the quantity is reduced.
             *
             * Example:
             * 5 - 4 = 1
             */
            int remainingQuantity =
                currentFoodQuantity -
                requestedQuantity;

            String updateFoodSql;

            if (remainingQuantity == 0) {

                updateFoodSql =
                    "UPDATE food_items " +
                    "SET quantity = 0, status = 'UNAVAILABLE' " +
                    "WHERE food_id = ?";

            } else {

                updateFoodSql =
                    "UPDATE food_items " +
                    "SET quantity = ?, status = 'AVAILABLE' " +
                    "WHERE food_id = ?";
            }

            try (PreparedStatement statement =
                     connection.prepareStatement(updateFoodSql)) {

                if (remainingQuantity == 0) {

                    statement.setInt(1, foodId);

                } else {

                    statement.setInt(1, remainingQuantity);
                    statement.setInt(2, foodId);
                }

                statement.executeUpdate();
            }

            /*
             * Mark request as accepted.
             */
            String acceptSql =
                "UPDATE food_requests " +
                "SET status = 'ACCEPTED' " +
                "WHERE request_id = ?";

            try (PreparedStatement statement =
                     connection.prepareStatement(acceptSql)) {

                statement.setInt(1, requestId);
                statement.executeUpdate();
            }

        }

        /*
         * REJECT
         *
         * Do NOT change food quantity.
         */
        else {

            String rejectSql =
                "UPDATE food_requests " +
                "SET status = 'REJECTED' " +
                "WHERE request_id = ?";

            try (PreparedStatement statement =
                     connection.prepareStatement(rejectSql)) {

                statement.setInt(1, requestId);
                statement.executeUpdate();
            }
        }

        connection.commit();

        response.getWriter().println(

            "<!DOCTYPE html>" +
            "<html>" +

            "<head>" +

            "<meta charset='UTF-8'>" +

            "<meta name='viewport' " +
            "content='width=device-width, initial-scale=1.0'>" +

            "<title>FoodShare - Request Updated</title>" +

            "<style>" +

            "body{" +
            "margin:0;" +
            "min-height:100vh;" +
            "display:flex;" +
            "align-items:center;" +
            "justify-content:center;" +
            "background:linear-gradient(135deg,#e8f5e9,#f8fff8);" +
            "font-family:Arial,sans-serif;" +
            "}" +

            ".card{" +
            "background:white;" +
            "padding:45px;" +
            "border-radius:22px;" +
            "text-align:center;" +
            "box-shadow:0 10px 30px rgba(0,0,0,.1);" +
            "max-width:520px;" +
            "width:90%;" +
            "}" +

            ".icon{" +
            "font-size:60px;" +
            "margin-bottom:15px;" +
            "}" +

            "h1{" +
            "color:#2e7d32;" +
            "}" +

            "p{" +
            "color:#666;" +
            "line-height:1.6;" +
            "}" +

            ".status{" +
            "display:inline-block;" +
            "padding:8px 16px;" +
            "border-radius:20px;" +
            "background:#e8f5e9;" +
            "color:#1b5e20;" +
            "font-weight:bold;" +
            "margin:15px 0;" +
            "}" +

            ".button{" +
            "display:block;" +
            "margin-top:25px;" +
            "padding:13px;" +
            "background:#2e7d32;" +
            "color:white;" +
            "text-decoration:none;" +
            "border-radius:9px;" +
            "font-weight:bold;" +
            "}" +

            "</style>" +

            "</head>" +

            "<body>" +

            "<div class='card'>" +

            "<div class='icon'>&#9989;</div>" +

            "<h1>Request Updated Successfully!</h1>" +

            "<div class='status'>" +
            action +
            "</div>" +

            "<p>" +
            "Request #" +
            requestId +
            " has been processed." +
            "</p>" +

            "<a class='button' href='donationRequests'>" +
            "Back to Donation Requests" +
            "</a>" +

            "</div>" +

            "</body>" +
            "</html>"
        );

    } catch (Exception e) {

        e.printStackTrace();

        if (connection != null) {

            try {
                connection.rollback();
            } catch (Exception rollbackError) {
                rollbackError.printStackTrace();
            }
        }

        response.getWriter().println(
            "<h2>Error updating request.</h2>"
        );

    } finally {

        if (connection != null) {

            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (Exception closeError) {
                closeError.printStackTrace();
            }
        }
    }
}


}
