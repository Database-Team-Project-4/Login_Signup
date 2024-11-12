package twitter.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class PostUI extends JPanel {
    public PostUI(int postId, Connection connection) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        setPreferredSize(new Dimension(300, 150));

        // 기본 변수 설정
        String userName = "Unknown";
        String userHandle = "@unknown";
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String contentText = "내용을 불러올 수 없습니다.";
        String postInfo = "00:00 · 날짜 없음 · 조회수 없음";
        int likes = 0;
        int comments = 0;
        int bookmarks = 0;

       
        // 데이터베이스에서 postId 기반 정보 가져오기
        String query = "SELECT u.name, u.handle, p.content, p.created_at, p.views, p.likes, p.comments, p.bookmarks " +
                       "FROM posts p JOIN users u ON p.user_id = u.user_id WHERE p.post_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("name");
                userHandle = rs.getString("handle");
                contentText = rs.getString("content");
                postInfo = String.format("%s · 조회 %d 회", rs.getString("created_at"), rs.getInt("views"));
                likes = rs.getInt("likes");
                comments = rs.getInt("comments");
                bookmarks = rs.getInt("bookmarks");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        
    }

        // 상단 패널 (작성자 정보)
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userHandle, profileIcon);
        add(userHeaderPanel, BorderLayout.NORTH);

        // 중간 패널 (게시글 본문 및 작성 정보)
        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
        PostFooterPanel postFooterPanel = new PostFooterPanel(likes, comments, bookmarks);
        add(postFooterPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height + userHeaderPanel.getPreferredSize().height + postFooterPanel.getPreferredSize().height + 30));
    }
     // 새로운 생성자 (테스트 데이터를 위한 생성자)
     public PostUI(String userName, String userHandle, String contentText, int likes, int comments, int bookmarks) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 임시 데이터 초기화
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String postInfo = "00:00 · 날짜 없음 · 조회수 없음";

        // 상단 패널 (작성자 정보)
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userHandle, profileIcon);
        add(userHeaderPanel, BorderLayout.NORTH);

        // 중간 패널 (게시글 본문 및 작성 정보)
        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
        PostFooterPanel postFooterPanel = new PostFooterPanel(likes, comments, bookmarks);
        add(postFooterPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height + userHeaderPanel.getPreferredSize().height + postFooterPanel.getPreferredSize().height + 30));
    }


    public static void main(String[] args) {
        // 데이터베이스 연결 및 임시 postId 값 설정
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://58.121.110.129:4472/twitter", "root", "ckwnsgk@1");
            int postId = 1;

            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("Twitter Post");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 400);
                frame.add(new PostUI(postId, connection));
                frame.pack();
                frame.setVisible(true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
