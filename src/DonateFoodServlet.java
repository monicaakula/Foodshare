import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/donateFood")
@MultipartConfig
public class DonateFoodServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String foodName = request.getParameter("food_name");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String foodType = request.getParameter("food_type");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String availableUntil = request.getParameter("available_until");
        String donationType = request.getParameter("donation_type");
        double price = Double.parseDouble(request.getParameter("price"));

        Part imagePart = request.getPart("food_image");

        String imagePath = "";

        if (imagePart != null && imagePart.getSize() > 0) {

            String fileName = imagePart.getSubmittedFileName();

            String uploadPath = getServletContext().getRealPath("/uploads");

            java.io.File uploadDir = new java.io.File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            imagePart.write(uploadPath + java.io.File.separator + fileName);

            imagePath = "uploads/" + fileName;
        }

        // Temporary donor ID for testing
        int donorId = 1;

        FoodItem food = new FoodItem(
            donorId,
            foodName,
            quantity,
            foodType,
            description,
            imagePath,
            location,
            availableUntil.replace("T", " "),
            donationType,
            price
        );

        FoodItemDAO foodDAO = new FoodItemDAO();

        boolean result = foodDAO.addFood(food);

        response.setContentType("text/html");

        if (result) {

            response.getWriter().println(
                "<h2>Food Donation Successful! 🎉</h2>" +
                "<p>Food Name: " + foodName + "</p>" +
                "<p>Quantity: " + quantity + "</p>" +
                "<p>Food Type: " + foodType + "</p>" +
                "<p>Location: " + location + "</p>" +
                "<p>Donation Type: " + donationType + "</p>" +
                "<p>Photo Saved: " +
                (imagePath.isEmpty() ? "No" : "Yes") +
                "</p>"
            );

        } else {

            response.getWriter().println(
                "<h2>Food Donation Failed!</h2>" +
                "<p>Please check the Tomcat console for the error.</p>"
            );
        }
    }
}

