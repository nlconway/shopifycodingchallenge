public class Image {
    private int id;
    private String address;
    private String name;
    private User user;

    //Image object, has an id, address(url), name and user who's added
    Image(int id, String address, String name, User user) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.user = user;
    }

    int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    String getName() {
        return name;
    }

    public User getUser() {
        return user;
    }
}
