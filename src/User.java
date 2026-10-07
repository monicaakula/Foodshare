public class User {

    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String location;

    public User(String name, String email, String password,
                String role, String phone, String location) {

        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }
}