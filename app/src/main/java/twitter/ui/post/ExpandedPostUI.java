package twitter.ui.post;

import java.awt.*;
import javax.swing.*;

public class ExpandedPostUI extends JPanel {

    public ExpandedPostUI(String userName, String userEmail, String contentText, int likes, int comments, int bookmarks, String createdAt) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 상단 뒤로가기 버튼 패널 추가
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BorderLayout());
        backPanel.setBackground(new Color(7,7,7)); // 배경색 검은색으로 설정
        backPanel.setPreferredSize(new Dimension(600, 50));
        backPanel.setOpaque(true); // 투명도 해제하여 배경색 적용

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setPreferredSize(new Dimension(50, 40)); // 버튼 크기 조정
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(7,7,7));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 19)); // 폰트 크기와 스타일 설정

        // 뒤로가기 버튼 액션 추가
        backButton.addActionListener(e -> {
            System.out.println("뒤로가기 버튼 클릭됨");
        });

        // "게시" 텍스트
        JLabel titleLabel = new JLabel("게시", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15)); // 텍스트 크기와 스타일 설정

        // 왼쪽 빈 공간 (뒤로가기 버튼 오른쪽 여백)
        JPanel leftSpacePanel = new JPanel();
        leftSpacePanel.setPreferredSize(new Dimension(50, 40)); // 뒤로가기 버튼 크기만큼 여백
        leftSpacePanel.setBackground(new Color(7,7,7));

        // 상단바 구성
        backPanel.add(backButton, BorderLayout.WEST);
        backPanel.add(titleLabel, BorderLayout.CENTER);
        backPanel.add(leftSpacePanel, BorderLayout.EAST);

        add(backPanel, BorderLayout.NORTH);

        // 기존 PostUI를 확대하여 중앙에 배치
        PostUI postUI = new PostUI(userName, userEmail, contentText, likes, comments, bookmarks, createdAt);

        // 프로필 사진과 텍스트의 크기를 조정
        postUI.setFont(new Font("SansSerif", Font.PLAIN, 40)); // 기본 폰트 크기 증가
        postUI.setPreferredSize(new Dimension(600, 800)); // 크기 조정

        add(postUI, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Expanded Post - Design Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 900);

            // 임시 데이터로 ExpandedPostUI 테스트
            String userName = "John Doe";
            String userEmail = "john.doe@example.com";
            String contentText = "This is a detailed post content. It is designed to span multiple lines and test the layout.";
            int likes = 123;
            int comments = 45;
            int bookmarks = 67;
            String createdAt = "2024-11-19 14:35";

            ExpandedPostUI expandedPostUI = new ExpandedPostUI(userName, userEmail, contentText, likes, comments, bookmarks, createdAt);
            frame.add(expandedPostUI);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
