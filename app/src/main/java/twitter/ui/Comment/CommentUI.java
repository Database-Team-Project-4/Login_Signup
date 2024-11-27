package twitter.ui.Comment;

import twitter.ui.post.UserHeaderPanel;
import javax.swing.*;
import java.awt.*;

public class CommentUI extends JPanel {
    private UserHeaderPanel userHeaderPanel;
    private String userName;
    private String userEmail;
    private String contentText;

    public CommentUI(String userName, String userEmail, String contentText) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 80)); // 세로 길이를 줄이기 위해 기본 크기 조정

        this.userName = userName;
        this.userEmail = userEmail;
        this.contentText = contentText;

        // 상단 패널 (작성자 정보)
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail, new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png")));
        Dimension originalSize = userHeaderPanel.getPreferredSize();
        userHeaderPanel.setPreferredSize(new Dimension((int) (originalSize.width * 1.0), (int) (originalSize.height * 1.0))); // 크기를 유지
        add(userHeaderPanel, BorderLayout.NORTH);

        // 중간 패널 (댓글 내용)
        CommentContentPanel contentPanel = new CommentContentPanel(contentText);
        contentPanel.setPreferredSize(new Dimension(originalSize.width, (int) (originalSize.height * 0.6))); // 높이를 1.2배로 설정
        add(contentPanel, BorderLayout.CENTER);

        // 전체 크기 조정 (하단 패널 제거로 인해 계산 간소화)
        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height +
                userHeaderPanel.getPreferredSize().height + 20)); // 추가 여백 포함
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getContentText() {
        return contentText;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Comment UI Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            // 하드코딩된 데이터로 CommentUI 생성
            CommentUI commentUI = new CommentUI(
                    "Test User",
                    "test@example.com",
                    "This is a test comment. It is meant to check the UI design."
            );

            frame.add(commentUI);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
