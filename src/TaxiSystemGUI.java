import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaxiSystemGUI extends JFrame {
    private TaxiSystem taxiSystem;
    private User currentUser;

    public TaxiSystemGUI() {
        taxiSystem = new TaxiSystem();
        currentUser = new User("testUser", "password", 100); // Пример пользователя

        setTitle("Taxi System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Панель с кнопками
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton orderButton = new JButton("Сделать заказ");
        JButton availableCarsButton = new JButton("Посмотреть доступные машины");
        JButton balanceButton = new JButton("Посмотреть баланс");
        JButton addFundsButton = new JButton("Пополнить баланс");
        JButton exitButton = new JButton("Выйти");

        panel.add(orderButton);
        panel.add(availableCarsButton);
        panel.add(balanceButton);
        panel.add(addFundsButton);
        panel.add(exitButton);

        add(panel);

        // Обработчики событий
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTrip();
            }
        });

        availableCarsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableCars();
            }
        });

        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBalance();
            }
        });

        addFundsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFunds();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void createTrip() {
        String pickupLocation = JOptionPane.showInputDialog("Введите место подачи такси:");
        String destination = JOptionPane.showInputDialog("Введите ваш пункт назначения:");

        Car requestedCar = taxiSystem.requestCar();

        if (requestedCar != null) {
            Trip trip = new Trip(currentUser, pickupLocation, destination, requestedCar.getType());
            taxiSystem.confirmOrder(trip, requestedCar);
            JOptionPane.showMessageDialog(this, "Поездка заказана. Ожидайте такси.");
        } else {
            JOptionPane.showMessageDialog(this, "Извините, в данный момент нет доступных машин.");
        }
    }

    private void displayAvailableCars() {
        StringBuilder availableCars = new StringBuilder("Доступные машины:\n");
        for (Car car : taxiSystem.getCars()) {
            if (car.isAvailable()) {
                availableCars.append(car).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, availableCars.toString());
    }

    private void showBalance() {
        JOptionPane.showMessageDialog(this, "Ваш текущий баланс: " + currentUser.getBalance() + " руб.");
    }

    private void addFunds() {
        String amountStr = JOptionPane.showInputDialog("Введите сумму для пополнения:");
        double amount = Double.parseDouble(amountStr);
        currentUser.addFunds(amount);
        JOptionPane.showMessageDialog(this, "Ваш баланс успешно пополнен на " + amount + " руб.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaxiSystemGUI().setVisible(true);
            }
        });
    }
}