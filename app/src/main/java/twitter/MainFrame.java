package twitter;

import twitter.service.userService;
import twitter.ui.Login_Ui;
import twitter.ui.SignUp_Ui;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class MainFrame extends JFrame {
    private static Connection connection;
    private JPanel currentPanel;
    private userService userService = new userService();

    public MainFrame(Connection connection, userService userService)
    {
        this.connection = connection;
        this.userService = userService;

        setTitle("Twitter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,600);

        showLoginPanel();
    }

    public void showLoginPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new Login_Ui(this, connection, userService);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showSignUpPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new SignUp_Ui(this, connection, userService);
        add(currentPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/twitter", "root","pw");
            userService userService = new userService();
            // 로그인 UI 띄우기
            SwingUtilities.invokeLater(() -> {
                MainFrame frame = new MainFrame(connection, userService);
                frame.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
