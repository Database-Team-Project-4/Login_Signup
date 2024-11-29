package twitter.ui.login;

import twitter.Controller.userController;
import twitter.main.MainFrame;
import twitter.model.User;

import javax.swing.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Login_Ui extends JPanel {
    private userController userController;
    private Connection connection;
    private final ImageIcon xLogoIcon = loadIcon("/TwitterIcons/X_logo.png");

    // 기존 기능 유지하며 아이콘 로드 기능
    private ImageIcon loadIcon(String path) {
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }

    public Login_Ui(MainFrame mainframe , Connection connection, userController userController)
    {
        this.connection = connection;
        this.userController = userController;

        Image scaledImage = xLogoIcon.getImage().getScaledInstance(
                (int) (xLogoIcon.getIconWidth() * 1.3),
                (int) (xLogoIcon.getIconHeight() * 1.3),
                Image.SCALE_SMOOTH
        );

        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        setSize(400, 600);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setLayout(new BorderLayout()); // 라벨 중앙 배치

        // 여백 추가
        titlePanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0)); // 위, 좌, 아래, 우 여백 설정

        JLabel titleLabel = new JLabel(scaledIcon, SwingConstants.CENTER);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel idLabel = new JLabel("Email: ");
        idLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(idLabel, gbc);

        JTextField idField = new JTextField(10);
        idField.setBackground(Color.BLACK);
        idField.setForeground(Color.WHITE);
        idField.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 1;
        inputPanel.add(idField, gbc);

        JLabel pwLabel = new JLabel("Password: ");
        pwLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(pwLabel, gbc);

        JTextField pwField = new JTextField(10);
        pwField.setBackground(Color.BLACK);
        pwField.setForeground(Color.WHITE);
        pwField.setBorder(BorderFactory.createEmptyBorder());
        gbc.gridx = 1;
        inputPanel.add(pwField, gbc);

        // 로그인 버튼 생성
        JButton loginButton = new JButton("Log in");
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setBackground(Color.WHITE);
        loginButton.setForeground(Color.BLACK);
        loginButton.setOpaque(true);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(loginButton, gbc);

        // 로그인 버튼 클릭 이벤트
        loginButton.addActionListener(e -> {
            String email = idField.getText();
            String password = pwField.getText();
            User user = new User(email, password);

            try {
                userController.login(connection, user);
                if (userController.getCurrentUser() != null) {
                    JOptionPane.showMessageDialog(mainframe, "Welcome, " + userController.getCurrentUser().getName() + "!");
                    mainframe.showTwitterMainUiPanel(); //잘 넘어가는지 확인하기 위해서 회원가입 panel로 넘어감, mainpanel로 연결 예정
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during login.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 회원가입 버튼 생성
        JButton signupButton = new JButton("Don't have an account? Sign up!");
        signupButton.setFocusPainted(false);
        signupButton.setBorderPainted(false);
        signupButton.setOpaque(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setForeground(new Color(100, 149, 237)); // 은은한 파란색
        gbc.gridy = 3;
        inputPanel.add(signupButton, gbc);

        // 회원가입 버튼 클릭 시 회원가입 UI로 이동
        signupButton.addActionListener(e -> {
            // 회원가입 창 열기
            mainframe.showSignUpPanel();
        });

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}
