public class Car {
    private String type;
    private String color;
    private boolean available;

    public Car(String type, String color, boolean available) {
        this.type = type;
        this.color = color;
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "Car{Type: " + type + ", Color: " + color + ", Available: " + available + "}";
    }
}