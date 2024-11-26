package twitter.ui.mainPage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import twitter.model.User;
import twitter.service.followService;
import twitter.service.postService;
import twitter.ui.follow.follower.FollowerListPanel;
import twitter.ui.follow.following.FollowingListPanel;
import twitter.main.MainFrame;
import twitter.service.userService;
import twitter.ui.post.PostUI;
import twitter.ui.post.ExpandedPostUI;


public class Main_Ui extends JPanel {
    private JPanel completeTopPanel;
    private JButton homeButton, searchButton, followerButton, bookmarkButton, GeminiButton;
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private MainFrame mainFrame;
    private Connection connection; //
    private userService userService;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Main_Ui getMainUi() {
        return this;
    }

    private final String homeIconDefault = "/TwitterIcons/home_icondef.png";
    private final String homeIconClicked = "/TwitterIcons/home_iconclicked.png";
    private final String homeIconHover = "/TwitterIcons/home_iconcursor.png";
    private final String searchIconDefault = "/TwitterIcons/searchdef.png";
    private final String searchIconHover = "/TwitterIcons/searchcursor.png";
    private final String searchIconClicked = "/TwitterIcons/searchclicked.png";
    private final String communityIconDefault = "/TwitterIcons/comdef.png";
    private final String communityIconHover = "/TwitterIcons/comcursor.png";
    private final String communityIconClicked = "/TwitterIcons/comclicked.png";
    private final String BookmarkIconDefault = "/TwitterIcons/bookmarkdef.png";
    private final String BookmarkIconHover = "/TwitterIcons/bookmarkdef.png";
    private final String BookmarkIconClicked = "/TwitterIcons/bookmarkClicked.png";

    private final String GeminiIconDefault = "/TwitterIcons/aimessagebot.png";
    private final String GeminiIconHover = "/TwitterIcons/aimessagebothover.png";
    private final String GeminiIconClicked = "/TwitterIcons/aimessagebothover.png";


    public Main_Ui(MainFrame mainframe, Connection connection, userService userService) {
        this.mainFrame = mainframe;
        this.connection = connection;
        this.userService = userService; // Initialize connection and userService


        setLayout(new BorderLayout());

        completeTopPanel = new JPanel(new CardLayout());
        completeTopPanel.add(new MainTopPanel(this, mainframe, connection, userService), "MainTop");
        completeTopPanel.add(new SearchTopPanel(mainframe, connection, userService), "SearchTop");

        completeTopPanel.add(new FollowerTopPanel(mainframe, connection, userService, this), "FollowerTop"); //FollowerTopPanel에서 mainui의 메소드를 사용하기위해 this를 넘겼습니다.

        completeTopPanel.add(new BookmarkTopPanel(), "BookmarkTop");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(7, 7, 7));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 72));

        homeButton = createIconButtonWithHover(homeIconDefault, homeIconHover, homeIconClicked);
        searchButton = createIconButtonWithHover(searchIconDefault, searchIconHover, searchIconClicked);
        followerButton = createIconButtonWithHover(communityIconDefault, communityIconHover, communityIconClicked);
        bookmarkButton = createIconButtonWithHover(BookmarkIconDefault, BookmarkIconHover, BookmarkIconClicked);
        GeminiButton = createIconButtonWithHover(GeminiIconDefault, GeminiIconHover, GeminiIconClicked);

        homeButton.addActionListener(e -> {
            setBottomButtonSelected(homeButton);
            showPanel("MainTop");
        });

        searchButton.addActionListener(e -> {
            setBottomButtonSelected(searchButton);
            showPanel("SearchTop");
        });

        followerButton.addActionListener(e -> {
            setBottomButtonSelected(followerButton);
            showPanel("FollowerTop");
        });

        bookmarkButton.addActionListener(e -> {
            setBottomButtonSelected(bookmarkButton); // 버튼 상태 변경
            showPanel("BookmarkTop"); // Bookmark 패널 표시
        });
        GeminiButton.addActionListener(e -> {
            setBottomButtonSelected(GeminiButton);
            mainFrame.showGeminiPanel(); // mainFrame 사용
            showPanel("GeminiPanel");  // GeminiPanel을 보여주도록 showPanel 호출 추가
        });

        bottomPanel.add(homeButton);
        bottomPanel.add(searchButton);
        bottomPanel.add(followerButton);
        bottomPanel.add(bookmarkButton);
        bottomPanel.add(GeminiButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(LEFT_ALIGNMENT); // 패널을 왼쪽 정렬로 설정

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

   // 커스텀 스크롤바 디자인 적용
   scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(128, 128, 128, 180); // 회색, 반투명
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createEmptyButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createEmptyButton();
    }

    private JButton createEmptyButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, 8, thumbBounds.height, 10, 10); // 스크롤 막대 너비 8로 설정
        g2.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // 트랙을 투명하게 처리하여 보이지 않도록 설정
        g.setColor(Color.BLACK); // 완전히 투명한 색
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }
});


// 스크롤바 두께 설정 및 가로 스크롤바 숨김
scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

add(completeTopPanel, BorderLayout.NORTH);
add(scrollPane, BorderLayout.CENTER);
add(bottomPanel, BorderLayout.SOUTH);

setBottomButtonSelected(homeButton);
updatePostContent("recommend");


}


    public void updatePostContent(String filterType) {
        // 기존 콘텐츠 제거
        mainPanel.removeAll();
        mainPanel.setPreferredSize(null);

        postService postService = new postService();
        Connection con = MainFrame.getConnection();

        // 모든 포스트 가져오기
        List<PostUI> examplePosts = postService.getAllPosts(con, mainFrame, userService);

        if ("following".equals(filterType)) {
            // 로그인 상태 확인
            if (userService.getCurrentUser() == null) {
                // 로그인하지 않은 경우 "로그인 필요" 메시지 출력
                JPanel noResultPanel = new JPanel(new GridBagLayout());
                noResultPanel.setBackground(Color.BLACK);

                JLabel noResultLabel = new JLabel("로그인 필요");
                noResultLabel.setForeground(Color.WHITE);
                noResultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                noResultPanel.add(noResultLabel);

                mainPanel.add(noResultPanel);
            } else {
                // 로그인한 경우, 팔로우한 사용자의 게시물 필터링
                User currentUser = userService.getCurrentUser();
                followService followService = new followService();

                List<User> followingList = followService.getFollowing(con, currentUser);

                // 팔로우한 사용자의 ID 목록 추출
                List<Integer> followedUserIds = followingList.stream()
                        .map(User::getId)
                        .toList();

                // 팔로우한 사용자의 게시물만 필터링
                List<PostUI> filteredPosts = examplePosts.stream()
                        .filter(post -> followedUserIds.contains(post.getUserId()))
                        .toList();

                if (filteredPosts.isEmpty()) {
                    // 팔로우한 사용자의 게시물이 없는 경우 "게시물 없음" 메시지 출력
                    JPanel noResultPanel = new JPanel(new GridBagLayout());
                    noResultPanel.setBackground(Color.BLACK);

                    JLabel noResultLabel = new JLabel("게시물 없음");
                    noResultLabel.setForeground(Color.WHITE);
                    noResultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                    noResultPanel.add(noResultLabel);

                    mainPanel.add(noResultPanel);
                } else {
                    // 필터링된 게시물 추가
                    for (PostUI post : filteredPosts) {
                        mainPanel.add(post);

                        // 포스트 클릭 이벤트 추가 (확장된 화면으로 이동)
                        post.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                showExpandedPost(post.getPostId());
                            }
                        });
                    }
                }
            }
        } else {
            // "popular" 또는 "recent" 필터 처리
            if (examplePosts.isEmpty()) {
                // 게시물이 없을 때 "게시물 없음" 메시지 출력
                JPanel noResultPanel = new JPanel(new GridBagLayout());
                noResultPanel.setBackground(Color.BLACK);

                JLabel noResultLabel = new JLabel("게시물 없음");
                noResultLabel.setForeground(Color.WHITE);
                noResultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                noResultPanel.add(noResultLabel);

                mainPanel.add(noResultPanel);
            } else {
                // 게시물이 존재할 때 정렬 처리
                if ("popular".equals(filterType)) {
                    examplePosts.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes())); // 좋아요 수로 정렬
                }
                // 최신순 정렬은 PostService에서 관리하는 데이터 순서를 그대로 사용

                // 포스트 추가
                for (PostUI post : examplePosts) {
                    mainPanel.add(post);

                    // 포스트 클릭 이벤트 추가
                    post.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            showExpandedPost(post.getPostId());
                        }
                    });
                }
            }
        }

        // 동적 크기 조정 및 레이아웃 갱신
        int postCount = mainPanel.getComponentCount();
        int postHeight = 150; // 각 포스트의 예상 높이
        mainPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 72));

        mainPanel.revalidate(); // 레이아웃 업데이트
        mainPanel.repaint(); // 화면 갱신

        // 스크롤바 위치 초기화
        JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    public void updateSearchContent(String keyword, String filterType) {

        mainPanel.removeAll(); // 기존 콘텐츠 제거
        mainPanel.setPreferredSize(null); // 크기 초기화

        if (keyword == null || keyword.trim().isEmpty()) {
            // 검색어가 없을 때 "검색어 없음" 메시지 출력
            System.out.println("empty keyword.");

            JPanel noKeywordPanel = new JPanel(new GridBagLayout());
            noKeywordPanel.setBackground(Color.BLACK);

            JLabel noKeywordLabel = new JLabel("검색어 없음");
            noKeywordLabel.setForeground(Color.WHITE);
            noKeywordLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            noKeywordPanel.add(noKeywordLabel);

            mainPanel.add(noKeywordPanel); // 메인 패널에 추가
        } else {

            postService postService = new postService();
            Connection con = MainFrame.getConnection();

            List<PostUI> examplePosts = postService.getAllPosts(con, mainFrame, userService); // 모든 포스트 가져오기

            // 키워드를 포함하는 포스트 필터링
            List<PostUI> filteredPosts = new ArrayList<>(examplePosts.stream()
                    .filter(post -> post.getContentText().toLowerCase().contains(keyword.toLowerCase()) ||
                            post.getUserName().toLowerCase().contains(keyword.toLowerCase()) ||
                            post.getUserEmail().toLowerCase().contains(keyword.toLowerCase()))
                    .toList());

            if (filteredPosts.isEmpty()) {
                //검색 결과 없음

                JPanel noResultPanel = new JPanel(new GridBagLayout());
                noResultPanel.setBackground(Color.BLACK);

                JLabel noResultLabel = new JLabel(keyword + "에 해당하는 검색 결과 없음");
                noResultLabel.setForeground(Color.WHITE);
                noResultLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
                noResultPanel.add(noResultLabel);

                // 하단 설명 메시지 라벨 생성
                JLabel suggestionLabel = new JLabel("검색어를 다시 확인해보세요");
                suggestionLabel.setForeground(Color.LIGHT_GRAY);
                suggestionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

                // GridBagLayout을 사용하여 아래쪽에 배치
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1; // 두 번째 줄
                gbc.insets = new Insets(10, 0, 0, 0); // 위쪽 여백 설정
                noResultPanel.add(suggestionLabel, gbc);

                mainPanel.add(noResultPanel); // 메인 패널에 추가
            } else {
                // 인기순/최신순 정렬
                if ("popular".equals(filterType)) {
                    filteredPosts.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes())); // 좋아요 수로 정렬
                } else if ("recent".equals(filterType)) {
                    // DateTimeFormatter를 사용하여 문자열을 LocalDateTime으로 파싱
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    filteredPosts.sort((p1, p2) -> {
                        LocalDateTime time1 = LocalDateTime.parse(p1.getCreatedAt(), formatter);
                        LocalDateTime time2 = LocalDateTime.parse(p2.getCreatedAt(), formatter);
                        return time2.compareTo(time1); // 최신순 정렬
                    });
                }

                // 필터링된 포스트를 메인 패널에 추가
                for (PostUI post : filteredPosts) {
                    mainPanel.add(post);
                }
            }

            // 동적 크기 조정 및 레이아웃 갱신
            int postCount = mainPanel.getComponentCount();
            int postHeight = 150; // 각 포스트의 예상 높이
            mainPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 72));

            mainPanel.revalidate(); // 레이아웃 업데이트
            mainPanel.repaint(); // 화면 갱신

            // 스크롤바 위치 초기화
            JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // 스크롤 초기화
        }
    }


    public void updateFollowContent(String type) {
        mainPanel.removeAll();

        List<String> userNames = new ArrayList<>();
        List<String> userHandles = new ArrayList<>();

        Connection connection = MainFrame.getConnection();
        followService followService = new followService();

        if(userService.getCurrentUser() == null){
                // 로그인X 문구 추가
                JPanel loginPromptPanel = new JPanel(new GridBagLayout());
                loginPromptPanel.setBackground(Color.BLACK);

                JLabel loginRequiredLabel = new JLabel("Need to login.");
                loginRequiredLabel.setForeground(Color.WHITE);
                loginRequiredLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

                JButton loginButton = new JButton("Login");
                loginButton.setBackground(new Color(0, 122, 255));
                loginButton.setForeground(Color.WHITE);
                loginButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
                loginButton.setFocusPainted(false);
                loginButton.setBorderPainted(false);
                loginButton.setOpaque(true);
                loginButton.setPreferredSize(new Dimension(150, 40));

                loginButton.addActionListener(e -> {
                    mainFrame.showLoginPanel(); // 로그인 화면으로 이동
                });

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 0, 10, 0);

                gbc.gridy = 0;
                loginPromptPanel.add(loginRequiredLabel, gbc);

                gbc.gridy = 1;
                loginPromptPanel.add(loginButton, gbc);

                mainPanel.add(loginPromptPanel); // "로그인 필요" 메시지와 버튼 추가
                mainPanel.setPreferredSize(new Dimension(getWidth(), 200)); // 메시지 표시 영역 크기 설정
        }
        else
        {

            ImageIcon profileImage = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));
            int id = userService.getCurrentUser().getId();
            if (type.equals("follower")) {
                System.out.println("follower화면으로 바꿨어요"); //debug code

                List<User> followerList = followService.getFollowers(connection, userService.getCurrentUser());

                userNames = followerList.stream().map(User::getName).toList();
                userHandles = followerList.stream().map(User::getEmail).toList();
                mainPanel.add(new FollowerListPanel(userNames,userHandles,profileImage));

            } else if (type.equals("following")){
                System.out.println("following화면으로 바꿨어요"); //debug code

                List<User> followingList = followService.getFollowing(connection, userService.getCurrentUser());

                userNames = followingList.stream().map(User::getName).toList();
                userHandles = followingList.stream().map(User::getEmail).toList();

                int itemCount = userNames.size();
                int itemHeight = 70; // 각 항목의 예상 높이
                int maxVisibleHeight = 540; // 최대 표시 가능한 높이
                int totalHeight = itemCount * itemHeight;
                mainPanel.setPreferredSize(new Dimension(400, totalHeight));
                mainPanel.add(new FollowingListPanel(userNames,userHandles,profileImage));
            }
        }


        mainPanel.revalidate(); // 레이아웃 업데이트
        mainPanel.repaint(); // 화면 갱신
    }

    private void showExpandedPost(int postId) {
        mainFrame.showExpandedPostPanel(postId);
    }


    private JButton createIconButtonWithHover(String defaultIconPath, String hoverIconPath, String clickedIconPath) {
        ImageIcon defaultIcon = loadIcon(defaultIconPath);
        ImageIcon hoverIcon = loadIcon(hoverIconPath);
        ImageIcon clickedIcon = loadIcon(clickedIconPath);

        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new IconButtonMouseAdapter(button, hoverIcon, defaultIcon, clickedIcon));
        return button;
    }

    private ImageIcon loadIcon(String iconPath) {
        java.net.URL resource = getClass().getResource(iconPath);
        return resource != null ? new ImageIcon(resource) : null;
    }

    private void setBottomButtonSelected(JButton selectedButton) {
        // 모든 버튼의 아이콘을 초기화하여 기본 상태로 설정
        homeButton.setIcon(loadIcon(homeIconDefault));
        searchButton.setIcon(loadIcon(searchIconDefault));
        followerButton.setIcon(loadIcon(communityIconDefault));
        bookmarkButton.setIcon(loadIcon(BookmarkIconDefault));
        GeminiButton.setIcon(loadIcon(GeminiIconDefault));

        // 클릭된 버튼만 클릭된 상태의 아이콘으로 설정
        if (selectedButton == homeButton) {
            selectedButton.setIcon(loadIcon(homeIconClicked));
        } else if (selectedButton == searchButton) {
            selectedButton.setIcon(loadIcon(searchIconClicked));
        } else if (selectedButton == followerButton) {
            selectedButton.setIcon(loadIcon(communityIconClicked));
        } else if (selectedButton == bookmarkButton) {
            selectedButton.setIcon(loadIcon(BookmarkIconClicked));
        } else if (selectedButton == GeminiButton) {
            selectedButton.setIcon(loadIcon(GeminiIconClicked));
        }

        // 선택된 버튼 텍스트 색상을 흰색으로 설정하고 나머지는 기본색으로 초기화
        homeButton.setForeground(Color.GRAY);
        searchButton.setForeground(Color.GRAY);
        followerButton.setForeground(Color.GRAY);
        bookmarkButton.setForeground(Color.GRAY);
        GeminiButton.setForeground(Color.GRAY);
        selectedButton.setForeground(Color.WHITE);
    }

    public void updateBookmarkContent() {
        mainPanel.removeAll(); // 기존 콘텐츠 제거
        mainPanel.setPreferredSize(null); // 크기 초기화

        // 로그인 여부 확인 및 분기 처리
        if (userService.getCurrentUser() == null) {
            // 로그인되지 않은 경우 사용자 친화적인 메시지와 버튼 추가
            JPanel loginPromptPanel = new JPanel(new GridBagLayout());
            loginPromptPanel.setBackground(Color.BLACK);

            JLabel loginRequiredLabel = new JLabel("Need to login.");
            loginRequiredLabel.setForeground(Color.WHITE);
            loginRequiredLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

            JButton loginButton = new JButton("Login");
            loginButton.setBackground(new Color(0, 122, 255));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFont(new Font("SansSerif", Font.PLAIN, 16));
            loginButton.setFocusPainted(false);
            loginButton.setBorderPainted(false);
            loginButton.setOpaque(true);
            loginButton.setPreferredSize(new Dimension(150, 40));

            loginButton.addActionListener(e -> {
                mainFrame.showLoginPanel(); // 로그인 화면으로 이동
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 0, 10, 0);

            gbc.gridy = 0;
            loginPromptPanel.add(loginRequiredLabel, gbc);

            gbc.gridy = 1;
            loginPromptPanel.add(loginButton, gbc);

            mainPanel.add(loginPromptPanel); // "로그인 필요" 메시지와 버튼 추가
            mainPanel.setPreferredSize(new Dimension(getWidth(), 200)); // 메시지 표시 영역 크기 설정
        } else {
            // 로그인된 경우 북마크된 포스트 추가
            List<PostUI> bookmarkedPosts = new ArrayList<>();
            postService postService = new postService();

            bookmarkedPosts = postService.getBookmarkedPostsByUser(connection, mainFrame, userService);

            for (PostUI post : bookmarkedPosts) {
                mainPanel.add(post); // 북마크된 포스트를 메인 패널에 추가
            }

            // 동적 크기 조정
            int postCount = mainPanel.getComponentCount();
            int postHeight = 150; // 각 포스트의 예상 높이
            mainPanel.setPreferredSize(new Dimension(getWidth(), postCount * postHeight + 72));

            // 스크롤바 위치 초기화
            JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // 스크롤 초기화
        }

        mainPanel.revalidate(); // 레이아웃 업데이트
        mainPanel.repaint(); // 화면 갱신
    }



    private String currentSearchKeyword = "";

    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (completeTopPanel.getLayout());
        cl.show(completeTopPanel, panelName); // 패널 전환

        if ("MainTop".equals(panelName)) {
            updatePostContent("recommend");
        } else if ("GeminiPanel".equals(panelName)) { // GeminiPanel 처리 추가
            mainPanel.removeAll();
            mainPanel.revalidate();
            mainPanel.repaint();
        } else if ("FollowerTop".equals(panelName)) {
            updateFollowContent("follower");
        } else if ("SearchTop".equals(panelName)) {
            System.out.println("SearchTop 패널 표시");
            currentSearchKeyword = "";
            updateSearchContent(currentSearchKeyword, "popular");

            SearchTopPanel searchTopPanel = (SearchTopPanel) completeTopPanel.getComponent(1); // SearchTopPanel 가져오기
            searchTopPanel.addSearchListener(() -> {

                currentSearchKeyword = searchTopPanel.getKeyword(); // 검색어 가져오기
                String filterType = searchTopPanel.getCurrentFilterType(); // 필터 타입 가져오기

                // updateSearchContent 호출로 mainPanel 갱신
                updateSearchContent(currentSearchKeyword, filterType);

                // 갱신된 mainPanel 출력
                mainPanel.revalidate();
                mainPanel.repaint();

                JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
                SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // 스크롤 초기화

            });
        } else if ("BookmarkTop".equals(panelName)) { // BookmarkTop 패널 처리 추가
            updateBookmarkContent(); // Bookmark 콘텐츠 업데이트

        }
        else {
            mainPanel.removeAll(); // 다른 패널에서는 초기화
            mainPanel.revalidate();
            mainPanel.repaint();
        }

        // 현재 활성화된 패널 디버그 출력

    }


    public void refreshMainPanel() {
        mainPanel.revalidate();
        mainPanel.repaint();

        JScrollPane scrollPane = (JScrollPane) mainPanel.getParent().getParent();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0)); // 스크롤 초기
        System.out.println("mainPanel Refreshed!!");
    }


    private class IconButtonMouseAdapter extends MouseAdapter {
        private final JButton button;
        private final ImageIcon hoverIcon;
        private final ImageIcon defaultIcon;
        private final ImageIcon clickedIcon;
        private boolean isClicked;

        public IconButtonMouseAdapter(JButton button, ImageIcon hoverIcon, ImageIcon defaultIcon, ImageIcon clickedIcon) {
            this.button = button;
            this.hoverIcon = hoverIcon;
            this.defaultIcon = defaultIcon;
            this.clickedIcon = clickedIcon;
            this.isClicked = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isClicked && hoverIcon != null) {
                button.setIcon(hoverIcon);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isClicked && defaultIcon != null) {
                button.setIcon(defaultIcon);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            button.setIcon(clickedIcon);
            isClicked = true;
        }
    }
}