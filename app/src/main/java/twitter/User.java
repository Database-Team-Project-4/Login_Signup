package twitter;

public class User {
    private int id;
    private String name;
    private String password;
    private String phone_number;

    public User(int id, String name, String password, String phone_number) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
    }

    public User(String name, String password, String phone_number) {
        this.name = name;
        this.password = password;
        this.phone_number = phone_number;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number(){
        return phone_number;
    }

    @Override
    public String toString() {
        return "User [ID=" + id + ", Username=" + name + ", Phone_number=" + phone_number + "]";
    }

}
