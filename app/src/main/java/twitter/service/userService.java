// userService.java
package twitter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import twitter.User; // User 클래스가 twitter 패키지에 있다고 가정합니다.

public class userService {

    // 회원가입 메서드
    public void signup(Connection con, User user) throws SQLException {
        String query = "INSERT INTO user (name, password, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getPhone_number());
            pstmt.executeUpdate();
            System.out.println("User registered successfully!");
            System.out.println();
        }
    }

    // 로그인 메서드
    public User login(Connection con, User user) throws SQLException {
        String query = "SELECT * FROM user WHERE name = ? AND password = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Logged in successfully!");

                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String phone_number = rs.getString("phone_number");

                return new User(id, name, password, phone_number);
            } else {
                System.out.println("Invalid username or password.");
                return null;
            }
        } // handling exception only at APP
    }
}
