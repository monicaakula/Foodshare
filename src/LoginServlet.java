import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {


protected void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    String sql =
        "SELECT * FROM users WHERE email = ? AND password = ?";

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement =
             connection.prepareStatement(sql)) {

        statement.setString(1, email);
        statement.setString(2, password);

        ResultSet result = statement.executeQuery();

        if (result.next()) {

            String role = result.getString("role");

            // Remove extra spaces and handle upper/lower case
            if (role != null) {
                role = role.trim().toUpperCase();
            }

            if ("DONOR".equals(role)) {

                response.sendRedirect(
                    request.getContextPath() +
                    "/donor-dashboard.html"
                );

            } else if ("RECEIVER".equals(role)) {

                response.sendRedirect(
                    request.getContextPath() +
                    "/receiver-dashboard.html"
                );

            } else if ("VOLUNTEER".equals(role)) {

                response.sendRedirect(
                    request.getContextPath() +
                    "/volunteer-dashboard.html"
                );

            } else {

                response.setContentType("text/html");

                response.getWriter().println(
                    "<h2>Login Successful!</h2>" +
                    "<p>Welcome, " +
                    result.getString("name") +
                    "!</p>" +
                    "<p>Role found in database: " +
                    role +
                    "</p>"
                );
            }

        } else {

            response.setContentType("text/html");

            response.getWriter().println(
                "<h2>Login Failed!</h2>" +
                "<p>Invalid email or password.</p>"
            );
        }

    } catch (Exception e) {

        e.printStackTrace();

        response.setContentType("text/html");

        response.getWriter().println(
            "<h2>Login Error!</h2>" +
            "<p>" + e.getMessage() + "</p>"
        );
    }
}


}
