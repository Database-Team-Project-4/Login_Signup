package twitter.ui.post;

import twitter.main.MainFrame;
import twitter.model.User;
import twitter.service.postService;
import twitter.service.imgService;
import twitter.service.userService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

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
    private Runnable refreshCallback;
    private postService postService;
    private List<byte[]> images; // 이미지 리스트 추가

    public void addRefreshCallback(Runnable refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public PostUI(MainFrame mainFrame, int postId, int userId, String userName, String userEmail, String contentText, int likes, int comments, int bookmarks, String created_at, userService userService, postService postService, Connection connection) {
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
        this.createdAt = created_at;
        this.postService = postService;
        // 이미지 가져오기
        this.images = getImagesForPost(connection, postId);

        // 프로필 아이콘 설정
        ImageIcon profileIcon = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
        String postInfo = String.format("%s", created_at);


        // 상단 패널 (작성자 정보)
        UserHeaderPanel userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, userService, connection, refreshCallback);
        add(userHeaderPanel, BorderLayout.NORTH);

        // 팔로우 버튼 숨김 처리 (프로필 화면인 경우)
        if (mainFrame != null && mainFrame.isProfileView()) {
            userHeaderPanel.hideFollowButton();
        }

        add(userHeaderPanel, BorderLayout.NORTH);

        if (mainFrame != null && userId != -1) {
            userHeaderPanel.addProfileImageMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    mainFrame.showUserProfilePanel(userId);
                }
            });
        }

        List<byte[]> images = null;
        try {
            images = postService.getImagesByPostId(connection, postId); // 게시물의 이미지 가져오기
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 중단 패널
        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks, images);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
        PostFooterPanel postFooterPanel = new PostFooterPanel(postId, userId , userService, connection);
        add(postFooterPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400, contentPanel.getPreferredSize().height + userHeaderPanel.getPreferredSize().height + postFooterPanel.getPreferredSize().height + 30));
    }


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
        userHeaderPanel = new UserHeaderPanel(userName, userEmail, profileIcon, userId, userService, connection, refreshCallback);
        add(userHeaderPanel, BorderLayout.NORTH);


        List<byte[]> images = null;
        try {
            images = postService.getImagesByPostId(connection, postId); // 게시물의 이미지 가져오기
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 중간 패널 (게시글 본문 및 작성 정보)
        //PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks);
        //add(contentPanel, BorderLayout.CENTER);
        PostContentPanel contentPanel = new PostContentPanel(contentText, postInfo, likes, comments, bookmarks, images);
        add(contentPanel, BorderLayout.CENTER);

        // 하단 패널 (좋아요, 댓글, 북마크 버튼)
        PostFooterPanel postFooterPanel = new PostFooterPanel(postId,userId, userService, connection);
        add(postFooterPanel, BorderLayout.SOUTH);

        // 전체 크기 조정
        setPreferredSize(new Dimension(400,
                //contentPanel.getPreferredSize().height +
                        userHeaderPanel.getPreferredSize().height +
                        postFooterPanel.getPreferredSize().height + 30));
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
    public int getUserId() {return userId;}
    public String getCreatedAt() {return createdAt;}


    public void addProfileImageMouseListener(MouseAdapter listener) {
        userHeaderPanel.addProfileImageMouseListener(listener);
    }

    // 이미지 데이터 가져오는 메서드 추가
    private List<byte[]> getImagesForPost(Connection connection, int postId) {
        List<byte[]> images = new ArrayList<>();
        try {
            imgService imgService = new imgService();
            images = imgService.retrieveImagesByPostId(connection, postId);
        } catch (SQLException e) {
            System.out.println("이미지 로딩 중 오류 발생: " + e.getMessage());
        }
        return images;
    }
}
