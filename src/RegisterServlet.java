import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String location = request.getParameter("location");
        String role = request.getParameter("role");

        User user = new User(
            name,
            email,
            password,
            role,
            phone,
            location
        );

        UserDAO userDAO = new UserDAO();

        boolean result = userDAO.registerUser(user);

        response.setContentType("text/html");

        if (result) {
            response.getWriter().println(
                "<h2>Registration Successful!</h2>"
            );
        } else {
            response.getWriter().println(
                "<h2>Registration Failed!</h2>"
            );
        }
    }
}