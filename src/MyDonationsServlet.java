import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/myDonations")
public class MyDonationsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Temporary donor ID for testing
        int donorId = 1;

        String sql =
            "SELECT * FROM food_items " +
            "WHERE donor_id = ? " +
            "ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setInt(1, donorId);

            ResultSet result = statement.executeQuery();

            StringBuilder html = new StringBuilder();

            html.append("<!DOCTYPE html>");
            html.append("<html>");
            html.append("<head>");
            html.append("<meta charset='UTF-8'>");
            html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            html.append("<title>FoodShare - My Donations</title>");

            html.append("<style>");

            html.append("* {");
            html.append("margin:0;");
            html.append("padding:0;");
            html.append("box-sizing:border-box;");
            html.append("font-family:Arial,sans-serif;");
            html.append("}");

            html.append("body {");
            html.append("min-height:100vh;");
            html.append("background:linear-gradient(135deg,#e8f5e9,#f8fff8);");
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
            html.append("max-width:1050px;");
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

            html.append(".donation-grid {");
            html.append("display:grid;");
            html.append("grid-template-columns:repeat(2,1fr);");
            html.append("gap:25px;");
            html.append("}");

            html.append(".donation-card {");
            html.append("background:white;");
            html.append("border-radius:18px;");
            html.append("overflow:hidden;");
            html.append("box-shadow:0 8px 25px rgba(0,0,0,0.09);");
            html.append("transition:transform 0.2s,box-shadow 0.2s;");
            html.append("}");

            html.append(".donation-card:hover {");
            html.append("transform:translateY(-5px);");
            html.append("box-shadow:0 14px 30px rgba(0,0,0,0.13);");
            html.append("}");

            html.append(".food-image {");
            html.append("width:100%;");
            html.append("height:220px;");
            html.append("object-fit:cover;");
            html.append("display:block;");
            html.append("}");

            html.append(".no-image {");
            html.append("width:100%;");
            html.append("height:220px;");
            html.append("background:#e8f5e9;");
            html.append("display:flex;");
            html.append("justify-content:center;");
            html.append("align-items:center;");
            html.append("font-size:60px;");
            html.append("}");

            html.append(".content {");
            html.append("padding:25px;");
            html.append("}");

            html.append(".content h2 {");
            html.append("color:#2e7d32;");
            html.append("font-size:24px;");
            html.append("margin-bottom:18px;");
            html.append("}");

            html.append(".detail {");
            html.append("background:#f7faf7;");
            html.append("padding:12px;");
            html.append("margin-bottom:10px;");
            html.append("border-radius:9px;");
            html.append("line-height:1.5;");
            html.append("}");

            html.append(".detail b {");
            html.append("color:#555;");
            html.append("}");

            html.append(".status {");
            html.append("display:inline-block;");
            html.append("padding:7px 14px;");
            html.append("border-radius:20px;");
            html.append("font-size:13px;");
            html.append("font-weight:bold;");
            html.append("background:#e8f5e9;");
            html.append("color:#2e7d32;");
            html.append("}");

            html.append(".empty {");
            html.append("background:white;");
            html.append("padding:40px;");
            html.append("border-radius:18px;");
            html.append("text-align:center;");
            html.append("color:#777;");
            html.append("box-shadow:0 8px 25px rgba(0,0,0,0.08);");
            html.append("}");

            html.append(".back {");
            html.append("display:block;");
            html.append("width:220px;");
            html.append("margin:35px auto;");
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

            html.append("@media(max-width:750px) {");

            html.append(".donation-grid {");
            html.append("grid-template-columns:1fr;");
            html.append("}");

            html.append(".header {");
            html.append("padding:22px;");
            html.append("flex-direction:column;");
            html.append("gap:10px;");
            html.append("}");

            html.append(".page-title h1 {");
            html.append("font-size:30px;");
            html.append("}");

            html.append("}");

            html.append("</style>");
            html.append("</head>");

            html.append("<body>");

            html.append("<div class='header'>");
            html.append("<div class='brand'>&#127860; FoodShare</div>");
            html.append("<div class='role'>DONOR</div>");
            html.append("</div>");

            html.append("<div class='container'>");

            html.append("<div class='page-title'>");
            html.append("<h1>My Donations</h1>");
            html.append("<p>View the food you have shared through FoodShare.</p>");
            html.append("</div>");

            html.append("<div class='donation-grid'>");

            boolean found = false;

            while (result.next()) {

                found = true;

                html.append("<div class='donation-card'>");

                String imagePath = result.getString("image_path");

                if (imagePath != null && !imagePath.isEmpty()) {

                    html.append("<img class='food-image' src='")
                        .append(imagePath)
                        .append("' alt='Food image'>");

                } else {

                    html.append("<div class='no-image'>");
                    html.append("&#127860;");
                    html.append("</div>");
                }

                html.append("<div class='content'>");

                html.append("<h2>")
                    .append(result.getString("food_name"))
                    .append("</h2>");

                html.append("<div class='detail'>");
                html.append("<b>Quantity:</b> ");
                html.append(result.getInt("quantity"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Food Type:</b> ");
                html.append(result.getString("food_type"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Location:</b> ");
                html.append(result.getString("location"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Donation Type:</b> ");
                html.append(result.getString("donation_type"));
                html.append("</div>");

                html.append("<div class='detail'>");
                html.append("<b>Status:</b><br><br>");
                html.append("<span class='status'>");
                html.append(result.getString("status"));
                html.append("</span>");
                html.append("</div>");

                html.append("</div>");
                html.append("</div>");
            }

            html.append("</div>");

            if (!found) {

                html.append("<div class='empty'>");
                html.append("<h2>No Donations Found</h2>");
                html.append("<p>You have not added any food donations yet.</p>");
                html.append("</div>");
            }

            html.append("<a class='back' href='donor-dashboard.html'>");
            html.append("Back to Dashboard");
            html.append("</a>");

            html.append("<div class='footer'>");
            html.append("FoodShare © 2026");
            html.append("</div>");

            html.append("</div>");
            html.append("</body>");
            html.append("</html>");

            response.getWriter().println(html.toString());

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "<h2>Error loading donations.</h2>"
            );
        }
    }
}