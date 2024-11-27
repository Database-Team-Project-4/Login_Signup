package twitter.ui.mainPage;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import twitter.main.MainFrame;
import twitter.service.userService;

public class MainTopPanel extends JPanel {
    private JPanel recommendButtonUnderline, followButtonUnderline;
    private JButton recommendButton, followButton;
    private final ImageIcon profileIcon = loadIcon("/TwitterIcons/icondef.png");
    private final ImageIcon xLogoIcon = loadIcon("/TwitterIcons/X_logo.png");
    private final ImageIcon writePostIconDefault = loadIcon("/TwitterIcons/writepostdef.png");
    private final ImageIcon writePostIconHover = loadIcon("/TwitterIcons/writepostcursor.png");
    private Main_Ui mainUI;  // MainUI 인스턴스 참조 추가
    private int userId;
    private userService userService;
    private MainFrame mainframe;

    // 아이콘 로드 기능 유지
    private ImageIcon loadIcon(String path) {
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }

    // 생성자
    public MainTopPanel(Main_Ui mainUI, MainFrame mainframe, Connection connection, userService userService) {
        this.mainUI = mainUI;
        this.mainframe = mainframe;
        this.userService = userService;
        setLayout(new BorderLayout());
        setBackground(new Color(7, 7, 7));
        setPreferredSize(new Dimension(getWidth(), 100));

        if (userService.isLoggedIn()) {
            this.userId = userService.getCurrentUser().getId(); // 현재 사용자 ID 가져오기
        } else {
            this.userId = -1; // 로그인 안된 경우 기본값
        }

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton profileButton = createIconButton(profileIcon, userService, mainframe);
        JLabel logoLabel = new JLabel(xLogoIcon, SwingConstants.CENTER);
        JButton writePostButton = createIconButtonWithHover(writePostIconDefault, writePostIconHover, writePostIconDefault);

        topPanel.add(profileButton, BorderLayout.WEST);
        topPanel.add(logoLabel, BorderLayout.CENTER);
        topPanel.add(writePostButton, BorderLayout.EAST);

        // Post 작성 버튼 클릭 이벤트
        writePostButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainframe.showAddPostPanel();
            }
        });

        // 프로필 버튼 클릭 이벤트
        profileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (userService.isLoggedIn()) {
                    mainframe.showUserProfilePanel(userId);
                } else {
                    mainframe.showLoginPanel();
                }
            }
        });

        // 하단 버튼 패널
        JPanel subTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        subTopPanel.setBackground(new Color(7, 7, 7));
        recommendButton = createCustomButton("추천");
        followButton = createCustomButton("팔로우 중");

        recommendButtonUnderline = createUnderlinePanel(recommendButton);
        followButtonUnderline = createUnderlinePanel(followButton);
        followButtonUnderline.setVisible(false); // 초기값: "추천" 버튼만 활성화

        recommendButton.setForeground(Color.WHITE);
        followButton.setForeground(Color.GRAY);

        // "추천" 버튼 클릭 이벤트
        recommendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButtons(true); // "추천" 버튼 활성화
                recommendButton.setForeground(Color.WHITE);
                followButton.setForeground(Color.GRAY);
                mainUI.updatePostContent("recommend"); // 추천 포스트 호출
            }
        });

        // "팔로우 중" 버튼 클릭 이벤트
        followButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButtons(false); // "팔로우 중" 버튼 활성화
                followButton.setForeground(Color.WHITE);
                recommendButton.setForeground(Color.GRAY);
                mainUI.updatePostContent("following"); // 팔로우 중 포스트 호출
            }
        });

        subTopPanel.add(createButtonPanel(recommendButton, recommendButtonUnderline));
        subTopPanel.add(createButtonPanel(followButton, followButtonUnderline));

        add(topPanel, BorderLayout.NORTH);
        add(subTopPanel, BorderLayout.SOUTH);
    }

    // 버튼 기본 생성
    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.GRAY);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    // 밑줄 생성
    private JPanel createUnderlinePanel(JButton button) {
        JPanel underline = new JPanel();
        underline.setBackground(new Color(0, 122, 255));
        underline.setPreferredSize(new Dimension(button.getPreferredSize().width, 3));
        return underline;
    }

    // 버튼과 밑줄 패널 생성
    private JPanel createButtonPanel(JButton button, JPanel underline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(7, 7, 7));
        panel.add(button, BorderLayout.CENTER);
        panel.add(underline, BorderLayout.SOUTH);
        return panel;
    }

    // 버튼 활성화 상태 토글
    private void toggleButtons(boolean recommendSelected) {
        recommendButtonUnderline.setVisible(recommendSelected);
        followButtonUnderline.setVisible(!recommendSelected);
    }

    // 프로필 아이콘 버튼 생성
    private JButton createIconButton(ImageIcon icon, userService userService, MainFrame mainFrame) {
        JButton button = new JButton(icon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(e -> {
            if (userService.isLoggedIn()) {
                mainFrame.showUserProfilePanel(userId);
            } else {
                mainFrame.showLoginPanel();
            }
        });
        return button;
    }

    // 아이콘에 Hover 효과 추가
    private JButton createIconButtonWithHover(ImageIcon defaultIcon, ImageIcon hoverIcon, ImageIcon clickedIcon) {
        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            boolean isClicked = false;

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isClicked) {
                    button.setIcon(hoverIcon);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isClicked) {
                    button.setIcon(defaultIcon);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                button.setIcon(clickedIcon);
                isClicked = true;
            }
        });
        return button;
    }
}
