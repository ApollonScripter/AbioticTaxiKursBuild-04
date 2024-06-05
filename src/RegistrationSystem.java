import java.util.HashMap;
import java.util.Map;

public class RegistrationSystem {
    private Map<String, String> users;

    public RegistrationSystem() {
        this.users = new HashMap<>();
    }

    public void registerUser(String username, String password) {
        users.put(username, password);
        System.out.println("Регистрация прошла успешно!");
    }

    public boolean authenticateUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            return true;
        }
        return false;
    }

    public void registerUser(User currentUser) {
    }
}
