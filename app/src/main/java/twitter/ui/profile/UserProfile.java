package twitter.ui.profile;

import twitter.main.MainFrame;
import twitter.service.postService;
import twitter.service.userService;
import twitter.service.followService;
import twitter.ui.Comment.CommentUI;
import twitter.ui.module.custombutton.RoundedRectangleButton;
import twitter.ui.post.PostUI;
import twitter.ui.module.CustomScrollbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import twitter.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//기본적인 프로필화면입니다. 다른 사용자의 프로필임을 상정하고 만들어 팔로우 버튼이 있으며 팔로우 버튼만 삭제하면 바로 나의 프로필로 사용할 수 있습니다.
//추가로 나의 프로필에서 팔로우 버튼 위치에 사용자 정보 수정 버튼 등을 생성할수도 있습니다.
public class UserProfile extends JPanel {
    private int userId; //현재 프로필의 id
    private int currentUserId; //접속중인 사용자의 id
    private MainFrame mainframe;
    private JPanel mainPanel;
    private JButton postButton;
    private JButton replyButton;
    private JPanel postUnderline;
    private JPanel replyUnderline;
    private JPanel profilePic;
    private String userNameText;
    private String userHandleText;
    private ImageIcon profileImageIcon;
    private Connection connection; // 데이터베이스 연결 객체
    private userService userService;
    private postService postService;
    private followService followService;

    public UserProfile(MainFrame mainframe, Connection connection, userService userService, postService postService, int userId) {
        this.mainframe = mainframe;
        this.userId = userId;
        this.connection = connection;
        this.userService = userService;
        this.postService = postService;

        // 현재 로그인한 사용자 ID를 userService에서 가져옴
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            this.currentUserId = currentUser.getId(); // 로그인한 사용자의 ID 설정
        } else {
            this.currentUserId = -1; // 로그인이 되어 있지 않다면 기본값
        }

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        loadUserInfo();

        // 상단부 전체를 담을 패널 생성 (topBar와 centralPanel 포함)
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(Color.BLACK);

        // 상단바 추가
        JPanel topBar = createTopBar();
        topContainer.add(topBar);

        // 사용자 헤더 및 탭 버튼 패널 추가
        JPanel centralPanel = createCentralPanel();
        topContainer.add(centralPanel);

        // topContainer를 상단에 추가
        add(topContainer, BorderLayout.NORTH);

        // 포스트 콘텐츠 스크롤 영역 추가
        JScrollPane postScrollPane = createPostContentPanel();
        add(postScrollPane, BorderLayout.CENTER);
    }

    private void loadUserInfo() {
        try {
            String query = "SELECT name, email FROM Users WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userNameText = rs.getString("name");
                userHandleText = rs.getString("email"); // 이메일을 핸들로 사용
                // 프로필 이미지 로드 로직 추가 가능
            } else {
                userNameText = "Unknown";
                userHandleText = "unknown@example.com";
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            userNameText = "Unknown";
            userHandleText = "unknown@example.com";
        }
    }


    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 122, 255)); // 상단바 배경색 설정
        topBar.setPreferredSize(new Dimension(getWidth(), 40)); // TopBar의 높이를 줄임

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("SansSerif", Font.BOLD, 18)); // 크기 조정

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 뒤로 가기 버튼 클릭 시 이전 화면으로 돌아가는 로직 추가
                mainframe.showTwitterMainUiPanel();  // 이전 화면으로 돌아가는 메서드를 호출
            }
        });

        // TopBar 구성
        topBar.add(backButton, BorderLayout.WEST);


        return topBar;
    }


// 버튼 기본 설정

private JButton createFollowButton() {
    JButton followButton = new RoundedRectangleButton("팔로우");
    followButton.setPreferredSize(new Dimension(70, 30)); // 크기 조정
    followButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
    followButton.setBackground(Color.WHITE);
    followButton.setForeground(Color.BLACK);
    followButton.setFocusPainted(false);

    // 자신의 프로필인 경우 버튼을 비활성화
    if (userId == currentUserId) {
        followButton.setEnabled(false); // 버튼을 비활성화
        followButton.setVisible(false); // 버튼을 안보이게
    } else {
        // 호버 및 클릭 이벤트
        followButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                followButton.setBackground(Color.LIGHT_GRAY); // 호버 시 색상
                followButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                followButton.setBackground(Color.WHITE); // 기본 색상
                followButton.setForeground(Color.BLACK);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                followButton.setText(followButton.getText().equals("팔로우") ? "팔로우 취소" : "팔로우");
            }
        });
    }
    return followButton;
}

    private JPanel createCentralPanel() {
        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setBackground(Color.BLACK);

        // UserHeaderPanel 추가
        JPanel userHeaderPanel = createUserHeaderPanel();
        centralPanel.add(userHeaderPanel);

        // 탭 패널을 더 위로 올리기
        JPanel tabPanel = createTabPanel();
        centralPanel.add(Box.createVerticalStrut(-10)); // 간격 줄이기
        centralPanel.add(tabPanel);

        return centralPanel;
    }

    private JPanel createUserHeaderPanel() {
        followService followService = new followService();
        JPanel userHeaderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 프로필 이미지의 세로 중간 지점을 기준으로 위쪽 배경색 설정
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 122, 255)); // 위쪽 배경색
                int splitY = getHeight() / 2 - 60; // 프로필 이미지 중앙 기준 계산
                g2d.fillRect(0, 0, getWidth(), splitY);

                g2d.setColor(new Color(7, 7, 7)); // 아래쪽 배경색
                g2d.fillRect(0, splitY, getWidth(), getHeight() - splitY);
            }
        };

        userHeaderPanel.setLayout(null); // 절대 위치 사용
        userHeaderPanel.setPreferredSize(new Dimension(getWidth(), 200)); // 높이 조정

        // 프로필 이미지
        ImageIcon profileIcon = loadIcon("/TwitterIcons/icondef.png");
        if (profileIcon != null) {
            Image img = profileIcon.getImage();
            Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            profileIcon = new ImageIcon(scaledImg);
        }
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBounds(20, 10, 80, 80); // 프로필 이미지를 상단 중앙에 배치
        userHeaderPanel.add(profileImage);

        // 사용자 이름
        JLabel userName = new JLabel(userNameText);
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("SansSerif", Font.BOLD, 16));
        userName.setBounds(25, 100, 200, 20);
        userHeaderPanel.add(userName);


        // 사용자 핸들
        JLabel userHandle = new JLabel(userHandleText);
        userHandle.setForeground(Color.GRAY);
        userHandle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userHandle.setBounds(25, 125, 200, 20);
        userHeaderPanel.add(userHandle);

        // 팔로우/팔로잉 버튼
        JPanel followInfoPanel = new JPanel();
        followInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0)); // 버튼 간 간격을 5px로 설정
        followInfoPanel.setBackground(new Color(7, 7, 7));
        followInfoPanel.setBounds(8, 155, 200, 30); // 핸들 아래 위치

        List<User> followingList = followService.getFollowing(connection, userService.getCurrentUser());
        List<User> followerList = followService.getFollowers(connection, userService.getCurrentUser());

        // 자신의 팔로잉 수 확인 라벨
        JLabel followingLabel = new JLabel("팔로잉 " + followingList.size());
        followingLabel.setForeground(Color.GRAY);
        followingLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        followingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 자신의 팔로워 수 확인 라벨
        JLabel followerLabel = new JLabel("팔로워 " + followerList.size());
        followerLabel.setForeground(Color.GRAY);
        followerLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        followerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 팔로우 버튼
        JButton followButton = createFollowButton();
        followButton.setBounds(340, 55, 80, 30);  // 패널 오른쪽 경계 근처에 위치


        // 버튼 상하 패딩 및 오른쪽 유격 추가
        JPanel paddedFollowButton = new JPanel(new GridBagLayout());
        paddedFollowButton.setBackground(new Color(0, 122, 255));
        paddedFollowButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // 오른쪽에 10px 유격 추가
        paddedFollowButton.add(followButton);

        followInfoPanel.add(followingLabel);
        followInfoPanel.add(followerLabel);
        userHeaderPanel.add(followInfoPanel);
        userHeaderPanel.add(followButton);


        return userHeaderPanel;
    }




    private JButton createHoverableButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.GRAY);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 11));

        // 호버링 효과 추가
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.GRAY);
            }
        });

        return button;
    }

    private void updateTabButtonColors() {
        if (postUnderline.getBackground().equals(new Color(0, 122, 255))) {
            postButton.setForeground(Color.WHITE);
            replyButton.setForeground(Color.GRAY);
        } else if (replyUnderline.getBackground().equals(new Color(0, 122, 255))) {
            replyButton.setForeground(Color.WHITE);
            postButton.setForeground(Color.GRAY);
        }
    }

    private JPanel createTabPanel() {
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS)); // 수직으로 정렬
        tabPanel.setBackground(new Color(7,7,7));
        tabPanel.setPreferredSize(new Dimension(getWidth(), 35));

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 130, 0)); // 간격 조정
        buttonPanel.setBackground(new Color(7,7,7));

        // 게시물 버튼
        postButton = createTabButton("게시물");
        postUnderline = createDynamicUnderlinePanel(postButton);
        postUnderline.setBackground(new Color(0, 122, 255)); // 기본 활성 밑줄 색상
        postButton.setForeground(Color.WHITE); // 기본 활성 상태

// 답글 버튼
        replyButton = createTabButton("답글");
        replyUnderline = createDynamicUnderlinePanel(replyButton);
        replyUnderline.setBackground(new Color(7, 7, 7)); // 기본 비활성 밑줄 색상
        replyButton.setForeground(Color.GRAY); // 기본 비활성 상태



        // 버튼 이벤트
        postButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updatePostContent("posts");
                postUnderline.setBackground(new Color(0, 122, 255));
                replyUnderline.setBackground(new Color(7,7,7));
                updateTabButtonColors();
            }
        });

        replyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateCommentContent("replies");
                postUnderline.setBackground(new Color(7,7,7));
                replyUnderline.setBackground(new Color(0, 122, 255));
                updateTabButtonColors();
            }
        });

        // 버튼과 밑줄을 그룹화하여 정확히 정렬
        JPanel postButtonGroup = new JPanel(new BorderLayout());
        postButtonGroup.setBackground(new Color(7,7,7));
        postButtonGroup.add(postButton, BorderLayout.CENTER);
        postButtonGroup.add(postUnderline, BorderLayout.SOUTH);

        JPanel replyButtonGroup = new JPanel(new BorderLayout());
        replyButtonGroup.setBackground(new Color(7,7,7));
        replyButtonGroup.add(replyButton, BorderLayout.CENTER);
        replyButtonGroup.add(replyUnderline, BorderLayout.SOUTH);

        buttonPanel.add(postButtonGroup);
        buttonPanel.add(replyButtonGroup);

        tabPanel.add(buttonPanel);

        return tabPanel;
    }


    private JScrollPane createPostContentPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar()); // 커스텀 스크롤바 적용

        // 초기 스크롤 위치를 맨 위로 설정
        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(0);
        });

        updatePostContent("posts");
        return scrollPane;
    }

    private void updatePostContent(String type) {
        mainPanel.removeAll();
        List<PostUI> posts = new ArrayList<>();

        try {
            // 데이터베이스에서 현재 userId에 해당하는 게시물 가져오기
            String query = "SELECT Posts.post_id, Posts.user_id, Posts.content, Posts.created_at, " +
                    "Users.name, Users.email " +
                    "FROM Posts " +
                    "JOIN Users ON Posts.user_id = Users.user_id " +
                    "WHERE Posts.user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId); // 현재 프로필의 userId 사용
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int postId = rs.getInt("post_id");
                int userId = rs.getInt("user_id");
                String userName = rs.getString("name");
                String email = rs.getString("email");
                String content = rs.getString("content");
                String created_at = rs.getString("created_at");

                // 좋아요, 댓글, 북마크 수는 필요에 따라 가져오도록 수정
                int likes = 0;
                int comments = 0;
                int bookmarks = 0;

                // PostUI 객체 생성 시 mainFrame과 userId를 전달
                PostUI postUI = new PostUI(mainframe, postId, userId, userName, email, content, likes, comments, bookmarks, created_at, userService, postService, connection);
                posts.add(postUI);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (PostUI post : posts) {
            mainPanel.add(post);
        }

        mainPanel.revalidate();
        mainPanel.repaint();

        // 스크롤 위치 강제로 맨 위로 설정
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, mainPanel);
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }

    private void updateCommentContent(String type) {
        mainPanel.removeAll();
        List<CommentUI> comments = new ArrayList<>();

        try {
            // 데이터베이스에서 현재 userId에 해당하는 댓글 가져오기
            String query = "SELECT Comments.comment_id, Comments.post_id, Comments.user_id, Comments.content, Comments.created_at, " +
                    "Users.name, Users.email, COUNT(Comment_Likes.user_id) AS likes " +
                    "FROM Comments " +
                    "JOIN Users ON Comments.user_id = Users.user_id " +
                    "LEFT JOIN Comment_Likes ON Comments.comment_id = Comment_Likes.comment_id " +
                    "WHERE Comments.user_id = ? " +
                    "GROUP BY Comments.comment_id, Comments.post_id, Comments.user_id, Comments.content, Comments.created_at, Users.name, Users.email";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userId); // 현재 프로필의 userId 사용
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String userName = rs.getString("name");
                String email = rs.getString("email");
                String content = rs.getString("content");
                int likes = rs.getInt("likes");

                // CommentUI 객체 생성
                CommentUI commentUI = new CommentUI(userName, email, content);
                comments.add(commentUI);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (CommentUI comment : comments) {
            mainPanel.add(comment);
        }

        mainPanel.revalidate();
        mainPanel.repaint();

        // 스크롤 위치 강제로 맨 위로 설정
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, mainPanel);
            if (scrollPane != null) {
                scrollPane.getVerticalScrollBar().setValue(0);
            }
        });
    }



    private JPanel createDynamicUnderlinePanel(JButton button) {
        JPanel underline = new JPanel();
        underline.setBackground(Color.BLACK);
        underline.setPreferredSize(new Dimension(
                button.getFontMetrics(button.getFont()).stringWidth(button.getText()), 3)); // 텍스트 길이만큼 크기 설정
        return underline;
    }

    private JButton createTabButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        return button;
    }

    private JButton createFollowInfoButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.GRAY);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return button;
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            System.err.println("리소스를 찾을 수 없습니다: " + path);
            return null;
        }
        return new ImageIcon(resource);
    }
}
