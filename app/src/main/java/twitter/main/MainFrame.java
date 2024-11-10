package twitter.main;

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
        MainFrame.connection = connection;
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

    // 필요한 경우 다른 Panel 추가하는 방식으로 만들면 될거 같아요!


    public static void main(String[] args) 
    {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://58.121.110.129:4472/twitter", "root","ckwnskg@1");
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
