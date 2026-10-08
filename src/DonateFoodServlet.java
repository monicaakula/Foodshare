import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Base64;

@WebServlet("/donateFood")
@MultipartConfig(
    maxFileSize = 10 * 1024 * 1024,
    maxRequestSize = 12 * 1024 * 1024
)
public class DonateFoodServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String foodName = request.getParameter("food_name");

        int quantity =
            Integer.parseInt(
                request.getParameter("quantity")
            );

        String foodType =
            request.getParameter("food_type");

        String description =
            request.getParameter("description");

        String location =
            request.getParameter("location");

        String availableUntil =
            request.getParameter("available_until");

        String donationType =
            request.getParameter("donation_type");

        String priceText =
            request.getParameter("price");

        double price = 0;

        if (priceText != null &&
            !priceText.trim().isEmpty()) {

            price = Double.parseDouble(priceText);

        }


        // ==========================================
        // GET FOOD PHOTO
        // ==========================================

        Part imagePart =
            request.getPart("food_image");


        String imagePath = "";


        if (imagePart != null &&
            imagePart.getSize() > 0) {


            // Read image bytes

            byte[] imageBytes =
                imagePart.getInputStream()
                         .readAllBytes();


            // Convert image to Base64

            String base64Image =
                Base64.getEncoder()
                      .encodeToString(imageBytes);


            // Get image type

            String contentType =
                imagePart.getContentType();


            if (contentType == null ||
                contentType.isEmpty()) {

                contentType = "image/jpeg";

            }


            // Create complete image data

            imagePath =
                "data:" +
                contentType +
                ";base64," +
                base64Image;


            System.out.println(
                "Food image received successfully!"
            );

            System.out.println(
                "Image size: " +
                imageBytes.length +
                " bytes"
            );

        }


        // ==========================================
        // TEMPORARY DONOR ID
        // ==========================================

        int donorId = 1;


        // ==========================================
        // CREATE FOOD OBJECT
        // ==========================================

        FoodItem food =
            new FoodItem(

                donorId,

                foodName,

                quantity,

                foodType,

                description,

                imagePath,

                location,

                availableUntil.replace(
                    "T",
                    " "
                ),

                donationType,

                price

            );


        // ==========================================
        // SAVE FOOD
        // ==========================================

        FoodItemDAO foodDAO =
            new FoodItemDAO();


        boolean result =
            foodDAO.addFood(food);


        // ==========================================
        // RESPONSE
        // ==========================================

        response.setContentType(
            "text/html;charset=UTF-8"
        );


        if (result) {

            response.getWriter().println(

                "<!DOCTYPE html>" +

                "<html>" +

                "<head>" +

                "<meta charset='UTF-8'>" +

                "<title>FoodShare</title>" +

                "<style>" +

                "body{" +
                "font-family:Arial;" +
                "background:#e8f5e9;" +
                "text-align:center;" +
                "padding:50px;" +
                "}" +

                ".box{" +
                "background:white;" +
                "padding:35px;" +
                "border-radius:15px;" +
                "max-width:500px;" +
                "margin:auto;" +
                "box-shadow:0 8px 25px rgba(0,0,0,0.1);" +
                "}" +

                "h2{" +
                "color:#2e7d32;" +
                "}" +

                "a{" +
                "display:inline-block;" +
                "margin-top:20px;" +
                "padding:12px 20px;" +
                "background:#2e7d32;" +
                "color:white;" +
                "text-decoration:none;" +
                "border-radius:8px;" +
                "}" +

                "</style>" +

                "</head>" +

                "<body>" +

                "<div class='box'>" +

                "<h2>Food Donation Successful! 🎉</h2>" +

                "<p>Food Name: " +
                foodName +
                "</p>" +

                "<p>Quantity: " +
                quantity +
                "</p>" +

                "<p>Food Type: " +
                foodType +
                "</p>" +

                "<p>Location: " +
                location +
                "</p>" +

                "<p>Donation Type: " +
                donationType +
                "</p>" +

                "<p>Photo Saved: " +
                (
                    imagePath.isEmpty()
                    ? "No"
                    : "Yes"
                ) +
                "</p>" +

                "<a href='donor-dashboard.html'>" +
                "Back to Dashboard" +
                "</a>" +

                "</div>" +

                "</body>" +

                "</html>"
            );

        }

        else {

            response.getWriter().println(

                "<h2>Food Donation Failed!</h2>" +

                "<p>Please check the server logs.</p>"

            );

        }

    }

}