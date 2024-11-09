package twitter.ui;

import twitter.MainFrame;
import twitter.service.userService;
import twitter.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Login_Ui extends JPanel {
    private userService userService;
    private Connection connection;

    public Login_Ui(MainFrame mainframe , Connection connection, userService userService) {
        this.connection = connection;
        this.userService = userService;

        setSize(400, 600);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("X", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

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
                userService.login(connection, user);
                if (userService.currentUser != null) {
                    JOptionPane.showMessageDialog(this, "Welcome, " + userService.currentUser.getName() + "!");
                    mainframe.showSignUpPanel(); //잘 넘어가는지 확인하기 위해서 회원가입 panel로 넘어감, mainpanel로 연결 예정
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
