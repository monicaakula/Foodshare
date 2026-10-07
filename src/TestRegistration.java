public class TestRegistration {

    public static void main(String[] args) {

        User user = new User(
            "Monica",
            "monica@gmail.com",
            "12345",
            "DONOR",
            "9876543210",
            "Hyderabad"
        );

        UserDAO userDAO = new UserDAO();

        boolean result = userDAO.registerUser(user);

        if (result) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Registration failed!");
        }
    }
}
