public class Trip extends Order {
    private String startTime;
    private String endTime;

    public Trip(User user, String pickupLocation, String destination, String carType) {
        super(user, pickupLocation, destination, carType);
        this.status = "In Progress"; // Статус заказа "В процессе" при создании поездки
    }

    // Метод для запуска таймера и завершения поездки
    public void startTrip() {
        System.out.println("Ваше такси уже в пути!");
        System.out.println("Детали вашего заказа:");
        System.out.println(this);

        // Симулируем 5 минут для доставки
        try {
            Thread.sleep(300000); // 300000 миллисекунд = 5 минут
            endTime = "Сейчас"; // Устанавливаем время окончания поездки
            status = "Completed"; // Устанавливаем статус "Завершено"
            System.out.println("Вы доставлены! Спасибо за поездку!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Status: " + status + ", Start Time: " + startTime + ", End Time: " + endTime;
    }
}