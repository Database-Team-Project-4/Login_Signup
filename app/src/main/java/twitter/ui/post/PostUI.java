package twitter.ui.post;

import twitter.main.MainFrame;
import twitter.model.User;
import twitter.service.userService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PostUI extends JPanel {
    private UserHeaderPanel userHeaderPanel;
    private PostContentPanel contentPanel;
    private PostFooterPanel footerPanel;

    private String userName;
    private String userEmail;
    private String contentText;
    private int likes;
    private int comments;
    private int bookmarks;
    private int postId;
    private int userId;
    private String createdAt;

    private MainFrame mainFrame;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public PostUI(MainFrame mainFrame, int postId, int userId, String userName, String userEmail, String contentText,
                  int likes, int comments, int bookmarks, String createdAt, userService userService, Connection connection) {
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
        this.createdAt = createdAt;

        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String postInfo = String.format("%s", createdAt);

        userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, userService, connection);
        add(userHeaderPanel, BorderLayout.NORTH);

        // 프로필 화면에서 팔로우 버튼 숨기기
        if (mainFrame != null && mainFrame.isProfileView()) {
            userHeaderPanel.hideFollowButton();
        }

        if (mainFrame != null && userId != -1) {
            userHeaderPanel.addProfileImageMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.showUserProfilePanel(userId);
                }
            });
        }

        contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        add(contentPanel, BorderLayout.CENTER);

        footerPanel = new PostFooterPanel(likes, comments, bookmarks, postId, userId, userService, connection);
        add(footerPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height + userHeaderPanel.getPreferredSize().height + footerPanel.getPreferredSize().height + 30));
    }

    public PostUI(int postId, Connection connection) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        this.postId = postId;
        this.userName = "Unknown";
        this.userEmail = "unknown@domain.com";
        this.contentText = "내용을 불러올 수 없습니다.";
        this.createdAt = "알 수 없음";
        this.userId = -1;
        this.likes = 0;
        this.comments = 0;
        this.bookmarks = 0;

        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));

        // 데이터베이스에서 게시글 정보 로드
        String query = "SELECT u.user_id, u.name, u.email, p.content, p.created_at, p.likes, p.comments, p.bookmarks " +
                       "FROM posts p JOIN users u ON p.user_id = u.user_id WHERE p.post_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
                userName = rs.getString("name");
                userEmail = rs.getString("email");
                contentText = rs.getString("content");
                createdAt = rs.getString("created_at");
                likes = rs.getInt("likes");
                comments = rs.getInt("comments");
                bookmarks = rs.getInt("bookmarks");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String postInfo = String.format("%s", createdAt);

        userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, null, connection);
        add(userHeaderPanel, BorderLayout.NORTH);

        contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        add(contentPanel, BorderLayout.CENTER);

        footerPanel = new PostFooterPanel(likes, comments, bookmarks, postId, userId, null, connection);
        add(footerPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height + userHeaderPanel.getPreferredSize().height + footerPanel.getPreferredSize().height + 30));
    }

    // 좋아요 수 업데이트 메서드
    public void setLikes(int likes) {
        int oldLikes = this.likes;
        this.likes = likes;
        if (footerPanel != null) {
            footerPanel.setLikes(likes);
        }
        repaint();
        firePropertyChange("likeCount", oldLikes, likes);
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    // Getter methods
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

    public int getComments() {
        return comments;
    }

    public int getBookmarks() {
        return bookmarks;
    }

    public int getPostId() {
        return postId;
    }

    public void addProfileImageMouseListener(MouseAdapter listener) {
        userHeaderPanel.addProfileImageMouseListener(listener);
    }
}
