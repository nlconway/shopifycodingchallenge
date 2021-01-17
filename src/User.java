public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String userName;

    public User(int id, String firstName, String lastName, String userName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
    }
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
