package twitter.ui.post;

import twitter.main.MainFrame;
import twitter.model.User;
import twitter.service.userService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private UserHeaderPanel userHeaderPanel; // 멤버 변수로 선언
    private String userName;
    private String userEmail;
    private String contentText;
    private int likes;
    private int comments;
    private int bookmarks;
    private int postId;
    private int userId;
    private MainFrame mainFrame;
    private String createdAt;

    public PostUI(int postId, Connection connection) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        setPreferredSize(new Dimension(300, 150));

        // 기본 변수 설정
        this.userName = "Unknown";
        this.userEmail = "123@abc";
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        this.contentText = "내용을 불러올 수 없습니다.";
        String postInfo = "00:00 · 날짜 없음";

        int likes = 0;
        int comments = 0;
        int bookmarks = 0;
        this.postId = postId;
        this.userId = -1;


        // 데이터베이스에서 postId 기반 정보 가져오기
        String query = "SELECT u.user_id, u.name, u.email, p.content, p.created_at, p.views, p.likes, p.comments, p.bookmarks " +
                       "FROM posts p JOIN users u ON p.user_id = u.user_id WHERE p.post_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
                userName = rs.getString("name");
                userEmail = rs.getString("email");
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
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, null, connection);
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



    public PostUI(MainFrame mainFrame, int postId, int userId, String userName, String userEmail, String contentText, int likes, int comments, int bookmarks, String created_at, userService userService, Connection connection) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.mainFrame = mainFrame;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.contentText = contentText;
        this.likes = likes;
        this.comments = comments;
        this.bookmarks = bookmarks;

        // 프로필 아이콘 설정
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String postInfo = String.format("%s", created_at);

        // 상단 패널 (작성자 정보)
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, userService, connection);
        add(userHeaderPanel, BorderLayout.NORTH);

        if (mainFrame != null && userId != -1) {
            userHeaderPanel.addProfileImageMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.showUserProfilePanel(userId);
                }
            });
        }

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
//     public PostUI(MainFrame mainFrame, String userName, String userEmail, String contentText, int likes, int comments, int bookmarks, String created_at) {
//        setLayout(new BorderLayout());
//        setBackground(Color.BLACK);
//
//         this.postId = postId;
//         this.userName = userName;
//         this.userEmail = userEmail;
//         this.contentText = contentText;
//         this.likes = likes;
//         this.comments = comments;
//         this.bookmarks = bookmarks;
//
//        // 임시 데이터 초기화
//        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
//        String postInfo = String.format("%s", created_at); // createdAt 포함
//
//        // 상단 패널 (작성자 정보)
//        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon);
//        add(userHeaderPanel, BorderLayout.NORTH);
//
//        // 중간 패널 (게시글 본문 및 작성 정보)
//        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
//        add(contentPanel, BorderLayout.CENTER);
//
//        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
//        PostFooterPanel postFooterPanel = new PostFooterPanel(likes, comments, bookmarks);
//        add(postFooterPanel, BorderLayout.SOUTH);
//
//        // 전체 크기 조정
//        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height + userHeaderPanel.getPreferredSize().height + postFooterPanel.getPreferredSize().height + 30));

//}
    public PostUI(int postId, String userName, String userEmail, String contentText,
                  int likes, int comments, int bookmarks, String createdAt, userService userService, Connection connection) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.postId = postId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.contentText = contentText;
        this.likes = likes;
        this.comments = comments;
        this.bookmarks = bookmarks;
        this.createdAt = createdAt;
        this.userId = -1; // 기본값 설정
        this.mainFrame = null; // 기본값 설정

        // 프로필 아이콘 설정
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String postInfo = String.format("%s", createdAt);

        // 상단 패널 (작성자 정보)
        userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, userService, connection);
        add(userHeaderPanel, BorderLayout.NORTH);

        // 중간 패널 (게시글 본문 및 작성 정보)
        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
        PostFooterPanel postFooterPanel = new PostFooterPanel(likes, comments, bookmarks);
        add(postFooterPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400,
                contentPanel.getPreferredSize().height +
                        userHeaderPanel.getPreferredSize().height +
                        postFooterPanel.getPreferredSize().height + 30));
    }
    public PostUI(String userName, String userEmail, String contentText,
                  int likes, int comments, int bookmarks, String createdAt) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.postId = -1; // 게시물 ID를 알 수 없으므로 기본값 설정
        this.userId = -1; // 사용자 ID를 알 수 없으므로 기본값 설정
        this.userName = userName;
        this.userEmail = userEmail;
        this.contentText = contentText;
        this.likes = likes;
        this.comments = comments;
        this.bookmarks = bookmarks;
        this.createdAt = createdAt;
        this.mainFrame = null; // 기본값 설정

        // 프로필 아이콘 설정
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String postInfo = String.format("%s", createdAt);

        // 상단 패널 (작성자 정보)
        userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon,userId,null, null);
        add(userHeaderPanel, BorderLayout.NORTH);

        // 중간 패널 (게시글 본문 및 작성 정보)
        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
        PostFooterPanel postFooterPanel = new PostFooterPanel(likes, comments, bookmarks);
        add(postFooterPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400,
                contentPanel.getPreferredSize().height +
                        userHeaderPanel.getPreferredSize().height +
                        postFooterPanel.getPreferredSize().height + 30));
    }



    public PostUI(String gachon, String s, String s1, int i, int i1, int i2) {
    }

    public PostUI(String user1, String s, String thisIsABookmarkedPost, int likes, int comments, int bookmarks, String date, boolean b) {
    }


    public String getUserName() {
        return userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public int getComments() {
        return comments;
    }
    public int getBookmarks() {
        return bookmarks;
    }
    public String getContentText() {
        return contentText;
    }
    public int getLikes() {
        return likes;
    }
    public int getPostId() {return postId;}

    public void addProfileImageMouseListener(MouseAdapter listener) {
        userHeaderPanel.addProfileImageMouseListener(listener);
    }
}
