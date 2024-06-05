public class User {
    private String username;
    private String password;
    private double balance;

    public User(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            System.out.println("Сумма пополнения должна быть положительной.");
        }
    }

    public void addFunds(double amount) {
        addBalance(amount);
    }

    public void deductFare(double fare) {
        if (balance >= fare) {
            balance -= fare;
        } else {
            System.out.println("Недостаточно средств для списания.");
        }
    }
}