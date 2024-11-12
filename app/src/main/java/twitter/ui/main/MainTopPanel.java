package twitter.ui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

    // 기존 기능 유지하며 아이콘 로드 기능
    private ImageIcon loadIcon(String path) {
        java.net.URL resource = getClass().getResource(path);
        if (resource == null) {
            return null;
        }
        return new ImageIcon(resource);
    }

    // MainUI 인스턴스를 추가하여 recommendButton, followButton 클릭 시 호출
    public MainTopPanel(Main_Ui mainUI, MainFrame mainframe, Connection connection, userService userService) {
        this.mainUI = mainUI;
        setLayout(new BorderLayout());
        setBackground(new Color(7, 7, 7));
        setPreferredSize(new Dimension(getWidth(), 100));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton profileButton = createIconButton(profileIcon, userService, mainframe);
        JLabel logoLabel = new JLabel(xLogoIcon, SwingConstants.CENTER);
        JButton writePostButton = createIconButtonWithHover(writePostIconDefault, writePostIconHover, writePostIconDefault);

        topPanel.add(profileButton, BorderLayout.WEST);
        topPanel.add(logoLabel, BorderLayout.CENTER);
        topPanel.add(writePostButton, BorderLayout.EAST);

        JPanel subTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        subTopPanel.setBackground(new Color(7, 7, 7));
        recommendButton = createCustomButton("추천");
        followButton = createCustomButton("팔로우 중");

        recommendButtonUnderline = createUnderlinePanel(recommendButton);
        followButtonUnderline = createUnderlinePanel(followButton);
        followButtonUnderline.setVisible(false);

        recommendButton.setForeground(Color.WHITE);
        recommendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButtons(true);
                recommendButton.setForeground(Color.WHITE);
                followButton.setForeground(Color.GRAY);
                mainUI.updatePostContent("recommend"); // 추천 포스트 호출
            }
        });

        followButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButtons(false);
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

    // 기존 기능 유지
    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.GRAY);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private JPanel createUnderlinePanel(JButton button) {
        JPanel underline = new JPanel();
        underline.setBackground(new Color(0, 122, 255));
        underline.setPreferredSize(new Dimension(button.getPreferredSize().width, 3));
        return underline;
    }

    private JPanel createButtonPanel(JButton button, JPanel underline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(7, 7, 7));
        panel.add(button, BorderLayout.CENTER);
        panel.add(underline, BorderLayout.SOUTH);
        return panel;
    }

    private void toggleButtons(boolean recommendSelected) {
        recommendButtonUnderline.setVisible(recommendSelected);
        followButtonUnderline.setVisible(!recommendSelected);
    }

    private JButton createIconButton(ImageIcon icon, userService userService, MainFrame mainFrame) {
        JButton button;
        if (!userService.isLoggedIn()) {
            // 로그인되어 있지 않은 경우: "Login" 버튼 표시 => 임의로 이렇게 설정해두었습니다! 디자인 알맞게 수정해주시면 될 거 같아요
            button = new JButton("Login");
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.addActionListener(e -> mainFrame.showLoginPanel());
        } else {
            button = new JButton(icon);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.addActionListener(e -> System.out.println("Go to Profile Page"));
        }
        return button;
    }

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
