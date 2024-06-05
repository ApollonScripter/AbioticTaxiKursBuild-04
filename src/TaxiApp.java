import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class TaxiApp {
    private List<User> users;
    private List<Car> cars;
    private Map<String, String> registeredUsers;
    private User currentUser;

    public TaxiApp() {
        this.users = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.registeredUsers = new HashMap<>();
        this.currentUser = null;

        // Добавляем автомобили в систему
        cars.add(new Car("Седан", "Черный", true));
        cars.add(new Car("SUV", "Белый", true));
        cars.add(new Car("Хетчбек", "Красный", true));
    }

    public void registerUser(String username, String password) {
        registeredUsers.put(username, password);
        System.out.println("Регистрация прошла успешно!");
    }

    public boolean authenticateUser(String username, String password) {
        if (registeredUsers.containsKey(username) && registeredUsers.get(username).equals(password)) {
            return true;
        }
        return false;
    }

    public void loadUsersFromFile() {
        try {
            File file = new File("users.csv");
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String savedUsername = parts[0];
                String savedPassword = parts[1];
                registeredUsers.put(savedUsername, savedPassword);
            }
            fileScanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTrip() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите место подачи такси:");
        String pickupLocation = scanner.nextLine();

        System.out.println("Введите ваш пункт назначения:");
        String destination = scanner.nextLine();

        Car requestedCar = requestCar();

        if (requestedCar != null) {
            Trip trip = new Trip(currentUser, pickupLocation, destination, requestedCar.getType());
            if (currentUser.getBalance() >= 100) {
                boolean orderConfirmed = confirmOrder(trip, requestedCar);
                if (orderConfirmed) {
                    System.out.println("Поездка заказана. Ожидайте такси.");
                    startTimer(trip, currentUser);
                } else {
                    System.out.println("Невозможно заказать поездку.");
                }
            } else {
                System.out.println("Недостаточно средств для заказа поездки.");
            }
        } else {
            System.out.println("Извините, в данный момент нет доступных машин.");
        }
    }

    public Car requestCar() {
        List<Car> availableCars = new ArrayList<>();
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.add(car);
            }
        }
        if (!availableCars.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(availableCars.size());
            return availableCars.get(index);
        }
        return null;
    }

    public boolean confirmOrder(Trip trip, Car car) {
        if (car.isAvailable()) {
            car.setAvailable(false);
            trip.setStatus("Подтверждено");
            return true;
        }
        return false;
    }

    public void startTimer(Trip trip, User user) {
        System.out.println("Ваше такси уже в пути!");
        try {
            Thread.sleep(300000); // 5 минут
            trip.setStatus("Завершено");
            user.deductFare(100);
            System.out.println("Вы доставлены! Спасибо за поездку!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void displayAvailableCars() {
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car);
            }
        }
    }

    public void showBalance() {
        System.out.println("Ваш текущий баланс: " + currentUser.getBalance() + " руб.");
    }

    public void addFunds(double amount) {
        if (amount > 0) {
            currentUser.addFunds(amount);
            System.out.println("Ваш баланс успешно пополнен на " + amount + " руб.");
        } else {
            System.out.println("Сумма пополнения должна быть положительной.");
        }
    }

    public static void main(String[] args) {
        TaxiApp taxiApp = new TaxiApp();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в систему такси!");
        System.out.println("Что вы хотите сделать?");
        System.out.println("1. Авторизоваться");
        System.out.println("2. Зарегистрироваться");

        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 2) {
            System.out.println("Введите ваш логин:");
            String username = scanner.nextLine();

            System.out.println("Введите ваш пароль:");
            String password = scanner.nextLine();

            currentUser = new User(username, password, 0);
            taxiApp.registerUser(username, password);

            try {
                FileWriter writer = new FileWriter("users.csv", true);
                writer.write(username + "," + password + "\n");
                writer.close();
                System.out.println("Регистрация завершена. Теперь вы можете авторизоваться.");
            } catch (IOException e) {
                System.out.println("Ошибка при записи данных в файл.");
                e.printStackTrace();
            }
        }

        if (choice == 1) {
            System.out.println("Введите ваш логин:");
            String username = scanner.nextLine();

            System.out.println("Введите ваш пароль:");
            String password = scanner.nextLine();

            boolean authenticated = taxiApp.authenticateUser(username, password);

            if (authenticated) {
                currentUser = new User(username, password, 100);
                System.out.println("Авторизация успешна!");

                boolean exit = false;
                while (!exit) {
                    System.out.println("Выберите действие:");
                    System.out.println("1. Сделать заказ");
                    System.out.println("2. Посмотреть доступные машины");
                    System.out.println("3. Посмотреть баланс");
                    System.out.println("4. Пополнить баланс");
                    System.out.println("5. Выйти");

                    int userChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (userChoice) {
                        case 1:
                            taxiApp.createTrip();
                            break;
                        case 2:
                            taxiApp.displayAvailableCars();
                            break;
                        case 3:
                            taxiApp.showBalance();
                            break;
                        case 4:
                            System.out.println("Введите сумму для пополнения:");
                            double amount = scanner.nextDouble();
                            scanner.nextLine();
                            taxiApp.addFunds(amount);
                            break;
                        case 5:
                            exit = true;
                            System.out.println("Выход из системы. Спасибо за использование нашего сервиса!");
                            break;
                        default:
                            System.out.println("Неверный выбор. Пожалуйста, выберите действие от 1 до 5.");
                            break;
                    }
                }
            } else {
                System.out.println("Неправильный логин или пароль.");
            }
        }
    }
}