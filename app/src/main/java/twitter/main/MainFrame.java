package twitter.main;

import twitter.service.userService;
import twitter.ui.Login_Ui;
import twitter.ui.SignUp_Ui;
import twitter.ui.TopicPanel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

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

        //showTopicPanel();   // 테스트 하고자 바꾸어 놨습니다! 작업시 변경 바랍니다 ~!
        showLoginPanel();
    }

    public void showTopicPanel() {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // 테스트를 위한 임시 주제 데이터 생성 
        Map<String, Integer> topics = new HashMap<>();
        topics.put("Java", 10);
        topics.put("Swing", 7);
        topics.put("Database", 15);
        topics.put("AI", 5);

        currentPanel = new TopicPanel(topics);  // 임시 데이터 전달
        add(currentPanel);
        revalidate();
        repaint();
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
            connection = DriverManager.getConnection("jdbc:mysql://58.121.110.129:4472/twitter", "root","ckwnsgk@1");
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
