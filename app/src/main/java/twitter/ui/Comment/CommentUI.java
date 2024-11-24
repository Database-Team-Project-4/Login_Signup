package twitter.ui.Comment;

import twitter.ui.post.UserHeaderPanel;

import javax.swing.*;
import java.awt.*;

public class CommentUI extends JPanel {
    private UserHeaderPanel userHeaderPanel;
    private String userName;
    private String userEmail;
    private String contentText;
    private int likes;

    public CommentUI(String userName, String userEmail, String contentText, int likes) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 150));

        this.userName = userName;
        this.userEmail = userEmail;
        this.contentText = contentText;
        this.likes = likes;

        // 상단 패널 (작성자 정보)
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail, new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png")));
        add(userHeaderPanel, BorderLayout.NORTH);

        // 중간 패널 (댓글 내용)
        CommentContentPanel contentPanel = new CommentContentPanel(contentText);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요 버튼만 포함)
        CommentFooterPanel footerPanel = new CommentFooterPanel(likes);
        add(footerPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height +
                userHeaderPanel.getPreferredSize().height + footerPanel.getPreferredSize().height + 30));
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
                    "This is a test comment. It is meant to check the UI design.",
                    42
            );

            frame.add(commentUI);

            frame.pack();
            frame.setVisible(true);
        });
    }
}

/**public CommentUI(int commentId, Connection connection) {
    setLayout(new BorderLayout());
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(300, 150));

    this.commentId = commentId;
    this.userId = -1;
    this.userName = "Unknown";
    this.userEmail = "unknown@abc.com";
    this.contentText = "댓글 내용을 불러올 수 없습니다.";
    this.likes = 0;

    // 데이터베이스에서 commentId 기반 정보 가져오기
    String query = "SELECT u.user_id, u.name, u.email, c.content, c.created_at, c.likes " +
            "FROM comments c JOIN users u ON c.user_id = u.user_id WHERE c.comment_id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setInt(1, commentId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            userId = rs.getInt("user_id");
            userName = rs.getString("name");
            userEmail = rs.getString("email");
            contentText = rs.getString("content");
            createdAt = rs.getString("created_at");
            likes = rs.getInt("likes");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // 상단 패널 (작성자 정보)
    UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail,
            new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png")));
    add(userHeaderPanel, BorderLayout.NORTH);

    // 중간 패널 (댓글 내용)
    CommentContentPanel contentPanel = new CommentContentPanel(contentText);
    add(contentPanel, BorderLayout.CENTER);

    // 하단 패널 (좋아요 버튼만 포함)
    CommentFooterPanel footerPanel = new CommentFooterPanel(likes);
    add(footerPanel, BorderLayout.SOUTH);

    // 전체 크기 조정
    setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height +
            userHeaderPanel.getPreferredSize().height + footerPanel.getPreferredSize().height + 30));
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

public int getLikes() {
    return likes;
}

public int getCommentId() {
    return commentId;
}

 public static void main(String[] args) {
 // 데이터베이스 연결 및 임시 commentId 값 설정
 try {
 Class.forName("com.mysql.cj.jdbc.Driver");
 Connection connection = DriverManager.getConnection("jdbc:mysql://58.121.110.129:4472/twitter", "root", "ckwnsgk@1");

 );

 int commentId = 1; // 확인하고 싶은 댓글 ID

 SwingUtilities.invokeLater(() -> {
 JFrame frame = new JFrame("Comment UI Test");
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.setSize(400, 400);

 CommentUI commentUI = new CommentUI(commentId, connection);
 frame.add(commentUI);

 frame.pack();
 frame.setVisible(true);
 });

 } catch (Exception e) {
 e.printStackTrace();
 }
 }
**/