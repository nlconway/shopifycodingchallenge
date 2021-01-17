public class Image {
    private int id;
    private String address;
    private String name;
    private String userId;
    private User user;
    public Image(int id, String address, String name, User user) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setName(String name) {
        this.name = name;
    }
}
