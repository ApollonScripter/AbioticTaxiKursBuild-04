public class Person {
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;

    public Person(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{First Name: " + firstName + ", Last Name: " + lastName + ", Phone Number: " + phoneNumber + "}";
    }
}