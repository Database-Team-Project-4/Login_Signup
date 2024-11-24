package twitter.ui.topic;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import twitter.model.User;
import twitter.service.GeminiService;
import twitter.service.userService;


public class Gemini_panel extends JPanel {

    private User currentUser; // 활용 대기ㅣㅣㅣㅣㅣㅣㅣ
    private Connection connection;
    private userService userService;

    
    private JTextField promptField; // 프롬프트 입력 필드
    private JPanel chatArea; // 채팅 표시 영역
    private List<ChatMessage> messages; // 채팅 메시지 목록
    private JButton runButton; // 실행 버튼
    private int chatAreaWidth = 150;
    
    public Gemini_panel(Connection connection, userService userService) {

        this.connection = connection;
        this.userService = userService;
        this.currentUser = userService.currentUser; 

        
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        messages = new ArrayList<>();

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setForeground(Color.GRAY);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);

        // 제목 라벨 생성 및 설정
        JLabel titleLabel = new JLabel("Gemini API", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));

        // 필터 버튼
        JLabel BlankLabel = new JLabel("--------", JLabel.CENTER);
        
        // 상단 패널 생성 (뒤로가기 버튼과 제목 라벨을 포함)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(BlankLabel, BorderLayout.EAST);
        

        // 상단 패널을 메인 패널의 북쪽에 추가
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

        promptField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (promptField.getText().equals("프롬프트를 입력하세요!")) {
                    promptField.setText("");
                    promptField.setForeground(Color.WHITE);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
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

        runButton = new JButton("실행"); // 버튼 텍스트 변경
        runButton.setBackground(Color.LIGHT_GRAY);
        runButton.setForeground(Color.BLACK);
        runButton.setFocusPainted(false);
        runButton.setMargin(new Insets(10, 15, 10, 15));


        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prompt = promptField.getText();


                Thread apiThread = new Thread(() -> {
                    try {
                        String response = GeminiService.callGeminiApi(prompt, null);

                        SwingUtilities.invokeLater(() -> {
                            addMessage("사용자", prompt); // 사용자 메시지 추가
                            addMessage("Gemini", response); // Gemini 응답 추가

                            promptField.setText("");
                            if (promptField.getText().isEmpty()) {
                                promptField.setForeground(Color.GRAY);
                                promptField.setText("프롬프트를 입력하세요!");
                            }
                        });
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            addMessage("오류", "API 호출 중 오류 발생: " + ex.getMessage()); // 오류 메시지 추가
                        });
                        ex.printStackTrace();
                    }
                });
                apiThread.start();
            }
        });


        promptFieldPanel.add(promptField, BorderLayout.CENTER);
        promptFieldPanel.add(runButtonPanel, BorderLayout.EAST);


        add(promptFieldPanel, BorderLayout.SOUTH);

        runButtonPanel.add(runButton);


    }

    public void setCurrentUser(User user) 
    {  // currentUser 설정 메서드
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
    
        if (currentUser != null) { // currentUser가 null이 아닌 경우에만 정보 출력
            String userInfo = String.format(" (ID: %d, Email: %s)", currentUser.getId(), currentUser.getEmail());
            label.setText(label.getText() + userInfo); // 기존 텍스트에 사용자 정보 추가
            System.out.println("Current User Information: " + userInfo); // 콘솔에 정보 출력 (디버깅용)
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