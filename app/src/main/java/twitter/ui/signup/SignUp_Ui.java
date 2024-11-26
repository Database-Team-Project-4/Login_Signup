package twitter.ui.signup;

import twitter.service.userService;
import twitter.main.MainFrame;
import twitter.model.User;

import javax.swing.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;


public class SignUp_Ui extends JPanel {
    private Connection connection;
    private userService userService;
    private final ImageIcon xLogoIcon = loadIcon("/TwitterIcons/X_logo.png");

    // 기존 기능 유지하며 아이콘 로드 기능
    private ImageIcon loadIcon(String path) {
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }
    public SignUp_Ui(MainFrame mainframe, Connection connection, userService userService) {
        this.connection = connection;
        this.userService = userService;

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

        // Email 입력 필드
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(emailLabel, gbc);

        JTextField emailField = new JTextField(15);
        emailField.setBorder(BorderFactory.createEmptyBorder());
        emailField.setBackground(Color.BLACK);
        emailField.setForeground(Color.WHITE);
        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        // Password 입력 필드
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBorder(BorderFactory.createEmptyBorder());
        passwordField.setBackground(Color.BLACK);
        passwordField.setForeground(Color.WHITE);
        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        // Name 입력 필드
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(15);
        nameField.setBorder(BorderFactory.createEmptyBorder());
        nameField.setBackground(Color.BLACK);
        nameField.setForeground(Color.WHITE);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        // Address 입력 필드
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(addressLabel, gbc);

        JTextField addressField = new JTextField(15);
        addressField.setBorder(BorderFactory.createEmptyBorder());
        addressField.setBackground(Color.BLACK);
        addressField.setForeground(Color.WHITE);
        gbc.gridx = 1;
        inputPanel.add(addressField, gbc);

        // 회원가입 버튼 생성
        JButton signupButton = new JButton("Sign Up");
        signupButton.setFocusPainted(false);
        signupButton.setBackground(Color.WHITE);
        signupButton.setForeground(Color.BLACK);
        signupButton.setBorderPainted(false);
        signupButton.setOpaque(true);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(signupButton, gbc);

        // 회원가입 버튼 클릭 이벤트 처리
        signupButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String address = addressField.getText();

            // 필드 유효성 검사
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = new User(email, name, password, address);

            try {
                userService.signup(connection, newUser);
                JOptionPane.showMessageDialog(mainframe, "Sign up successful!");
                mainframe.showLoginPanel(); // 로그인 이후 홈화면으로 이동 !! (임시인거 같군여)
            } catch (SQLException ex) {
                // 에러 메시지 출력
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during sign up: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}
