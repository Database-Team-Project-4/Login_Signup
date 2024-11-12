package twitter.ui.main;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import twitter.main.MainFrame;
import twitter.service.userService;

public class FollowerTopPanel extends JPanel {
    private JPanel followerUnderline, followingUnderline;
    private JButton followerButton, followingButton;

    public FollowerTopPanel(MainFrame mainFrame, Connection connection, userService userService) {
        setLayout(new BorderLayout());
        setBackground(new Color(7, 7, 7));
        setPreferredSize(new Dimension(getWidth(), 100));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton backButton = createHoverButton("<");

        String usernameText = (userService.currentUser != null) ? userService.currentUser.getName() : "Need to Login";
        JLabel usernameLabel = new JLabel(usernameText, SwingConstants.CENTER);

        usernameLabel.setForeground(Color.WHITE);

        JButton addFriendButton = createHoverButton("+");

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(usernameLabel, BorderLayout.CENTER);
        topPanel.add(addFriendButton, BorderLayout.EAST);

        JPanel subTopPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        subTopPanel.setBackground(new Color(7, 7, 7));

        followerButton = createHoverButton("팔로워");
        followingButton = createHoverButton("팔로잉");

        followerUnderline = createUnderlinePanel();
        followingUnderline = createUnderlinePanel();

        followerButton.setForeground(Color.WHITE);
        followerUnderline.setVisible(true);

        // 익명 내부 클래스를 사용하여 이벤트 리스너 추가
        followerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectButton(followerButton, followerUnderline);
            }
        });

        followingButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectButton(followingButton, followingUnderline);
            }
        });

        subTopPanel.add(createButtonPanel(followerButton, followerUnderline));
        subTopPanel.add(createButtonPanel(followingButton, followingUnderline));

        add(topPanel, BorderLayout.NORTH);
        add(subTopPanel, BorderLayout.SOUTH);
    }

    private JButton createHoverButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.GRAY);
        button.setBackground(new Color(30, 30, 30));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // 익명 내부 클래스로 마우스 이벤트 처리
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.getForeground().equals(Color.WHITE)) {
                    button.setForeground(Color.DARK_GRAY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.getForeground().equals(Color.WHITE)) {
                    button.setForeground(Color.GRAY);
                }
            }
        });

        return button;
    }

    private JPanel createUnderlinePanel() {
        JPanel underline = new JPanel();
        underline.setBackground(new Color(0, 122, 255));
        underline.setPreferredSize(new Dimension(50, 3));
        underline.setVisible(false);
        return underline;
    }

    private JPanel createButtonPanel(JButton button, JPanel underline) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(7, 7, 7));
        panel.add(button, BorderLayout.CENTER);
        panel.add(underline, BorderLayout.SOUTH);
        return panel;
    }

    private void selectButton(JButton button, JPanel underline) {
        resetButtonColors();
        button.setForeground(Color.WHITE);
        underline.setVisible(true);
    }

    private void resetButtonColors() {
        followerButton.setForeground(Color.GRAY);
        followingButton.setForeground(Color.GRAY);

        followerUnderline.setVisible(false);
        followingUnderline.setVisible(false);
    }
}
