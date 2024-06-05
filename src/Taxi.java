public class Taxi {
    private String driverName;
    private String carType;
    private boolean available;

    public Taxi(String driverName, String carType, boolean available) {
        this.driverName = driverName;
        this.carType = carType;
        this.available = available;
    }
    // Геттер для получения типа автомобиля
    public String getCarType() {
        return carType;
    }
    // Геттер для проверки доступности такси
    public boolean isAvailable() {
        return available;
    }
    // Сеттер для установки доступности такси
    public void setAvailable(boolean available) {
        this.available = available;
    }
    @Override
    public String toString() {
        return "Taxi{Driver: " + driverName + ", Car Type: " + carType + ", Available: " + available + "}";
    }

}