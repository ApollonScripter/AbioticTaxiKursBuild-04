public class Order {
    private User user;
    private String pickupLocation;
    private String destination;
    private String carType;
    protected String status; // Изменяем модификатор доступа на protected

    public Order(User user, String pickupLocation, String destination, String carType) {
        this.user = user;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.carType = carType;
        this.status = "Pending"; // По умолчанию статус заказа "В ожидании"
    }

    // Геттер для получения статуса заказа
    public String getStatus() {
        return status;
    }

    // Сеттер для установки статуса заказа
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{User: " + user + ", Pickup Location: " + pickupLocation + ", Destination: " + destination + ", Car Type: " + carType + ", Status: " + status + "}";
    }
}