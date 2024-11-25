package twitter.ui.post;

import twitter.service.bookmarkService;
import twitter.service.userService;

import javax.swing.*;
import twitter.main.MainFrame;
import twitter.model.User;
import twitter.service.postService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostFooterPanel extends JPanel {
    private JButton commentButton;
    private final postService postService;
    private final userService userService;
    private final bookmarkService bookmarkService;
    private final int postId;
    private JButton likeButton;
    private JLabel likesLabel;
    private static final Logger logger = Logger.getLogger(PostFooterPanel.class.getName());

    public PostFooterPanel(int likes, int comments, int bookmarks, int postId, int userId, userService userService, bookmarkService bookmarkService, Connection connection) {
        this.postId = postId;
        this.userService = userService;
        this.bookmarkService = bookmarkService;
        this.postService = new postService();

        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(Color.BLACK);

        commentButton = createIconButton("commentdef.png", "commenthover.png");
        commentButton.setText(" " + comments);

        likeButton = createToggleIconButton("likedef.png", "likeclicked.png", likes, connection);
        JButton bookmarkButton = createBookmarkButton(postId, "bookmarkdef.png", "bookmarkClicked.png", userId, connection);

        likesLabel = new JLabel(" " + likes);
        likesLabel.setForeground(Color.GRAY);
        likesLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

        JPanel likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        likePanel.setBackground(Color.BLACK);
        likePanel.add(likeButton);
        likePanel.add(likesLabel);

        add(likePanel);
        add(commentButton);
        add(bookmarkButton);
    }

    public JButton getCommentButton() {
        return commentButton;
    }

    private JButton createIconButton(String defaultIconPath, String hoverIconPath) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + hoverIconPath));

        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(defaultIcon);
            }
        });
        return button;
    }

    private JButton createToggleIconButton(String defaultIconPath, String toggledIconPath, int initialCount, Connection connection) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon toggledIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + toggledIconPath));

        JButton button = new JButton(defaultIcon);
        button.setText(" " + initialCount);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);

        button.addMouseListener(new MouseAdapter() {
            private boolean isToggled = false;
            private int count = initialCount;

            @Override
            public void mousePressed(MouseEvent e) {
                User currentUser = userService.getCurrentUser();
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(null, "로그인 후 이용해주세요.", "로그인 필요", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                isToggled = !isToggled;
                try {
                    if (isToggled) {
                        postService.likePost(connection, currentUser, postId);
                    } else {
                        postService.unlikePost(connection, currentUser, postId);
                    }
                    count = postService.getLikeCount(connection, postId);
                    button.setText(" " + count);
                    button.setIcon(isToggled ? toggledIcon : defaultIcon);
                    likesLabel.setText(" " + count);

                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "좋아요 처리 중 오류 발생: " + ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(null, "좋아요 처리 중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    private JButton createBookmarkButton(int postId, String defaultIconPath, String toggledIconPath, int userId, Connection connection) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon toggledIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + toggledIconPath));

        JButton button = new JButton();
        boolean isBookmarked = false;
        int bookmarkCount = 0;

        try {
            bookmarkCount = bookmarkService.getBookmarkCount(postId);
            isBookmarked = bookmarkService.isBookmarkedByUser(postId, userId);

            button.setIcon(isBookmarked ? toggledIcon : defaultIcon);
            button.setText(" " + bookmarkCount);
        } catch (SQLException e) {
            e.printStackTrace();
            button.setText("Error");
        }

        button.addMouseListener(new MouseAdapter() {
            private boolean isToggled = isBookmarked;

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if (isToggled) {
                        bookmarkService.removeBookmark(postId, userId);
                        isToggled = false;
                        bookmarkCount--;
                        button.setIcon(defaultIcon);
                    } else {
                        bookmarkService.addBookmark(postId, userId);
                        isToggled = true;
                        bookmarkCount++;
                        button.setIcon(toggledIcon);
                    }
                    button.setText(" " + bookmarkCount);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return button;
    }
}
