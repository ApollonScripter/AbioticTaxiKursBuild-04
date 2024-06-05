import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TaxiSystem {
    private List<User> users;
    private List<Taxi> taxis;
    private List<Trip> trips;
    private List<Car> cars;
    private static final String FILENAME = "users.csv";

    public TaxiSystem() {
        this.users = new ArrayList<>();
        this.taxis = new ArrayList<>();
        this.trips = new ArrayList<>();
        this.cars = new ArrayList<>();

        cars.add(new Car("Седан", "Черный", true));
        cars.add(new Car("SUV", "Белый", true));
        cars.add(new Car("Хетчбек", "Красный", true));
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

    public void displayAvailableCars() {
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car);
            }
        }
    }

    public boolean confirmOrder(Trip trip, Car car) {
        if (car.isAvailable()) {
            car.setAvailable(false);
            trip.setStatus("Подтверждено");
            return true;
        }
        return false;
    }

    public boolean cancelOrder(Trip trip) {
        if (trip.getStatus().equals("В ожидании")) {
            trips.remove(trip);
            return true;
        }
        return false;
    }

    public String trackOrderStatus(Trip trip) {
        return trip.getStatus();
    }

    public void createTrip(User user) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите место подачи такси:");
        String pickupLocation = scanner.nextLine();

        System.out.println("Введите ваш пункт назначения:");
        String destination = scanner.nextLine();

        Car requestedCar = requestCar();

        if (requestedCar != null) {
            Trip trip = new Trip(user, pickupLocation, destination, requestedCar.getType());
            trips.add(trip);

            if (user.getBalance() >= 100) {
                boolean orderConfirmed = confirmOrder(trip, requestedCar);
                if (orderConfirmed) {
                    System.out.println("Поездка заказана. Ожидайте такси.");
                    startTimer(trip, user);
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

    private void startTimer(Trip trip, User user) {
        System.out.println("Ваше такси уже в пути!");
        try {
            Thread.sleep(300000); // 5 минут
            trip.setStatus("Completed");
            user.deductFare(100);
            System.out.println("Вы доставлены! Спасибо за поездку!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Car> getCars() {
        return cars;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaxiSystem taxiSystem = new TaxiSystem();

        RegistrationSystem registrationSystem = new RegistrationSystem();
        boolean authenticated = false;
        User currentUser = null;

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

            currentUser = new User(username, password, 0); // Инициализация с начальным балансом
            registrationSystem.registerUser(currentUser);

            try {
                FileWriter writer = new FileWriter(FILENAME, true);
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

            boolean authenticatedFromFile = false;
            try {
                File file = new File(FILENAME);
                Scanner fileScanner = new Scanner(file);
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(",");
                    String savedUsername = parts[0];
                    String savedPassword = parts[1];
                    if (savedUsername.equals(username) && savedPassword.equals(password)) {
                        authenticatedFromFile = true;
                        currentUser = new User(username, password, 100); // Предполагаем начальный баланс 100
                        break;
                    }
                }
                fileScanner.close();
            } catch (IOException e) {
                System.out.println("Ошибка при чтении данных из файла.");
                e.printStackTrace();
            }

            if (authenticatedFromFile) {
                System.out.println("Авторизация успешна!");
                authenticated = true;
            } else {
                System.out.println("Неправильный логин или пароль.");
            }
        }

        if (authenticated) {
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
                        taxiSystem.createTrip(currentUser);
                        break;
                    case 2:
                        taxiSystem.displayAvailableCars();
                        break;
                    case 3:
                        System.out.println("Ваш текущий баланс: " + currentUser.getBalance() + " руб.");
                        break;
                    case 4:
                        System.out.println("Введите сумму для пополнения:");
                        double amount = scanner.nextDouble();
                        scanner.nextLine(); // очистка буфера после чтения числа
                        if (amount > 0) {
                            currentUser.addBalance(amount);
                            System.out.println("Баланс успешно пополнен. Ваш текущий баланс: " + currentUser.getBalance() + " руб.");
                        } else {
                            System.out.println("Сумма пополнения должна быть положительной.");
                        }
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
        }
    }
}
