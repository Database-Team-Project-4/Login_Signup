package twitter.ui.TopBars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainTopPanel extends JPanel {
    private JPanel recommendButtonUnderline, followButtonUnderline;
    private JButton recommendButton, followButton;

    private final String profileIconPath = "C:/Users/ind07/OneDrive/바탕 화면/DongHO/VSCODE/pr2/TwitterDesign/TwitterDesign/TwitterIcons/icondef.png";
    private final String xLogoPath = "C:/Users/ind07/OneDrive/바탕 화면/DongHO/VSCODE/pr2/TwitterDesign/TwitterDesign/TwitterIcons/X_logo.png";
    private final String writePostIconDefault = "C:/Users/ind07/OneDrive/바탕 화면/DongHO/VSCODE/pr2/TwitterDesign/TwitterDesign/TwitterIcons/writepostdef.png";
    private final String writePostIconHover = "C:/Users/ind07/OneDrive/바탕 화면/DongHO/VSCODE/pr2/TwitterDesign/TwitterDesign/TwitterIcons/writepostcursor.png";

    public MainTopPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(7, 7, 7));
        setPreferredSize(new Dimension(getWidth(), 100));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton profileButton = createIconButton(profileIconPath);
        JLabel logoLabel = new JLabel(new ImageIcon(xLogoPath), SwingConstants.CENTER);
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

        recommendButton.setForeground(Color.WHITE); // 기본 선택된 버튼 텍스트 색상 흰색 설정
        recommendButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButtons(true);
                recommendButton.setForeground(Color.WHITE);
                followButton.setForeground(Color.GRAY);
            }
        });

        followButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleButtons(false);
                followButton.setForeground(Color.WHITE);
                recommendButton.setForeground(Color.GRAY);
            }
        });

        subTopPanel.add(createButtonPanel(recommendButton, recommendButtonUnderline));
        subTopPanel.add(createButtonPanel(followButton, followButtonUnderline));

        add(topPanel, BorderLayout.NORTH);
        add(subTopPanel, BorderLayout.SOUTH);
    }

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

    private JButton createIconButton(String iconPath) {
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(icon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private JButton createIconButtonWithHover(String defaultIconPath, String hoverIconPath, String clickedIconPath) {
        ImageIcon defaultIcon = new ImageIcon(defaultIconPath);
        ImageIcon hoverIcon = new ImageIcon(hoverIconPath);
        ImageIcon clickedIcon = new ImageIcon(clickedIconPath);

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
