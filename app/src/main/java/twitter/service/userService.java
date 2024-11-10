// userService.java
package twitter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import twitter.model.User; // User 클래스가 twitter 패키지에 있다고 가정합니다.

public class userService {

    public User currentUser = null;

    // 회원가입 메서드
    public void signup(Connection con, User user) throws SQLException {
        String query = "INSERT INTO Users (email, name, password, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getAddress());
            pstmt.executeUpdate();
            //System.out.println("User registered successfully!");
            System.out.println();
        }
    }

// 로그인 메서드
    public void login(Connection con, User user) throws SQLException {
        String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getEmail().trim()); // 입력된 이메일의 공백 제거
            pstmt.setString(2, user.getPassword().trim()); // 입력된 비밀번호의 공백 제거

            System.out.println("Executing query: " + query);
            System.out.println("Email: " + user.getEmail());
            System.out.println("Password: " + user.getPassword());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Logged in successfully!");

                int id = rs.getInt("user_id");
                String email = rs.getString("email");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String address = rs.getString("address");

                // 현재 로그인한 사용자 정보를 currentUser에 저장
                currentUser = new User(id, email, name, password, address);
            } else {
                System.out.println("Invalid username or password.");

            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteAccount(Connection con, User user) throws SQLException {
        String query = "DELETE FROM user WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;
        }
    }
}
