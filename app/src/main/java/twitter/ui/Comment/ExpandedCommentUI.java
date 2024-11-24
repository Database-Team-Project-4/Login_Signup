package twitter.ui.Comment;

import twitter.ui.module.CustomScrollbar;
import twitter.ui.module.CustomSearchField;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpandedCommentUI extends JPanel {
    private int postId; // Post ID 저장

    public ExpandedCommentUI(int postId) {
        this.postId = postId; // postId 설정
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 상단 패널: 뒤로가기 버튼과 "댓글" 제목
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setPreferredSize(new Dimension(600, 50));

        JButton backButton = new JButton("<");
        backButton.setPreferredSize(new Dimension(50, 40));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(7, 7, 7));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 19));

        backButton.addActionListener(e -> {
            System.out.println("뒤로가기 버튼 클릭됨"); // 테스트용
        });

        JLabel titleLabel = new JLabel("댓글", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JPanel rightSpacer = new JPanel(); // 오른쪽 빈 공간
        rightSpacer.setPreferredSize(new Dimension(50, 40));
        rightSpacer.setBackground(new Color(7, 7, 7));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(rightSpacer, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널: 댓글 목록
        JPanel commentListPanel = new JPanel();
        commentListPanel.setLayout(new BoxLayout(commentListPanel, BoxLayout.Y_AXIS));
        commentListPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(commentListPanel);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // 하드코딩된 댓글 추가
        List<CommentUI> comments = getHardcodedComments();
        for (CommentUI comment : comments) {
            commentListPanel.add(comment);
        }

        /*
        // 데이터베이스에서 댓글을 로드하는 코드 (각주 처리)
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://your-database-url", "username", "password")) {
            List<CommentUI> dbComments = getCommentsFromDatabase(postId, connection);
            for (CommentUI comment : dbComments) {
                commentListPanel.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        */

        add(scrollPane, BorderLayout.CENTER);

        // 스크롤바 초기 위치를 맨 위로 설정
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

        // 하단 패널: 댓글 입력창
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(7, 7, 7));
        bottomPanel.setPreferredSize(new Dimension(600, 70)); // 높이 조정

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10); // 패딩 추가

        // 댓글 입력창
        CustomSearchField commentInputField = new CustomSearchField();
        commentInputField.setSearchText("댓글을 입력하세요...");
        commentInputField.setPreferredSize(new Dimension(500, 35)); // 입력창 길이 조정
        gbc.gridx = 0;
        gbc.weightx = 0.85; // 입력창의 너비 비율
        bottomPanel.add(commentInputField, gbc);

        // 전송 버튼
        JButton sendButton = new JButton("전송");
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                sendButton.setForeground(new Color(200, 200, 200)); // 호버 시 색상
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                sendButton.setForeground(Color.WHITE); // 기본 색상
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                sendButton.setForeground(new Color(150, 150, 150)); // 클릭 시 색상
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                sendButton.setForeground(new Color(200, 200, 200)); // 클릭 후 색상
            }
        });

        sendButton.addActionListener(e -> {
            String commentText = commentInputField.getSearchText();
            if (!commentText.isEmpty()) {
                System.out.println("댓글 입력됨: " + commentText); // 테스트용
                commentInputField.setSearchText(""); // 입력창 초기화
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 0.15; // 전송 버튼의 너비 비율
        bottomPanel.add(sendButton, gbc);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private List<CommentUI> getHardcodedComments() {
        List<CommentUI> comments = new ArrayList<>();
        comments.add(new CommentUI("User1", "user1@example.com", "이것은 첫 번째 댓글입니다.", 10));
        comments.add(new CommentUI("User2", "user2@example.com", "두 번째 댓글이에요!", 5));
        comments.add(new CommentUI("User3", "user3@example.com", "좋은 정보 감사합니다!", 8));
        comments.add(new CommentUI("User4", "user4@example.com", "정말 유용한 정보네요!", 15));
        comments.add(new CommentUI("User5", "user5@example.com", "이 글에 공감합니다.", 12));
        return comments;
    }

    /*
    // 데이터베이스에서 댓글을 가져오는 메서드
    private List<CommentUI> getCommentsFromDatabase(int postId, Connection connection) {
        List<CommentUI> comments = new ArrayList<>();
        String query = "SELECT u.name, u.email, c.content, c.likes " +
                       "FROM comments c JOIN users u ON c.user_id = u.user_id " +
                       "WHERE c.post_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String userName = rs.getString("name");
                String userEmail = rs.getString("email");
                String content = rs.getString("content");
                int likes = rs.getInt("likes");
                comments.add(new CommentUI(userName, userEmail, content, likes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
    */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Expanded Comment UI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 900);
            frame.add(new ExpandedCommentUI(1)); // Post ID를 전달
            frame.setVisible(true);
        });
    }
}
