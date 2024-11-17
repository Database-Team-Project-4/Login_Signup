package twitter.ui.profile;

import twitter.ui.module.custombutton.RoundedRectangleButton;
import twitter.ui.post.PostUI;
import twitter.ui.module.CustomScrollbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
//기본적인 프로필화면입니다. 다른 사용자의 프로필임을 상정하고 만들어 팔로우 버튼이 있으며 팔로우 버튼만 삭제하면 바로 나의 프로필로 사용할 수 있습니다.
//추가로 나의 프로필에서 팔로우 버튼 위치에 사용자 정보 수정 버튼 등을 생성할수도 있습니다.
public class UserProfile extends JPanel {

    private JPanel mainPanel;
    private JButton postButton;
    private JButton replyButton;
    private JPanel postUnderline;
    private JPanel replyUnderline;

    public UserProfile() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

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
        JLabel userName = new JLabel("Gachon");
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("SansSerif", Font.BOLD, 16));
        userName.setBounds(25, 100, 200, 20); // 이름을 프로필 이미지 바로 아래 배치
        userHeaderPanel.add(userName);

        // 사용자 핸들
        JLabel userHandle = new JLabel("@gachon");
        userHandle.setForeground(Color.GRAY);
        userHandle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userHandle.setBounds(25, 125, 200, 20); // 핸들을 사용자 이름 바로 아래 배치
        userHeaderPanel.add(userHandle);

        // 팔로우/팔로잉 버튼
        JPanel followInfoPanel = new JPanel();
        followInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 버튼 간 간격을 5px로 설정
        followInfoPanel.setBackground(new Color(7, 7, 7));
        followInfoPanel.setBounds(8, 155, 200, 30); // 핸들 아래 위치

        JButton followingButton = createHoverableButton("4 팔로잉");
        followingButton.setPreferredSize(new Dimension(70, 25)); // 버튼 크기 조정
        followingButton.setMargin(new Insets(0, 0, 0, 0)); // 버튼 내부 패딩 축소

        JButton followerButton = createHoverableButton("2 팔로우");
        followerButton.setPreferredSize(new Dimension(70, 25)); // 버튼 크기 조정
        followerButton.setMargin(new Insets(0, 0, 0, 0)); // 버튼 내부 패딩 축소

        // 팔로우 버튼
        JButton followButton = createFollowButton();
        followButton.setBounds(340, 55, 80, 30);  // 패널 오른쪽 경계 근처에 위치


        // 버튼 상하 패딩 및 오른쪽 유격 추가
        JPanel paddedFollowButton = new JPanel(new GridBagLayout());
        paddedFollowButton.setBackground(new Color(0, 122, 255));
        paddedFollowButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // 오른쪽에 10px 유격 추가
        paddedFollowButton.add(followButton);

        followInfoPanel.add(followingButton);
        followInfoPanel.add(followerButton);
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
                updatePostContent("replies");
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

        if ("posts".equals(type)) {
            for (int i = 1; i <= 10; i++) { // 테스트용 10개의 포스트 생성
                posts.add(new PostUI("Gachon", "@gachon", "게시물 #" + i, 15 + i, 3 + i, 5 + i));
            }
        } else if ("replies".equals(type)) {
            for (int i = 1; i <= 5; i++) { // 테스트용 5개의 답글 생성
                posts.add(new PostUI("Gachon", "@gachon", "답글 #" + i, 5 + i, 1 + i, i));
            }
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
