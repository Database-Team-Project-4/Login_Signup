package twitter;

import twitter.service.userService;
import twitter.ui.*;
import java.sql.*;
import javax.swing.*;


// 현재 MainFrame으로 변경된 상태
public class app_test {
    private static Connection con;

    public static void main(String[] args) {
        userService userService = new userService();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/twitter", "root", "pw");

            // 로그인 UI 띄우기
            SwingUtilities.invokeLater(() -> {
                Login_Ui loginUi = new Login_Ui(con, userService);
                loginUi.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
