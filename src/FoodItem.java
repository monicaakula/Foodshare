public class FoodItem {

    private int donorId;
    private String foodName;
    private int quantity;
    private String foodType;
    private String description;
    private String imagePath;
    private String location;
    private String availableUntil;
    private String donationType;
    private double price;

    public FoodItem(
            int donorId,
            String foodName,
            int quantity,
            String foodType,
            String description,
            String imagePath,
            String location,
            String availableUntil,
            String donationType,
            double price) {

        this.donorId = donorId;
        this.foodName = foodName;
        this.quantity = quantity;
        this.foodType = foodType;
        this.description = description;
        this.imagePath = imagePath;
        this.location = location;
        this.availableUntil = availableUntil;
        this.donationType = donationType;
        this.price = price;
    }

    public int getDonorId() {
        return donorId;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLocation() {
        return location;
    }

    public String getAvailableUntil() {
        return availableUntil;
    }

    public String getDonationType() {
        return donationType;
    }

    public double getPrice() {
        return price;
    }
}
