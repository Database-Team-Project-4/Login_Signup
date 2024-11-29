package twitter.ui.topic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import twitter.main.MainFrame;
import twitter.model.User;
import twitter.Controller.GeminiController;
import twitter.Controller.userController;


public class Gemini_panel extends JPanel {

    private User currentUser;
    private Connection connection;
    private userController userController;

    private JTextField promptField;
    private JPanel chatArea;
    private List<ChatMessage> messages;
    private JButton runButton;
    private int chatAreaWidth = 160; // 채팅 영역 너비 증가


    public Gemini_panel(MainFrame mainframe, Connection connection, userController userController) {
        this.connection = connection;
        this.userController = userController;
        this.currentUser = userController.getCurrentUser();

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        messages = new ArrayList<>();

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setForeground(Color.GRAY);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);

        backButton.addActionListener(e -> mainframe.showTwitterMainUiPanel());

        // 제목 라벨 생성 및 설정
        JLabel titleLabel = new JLabel("Gemini API", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));

        // 상단 패널 생성 (뒤로가기 버튼과 제목 라벨을 포함)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);


        add(topPanel, BorderLayout.NORTH);

        // 채팅 영역 설정
        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(chatAreaWidth, scrollPane.getPreferredSize().height));
        scrollPane.setBorder(new LineBorder(Color.BLACK, 2));
        add(scrollPane, BorderLayout.CENTER);

        // 프롬프트 입력 필드 설정
        promptField = new JTextField("프롬프트를 입력하세요!");
        promptField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        promptField.setForeground(Color.GRAY);
        promptField.setBackground(new Color(30, 30, 30));
        promptField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(66, 66, 66), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        promptField.setCaretColor(Color.WHITE);

        promptField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (promptField.getText().equals("프롬프트를 입력하세요!")) {
                    promptField.setText("");
                    promptField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent evt) {
                if (promptField.getText().isEmpty()) {
                    promptField.setForeground(Color.GRAY);
                    promptField.setText("프롬프트를 입력하세요!");
                }
            }
        });

        // 실행 버튼 설정
        JPanel promptFieldPanel = new JPanel(new BorderLayout());
        promptFieldPanel.setBackground(Color.BLACK);
        promptFieldPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel runButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        runButtonPanel.setBackground(Color.BLACK);

        runButton = new JButton("실행");
        runButton.setBackground(Color.LIGHT_GRAY);
        runButton.setForeground(Color.BLACK);
        runButton.setFocusPainted(false);
        runButton.setMargin(new Insets(10, 15, 10, 15));

        runButton.addActionListener(e -> {
            if (userController.getCurrentUser() == null) {
                JOptionPane.showMessageDialog(Gemini_panel.this, "Gemini API를 사용하려면 로그인이 필요합니다.", "로그인 필요", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String prompt = promptField.getText();
            
            String information = currentUser.getName(); // currentUser의 프롬프트 정보 던져주기.
            //System.out.println("======================================================" + information);
            Thread apiThread = new Thread(() -> {
                try {
                    String response = GeminiController.callGeminiApi(prompt, information, null); // null은 추가적인 매개변수를 위한 자리입니다. 필요에 따라 수정하세요.
                
                    SwingUtilities.invokeLater(() -> {
                        addMessage("사용자", prompt);
                        addMessage("Gemini", response);
                        promptField.setText("");
                        promptField.setForeground(Color.GRAY);
                        promptField.setText("프롬프트를 입력하세요!");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        addMessage("오류", "API 호출 중 오류 발생: " + ex.getMessage());
                        ex.printStackTrace();
                    });
                }
            });
            apiThread.start();
        });


        promptFieldPanel.add(promptField, BorderLayout.CENTER);
        runButtonPanel.add(runButton);
        promptFieldPanel.add(runButtonPanel, BorderLayout.EAST);
        add(promptFieldPanel, BorderLayout.SOUTH);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void addMessage(String sender, String text) {
        ChatMessage message = new ChatMessage(sender, text);
        messages.add(message);

        JPanel messagePanel = new JPanel(new FlowLayout(sender.equals("사용자") ? FlowLayout.RIGHT : FlowLayout.LEFT));
        messagePanel.setOpaque(false);

        JLabel label = new JLabel("<html><div style='width:" + (chatAreaWidth - 40) + "px; word-wrap: break-word;'>" + text + "</div></html>");
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        label.setForeground(Color.WHITE);
        label.setBackground(sender.equals("사용자") ? new Color(0, 120, 215) : new Color(50, 50, 50));

        if (currentUser != null) {
            String userInfo = String.format(" (ID: %d, Email: %s)", currentUser.getId(), currentUser.getEmail());
            label.setText(label.getText() + userInfo);
            System.out.println("Current User Information: " + userInfo);
        }

        messagePanel.add(label);
        messagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height + 10));

        chatArea.add(messagePanel);
        chatArea.revalidate();
        chatArea.repaint();

        JScrollBar vertical = ((JScrollPane) chatArea.getParent().getParent()).getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }


    private static class ChatMessage {
        String sender;
        String text;

        public ChatMessage(String sender, String text) {
            this.sender = sender;
            this.text = text;
        }
    }
}