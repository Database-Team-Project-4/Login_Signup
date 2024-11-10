package twitter.model;

public class User {
    private int id;
    private String name;
    private String password;
    private String address;
    private String email;

    public User(int id, String email, String name, String password, String address) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
    }

    public User(String email, String name, String password, String address) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress(){
        return address;
    }

    @Override
    public String toString() {
        return "User [ID=" + id + ", Email=" + email + ", Username=" + name + ", Address=" + address + "]";
    }


}
