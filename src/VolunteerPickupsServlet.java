import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/volunteerPickups")
public class VolunteerPickupsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql =
            "SELECT fr.request_id, fi.food_name, fr.quantity, " +
            "u.name AS receiver_name, u.phone, u.location, " +
            "fr.status, " +
            "COALESCE(p.pickup_status, 'NOT_ASSIGNED') AS pickup_status " +
            "FROM food_requests fr " +
            "JOIN food_items fi ON fr.food_id = fi.food_id " +
            "JOIN users u ON fr.receiver_id = u.user_id " +
            "LEFT JOIN pickups p ON fr.request_id = p.request_id " +
            "WHERE fr.status = 'ACCEPTED' " +
            "ORDER BY fr.requested_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            StringBuilder html = new StringBuilder();

            html.append("<!DOCTYPE html>");
            html.append("<html>");
            html.append("<head>");
            html.append("<meta charset='UTF-8'>");
            html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            html.append("<title>FoodShare - Pickup Requests</title>");

            html.append("<style>");

            html.append("* {");
            html.append("margin:0;");
            html.append("padding:0;");
            html.append("box-sizing:border-box;");
            html.append("font-family:Arial,sans-serif;");
            html.append("}");

            html.append("body {");
            html.append("min-height:100vh;");
            html.append("background:linear-gradient(135deg,#e3f2fd,#f8fff8);");
            html.append("color:#222;");
            html.append("}");

            html.append(".header {");
            html.append("background:linear-gradient(135deg,#2e7d32,#43a047);");
            html.append("color:white;");
            html.append("padding:28px 8%;");
            html.append("display:flex;");
            html.append("justify-content:space-between;");
            html.append("align-items:center;");
            html.append("}");

            html.append(".brand {");
            html.append("font-size:28px;");
            html.append("font-weight:bold;");
            html.append("}");

            html.append(".role {");
            html.append("background:rgba(255,255,255,0.18);");
            html.append("padding:8px 15px;");
            html.append("border-radius:20px;");
            html.append("font-size:14px;");
            html.append("}");

            html.append(".container {");
            html.append("max-width:1000px;");
            html.append("margin:40px auto;");
            html.append("padding:0 25px;");
            html.append("}");

            html.append(".page-title {");
            html.append("text-align:center;");
            html.append("margin-bottom:35px;");
            html.append("}");

            html.append(".page-title h1 {");
            html.append("font-size:36px;");
            html.append("color:#2e7d32;");
            html.append("margin-bottom:10px;");
            html.append("}");

            html.append(".page-title p {");
            html.append("color:#666;");
            html.append("font-size:17px;");
            html.append("}");

            html.append(".request-card {");
            html.append("background:white;");
            html.append("padding:30px;");
            html.append("margin-bottom:25px;");
            html.append("border-radius:18px;");
            html.append("box-shadow:0 8px 25px rgba(0,0,0,0.09);");
            html.append("}");

            html.append(".request-header {");
            html.append("display:flex;");
            html.append("justify-content:space-between;");
            html.append("align-items:center;");
            html.append("margin-bottom:22px;");
            html.append("}");

            html.append(".request-header h2 {");
            html.append("color:#2e7d32;");
            html.append("font-size:24px;");
            html.append("}");

            html.append(".status {");
            html.append("padding:7px 14px;");
            html.append("border-radius:20px;");
            html.append("font-size:13px;");
            html.append("font-weight:bold;");
            html.append("}");

            html.append(".status.accepted {");
            html.append("background:#fff3cd;");
            html.append("color:#856404;");
            html.append("}");

            html.append(".status.assigned {");
            html.append("background:#e3f2fd;");
            html.append("color:#1565c0;");
            html.append("}");

            html.append(".status.picked {");
            html.append("background:#e8f5e9;");
            html.append("color:#2e7d32;");
            html.append("}");

            html.append(".status.delivered {");
            html.append("background:#e8f5e9;");
            html.append("color:#1b5e20;");
            html.append("}");

            html.append(".details {");
            html.append("display:grid;");
            html.append("grid-template-columns:1fr 1fr;");
            html.append("gap:14px;");
            html.append("margin-bottom:25px;");
            html.append("}");

            html.append(".detail {");
            html.append("background:#f7faf7;");
            html.append("padding:14px;");
            html.append("border-radius:10px;");
            html.append("}");

            html.append(".detail b {");
            html.append("color:#555;");
            html.append("}");

            html.append(".actions {");
            html.append("margin-top:10px;");
            html.append("}");

            html.append(".button {");
            html.append("display:inline-block;");
            html.append("text-decoration:none;");
            html.append("background:#43a047;");
            html.append("color:white;");
            html.append("padding:12px 22px;");
            html.append("border-radius:9px;");
            html.append("font-weight:bold;");
            html.append("margin-right:8px;");
            html.append("margin-top:8px;");
            html.append("}");

            html.append(".button:hover {");
            html.append("background:#1b5e20;");
            html.append("}");

            html.append(".success {");
            html.append("margin-top:15px;");
            html.append("padding:12px;");
            html.append("background:#e8f5e9;");
            html.append("color:#1b5e20;");
            html.append("border-radius:9px;");
            html.append("font-weight:bold;");
            html.append("}");

            html.append(".empty {");
            html.append("background:white;");
            html.append("padding:35px;");
            html.append("border-radius:18px;");
            html.append("text-align:center;");
            html.append("color:#777;");
            html.append("box-shadow:0 8px 25px rgba(0,0,0,0.08);");
            html.append("}");

            html.append(".back {");
            html.append("display:block;");
            html.append("width:220px;");
            html.append("margin:30px auto;");
            html.append("text-align:center;");
            html.append("text-decoration:none;");
            html.append("border:1px solid #2e7d32;");
            html.append("color:#2e7d32;");
            html.append("padding:12px;");
            html.append("border-radius:9px;");
            html.append("font-weight:bold;");
            html.append("}");

            html.append(".back:hover {");
            html.append("background:#2e7d32;");
            html.append("color:white;");
            html.append("}");

            html.append(".footer {");
            html.append("text-align:center;");
            html.append("padding-bottom:25px;");
            html.append("color:#999;");
            html.append("font-size:13px;");
            html.append("}");

            html.append("@media(max-width:700px) {");
            html.append(".header {");
            html.append("padding:22px;");
            html.append("flex-direction:column;");
            html.append("gap:10px;");
            html.append("}");
            html.append(".details {");
            html.append("grid-template-columns:1fr;");
            html.append("}");
            html.append(".request-header {");
            html.append("flex-direction:column;");
            html.append("align-items:flex-start;");
            html.append("gap:10px;");
            html.append("}");
            html.append("}");

            html.append("</style>");
            html.append("</head>");

            html.append("<body>");

            html.append("<div class='header'>");
            html.append("<div class='brand'>&#127860; FoodShare</div>");
            html.append("<div class='role'>VOLUNTEER</div>");
            html.append("</div>");

            html.append("<div class='container'>");

            html.append("<div class='page-title'>");
            html.append("<h1>Pickup Requests</h1>");
            html.append("<p>Manage food pickups and complete deliveries.</p>");
            html.append("</div>");

            boolean found = false;

            while (result.next()) {

                found = true;

                int requestId = result.getInt("request_id");

                String pickupStatus =
                    result.getString("pickup_status");

                String statusClass = "accepted";

                if ("ASSIGNED".equals(pickupStatus)) {
                    statusClass = "assigned";
                } else if ("PICKED_UP".equals(pickupStatus)) {
                    statusClass = "picked";
                } else if ("DELIVERED".equals(pickupStatus)) {
                    statusClass = "delivered";
                }

                html.append("<div class='request-card'>");

                html.append("<div class='request-header'>");

                html.append("<h2>Request #")
                    .append(requestId)
                    .append("</h2>");

                html.append("<span class='status ")
                    .append(statusClass)
                    .append("'>")
                    .append(pickupStatus)
                    .append("</span>");

                html.append("</div>");

                html.append("<div class='details'>");

                html.append("<div class='detail'>");
                html.append("<b>Food</b><br>");
                html.append(result.getString("food_name"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Quantity</b><br>");
                html.append(result.getInt("quantity"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Receiver</b><br>");
                html.append(result.getString("receiver_name"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Phone</b><br>");
                html.append(result.getString("phone"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Delivery Location</b><br>");
                html.append(result.getString("location"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Request Status</b><br>");
                html.append(result.getString("status"));
                html.append("</div>");

                html.append("</div>");

                html.append("<div class='actions'>");

                if ("NOT_ASSIGNED".equals(pickupStatus)) {

                    html.append("<a class='button' href='takePickup?request_id=")
                        .append(requestId)
                        .append("'>");

                    html.append("Take Pickup");

                    html.append("</a>");
                }

                if ("ASSIGNED".equals(pickupStatus)) {

                    html.append("<a class='button' href='pickupStatus?request_id=")
                        .append(requestId)
                        .append("&status=PICKED_UP'>");

                    html.append("Mark as Picked Up");

                    html.append("</a>");
                }

                if ("PICKED_UP".equals(pickupStatus)) {

                    html.append("<a class='button' href='pickupStatus?request_id=")
                        .append(requestId)
                        .append("&status=DELIVERED'>");

                    html.append("Mark as Delivered");

                    html.append("</a>");
                }

                if ("DELIVERED".equals(pickupStatus)) {

                    html.append(
                        "<div class='success'>" +
                        "&#9989; Food Delivered Successfully" +
                        "</div>"
                    );
                }

                html.append("</div>");
                html.append("</div>");
            }

            if (!found) {

                html.append("<div class='empty'>");
                html.append("<h3>No Pickup Requests</h3>");
                html.append("<p>There are currently no accepted food requests available for pickup.</p>");
                html.append("</div>");
            }

            html.append("<a class='back' href='volunteer-dashboard.html'>");
            html.append("Back to Dashboard");
            html.append("</a>");

            html.append("<div class='footer'>");
            html.append("FoodShare © 2026");
            html.append("</div>");

            html.append("</div>");
            html.append("</body>");
            html.append("</html>");

            response.getWriter().println(
                html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Error loading pickup requests.</h2>"
            );
        }
    }
}