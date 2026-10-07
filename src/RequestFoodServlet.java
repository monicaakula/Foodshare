import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/requestFood")
public class RequestFoodServlet extends HttpServlet {


protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("text/html;charset=UTF-8");

    String foodIdParameter = request.getParameter("food_id");

    if (foodIdParameter == null) {
        response.getWriter().println(
            "<h2>Food ID is missing.</h2>"
        );
        return;
    }

    int foodId;

    try {
        foodId = Integer.parseInt(foodIdParameter);
    } catch (NumberFormatException e) {
        response.getWriter().println(
            "<h2>Invalid Food ID.</h2>"
        );
        return;
    }

    // Temporary receiver ID for testing
    int receiverId = 2;

    String foodName = "";
    int availableQuantity = 0;

    String selectSql =
        "SELECT food_name, quantity " +
        "FROM food_items " +
        "WHERE food_id = ? AND status = 'AVAILABLE'";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
             connection.prepareStatement(selectSql)) {

        statement.setInt(1, foodId);

        ResultSet result = statement.executeQuery();

        if (!result.next()) {

            response.getWriter().println(
                "<h2>Food is no longer available.</h2>"
            );

            return;
        }

        foodName = result.getString("food_name");
        availableQuantity = result.getInt("quantity");

    } catch (Exception e) {

        e.printStackTrace();

        response.getWriter().println(
            "<h2>Error loading food details.</h2>"
        );

        return;
    }

    String quantityParameter =
        request.getParameter("quantity");

    /*
     * Show request form
     */
    if (quantityParameter == null) {

        response.getWriter().println(

            "<!DOCTYPE html>" +
            "<html>" +

            "<head>" +
            "<meta charset='UTF-8'>" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
            "<title>FoodShare - Request Food</title>" +

            "<style>" +

            "*{" +
            "margin:0;" +
            "padding:0;" +
            "box-sizing:border-box;" +
            "font-family:Arial,sans-serif;" +
            "}" +

            "body{" +
            "min-height:100vh;" +
            "background:linear-gradient(135deg,#fff8e1,#f8fff8);" +
            "display:flex;" +
            "justify-content:center;" +
            "align-items:center;" +
            "padding:25px;" +
            "}" +

            ".card{" +
            "width:500px;" +
            "background:white;" +
            "padding:40px;" +
            "border-radius:20px;" +
            "box-shadow:0 10px 30px rgba(0,0,0,.10);" +
            "}" +

            ".logo{" +
            "text-align:center;" +
            "font-size:48px;" +
            "margin-bottom:8px;" +
            "}" +

            "h1{" +
            "text-align:center;" +
            "color:#2e7d32;" +
            "margin-bottom:8px;" +
            "}" +

            ".subtitle{" +
            "text-align:center;" +
            "color:#777;" +
            "margin-bottom:28px;" +
            "}" +

            ".food-box{" +
            "background:#f7faf7;" +
            "padding:22px;" +
            "border-radius:14px;" +
            "margin-bottom:25px;" +
            "}" +

            ".food-box h2{" +
            "color:#2e7d32;" +
            "margin-bottom:10px;" +
            "}" +

            ".available{" +
            "font-weight:bold;" +
            "color:#555;" +
            "}" +

            "label{" +
            "display:block;" +
            "font-weight:bold;" +
            "margin-bottom:8px;" +
            "color:#444;" +
            "}" +

            "input{" +
            "width:100%;" +
            "padding:13px;" +
            "border:1px solid #ccc;" +
            "border-radius:9px;" +
            "font-size:15px;" +
            "}" +

            ".submit{" +
            "width:100%;" +
            "padding:14px;" +
            "margin-top:20px;" +
            "border:none;" +
            "border-radius:9px;" +
            "background:#2e7d32;" +
            "color:white;" +
            "font-size:16px;" +
            "font-weight:bold;" +
            "cursor:pointer;" +
            "}" +

            ".back{" +
            "display:block;" +
            "text-align:center;" +
            "margin-top:15px;" +
            "padding:12px;" +
            "border:1px solid #2e7d32;" +
            "border-radius:9px;" +
            "color:#2e7d32;" +
            "text-decoration:none;" +
            "font-weight:bold;" +
            "}" +

            "</style>" +
            "</head>" +

            "<body>" +

            "<div class='card'>" +

            "<div class='logo'>&#127860;</div>" +

            "<h1>Request Food</h1>" +

            "<p class='subtitle'>" +
            "Choose how much food you need." +
            "</p>" +

            "<div class='food-box'>" +

            "<h2>" +
            foodName +
            "</h2>" +

            "<p class='available'>" +
            "Available Quantity: " +
            availableQuantity +
            "</p>" +

            "</div>" +

            "<form action='requestFood' method='get'>" +

            "<input type='hidden' name='food_id' value='" +
            foodId +
            "'>" +

            "<label for='quantity'>" +
            "Quantity Required" +
            "</label>" +

            "<input " +
            "type='number' " +
            "id='quantity' " +
            "name='quantity' " +
            "min='1' " +
            "max='" +
            availableQuantity +
            "' " +
            "required>" +

            "<button class='submit' type='submit'>" +
            "Submit Request" +
            "</button>" +

            "</form>" +

            "<a class='back' href='availableFood'>" +
            "Back to Available Food" +
            "</a>" +

            "</div>" +

            "</body>" +
            "</html>"
        );

        return;
    }

    int requestedQuantity;

    try {

        requestedQuantity =
            Integer.parseInt(quantityParameter);

    } catch (NumberFormatException e) {

        response.getWriter().println(
            "<h2>Invalid quantity.</h2>"
        );

        return;
    }

    if (requestedQuantity <= 0 ||
        requestedQuantity > availableQuantity) {

        response.getWriter().println(
            "<h2>Invalid quantity requested.</h2>" +
            "<p>Please request between 1 and " +
            availableQuantity +
            ".</p>"
        );

        return;
    }

    /*
     * IMPORTANT:
     *
     * We ONLY create the request here.
     *
     * We DO NOT reduce food quantity here.
     *
     * Quantity will be reduced when donor ACCEPTS.
     */

    String insertSql =
        "INSERT INTO food_requests " +
        "(food_id, receiver_id, quantity) " +
        "VALUES (?, ?, ?)";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
             connection.prepareStatement(insertSql)) {

        statement.setInt(1, foodId);
        statement.setInt(2, receiverId);
        statement.setInt(3, requestedQuantity);

        int result =
            statement.executeUpdate();

        if (result > 0) {

            response.getWriter().println(

                "<!DOCTYPE html>" +
                "<html>" +

                "<head>" +

                "<meta charset='UTF-8'>" +

                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +

                "<title>FoodShare - Request Submitted</title>" +

                "<style>" +

                "*{" +
                "margin:0;" +
                "padding:0;" +
                "box-sizing:border-box;" +
                "font-family:Arial,sans-serif;" +
                "}" +

                "body{" +
                "min-height:100vh;" +
                "background:linear-gradient(135deg,#e8f5e9,#f8fff8);" +
                "display:flex;" +
                "justify-content:center;" +
                "align-items:center;" +
                "padding:25px;" +
                "}" +

                ".card{" +
                "width:520px;" +
                "background:white;" +
                "padding:45px;" +
                "border-radius:22px;" +
                "text-align:center;" +
                "box-shadow:0 10px 30px rgba(0,0,0,.10);" +
                "}" +

                ".icon{" +
                "font-size:65px;" +
                "margin-bottom:15px;" +
                "}" +

                "h1{" +
                "color:#2e7d32;" +
                "font-size:30px;" +
                "margin-bottom:12px;" +
                "}" +

                ".message{" +
                "color:#666;" +
                "line-height:1.6;" +
                "margin-bottom:25px;" +
                "}" +

                ".details{" +
                "background:#f7faf7;" +
                "padding:20px;" +
                "border-radius:12px;" +
                "text-align:left;" +
                "margin-bottom:25px;" +
                "}" +

                ".detail{" +
                "padding:8px 0;" +
                "}" +

                ".button{" +
                "display:block;" +
                "width:100%;" +
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

                "<h1>Food Request Submitted!</h1>" +

                "<p class='message'>" +
                "Your request has been sent to the donor." +
                "</p>" +

                "<div class='details'>" +

                "<div class='detail'>" +
                "<b>Food:</b> " +
                foodName +
                "</div>" +

                "<div class='detail'>" +
                "<b>Quantity Requested:</b> " +
                requestedQuantity +
                "</div>" +

                "<div class='detail'>" +
                "<b>Request Status:</b> PENDING" +
                "</div>" +

                "<div class='detail'>" +
                "<b>Available Quantity:</b> " +
                availableQuantity +
                "</div>" +

                "</div>" +

                "<a class='button' href='receiver-dashboard.html'>" +
                "Back to Receiver Dashboard" +
                "</a>" +

                "</div>" +

                "</body>" +
                "</html>"
            );

        } else {

            response.getWriter().println(
                "<h2>Request failed.</h2>"
            );
        }

    } catch (Exception e) {

        e.printStackTrace();

        response.getWriter().println(
            "<h2>Error submitting request.</h2>"
        );
    }
}


}

