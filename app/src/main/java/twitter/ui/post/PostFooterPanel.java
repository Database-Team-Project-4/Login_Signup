package twitter.ui.post;

import twitter.service.bookmarkService;
import twitter.service.likeService;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class PostFooterPanel extends JPanel {
    private JButton commentButton;
    private JLabel likeCountLabel;
    private boolean isLiked; // 좋아요 상태를 저장하는 인스턴스 변수
    private int likeCount; // 좋아요 개수를 저장하는 인스턴스 변수
    private JButton likeButton; // likeButton을 인스턴스 변수로 선언

    public PostFooterPanel(int postId, int userId, userService userService, Connection connection) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(Color.BLACK);

        commentButton = createIconButton("commentdef.png", "commenthover.png");
        commentButton.setText(" " + 0); // 초기값 0으로 설정

        JPanel likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        likePanel.setBackground(Color.BLACK);

        likeCountLabel = new JLabel();
        likeCountLabel.setForeground(Color.WHITE);

        likeService likeService = new likeService(connection);
        try {
            likeCount = likeService.getLikeCount(postId); // 좋아요 개수 가져오기
            isLiked = likeService.isLikedByUser(postId, userId); // 좋아요 상태 가져오기
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "좋아요 정보를 가져오는 중 오류가 발생했습니다.");
            // 에러 발생 시 기본값 설정 (선택적)
            likeCount = 0;
            isLiked = false;
        }

        likeButton = createLikeButton(postId, "likedef.png", "likeclicked.png", likeService, userService, connection); // likeButton 생성

        likePanel.add(likeButton);
        likePanel.add(likeCountLabel);

        bookmarkService bookmarks = new bookmarkService(connection);
        JButton bookmarkButton = createBookmarkButton(postId, "bookmarkdef.png", "bookmarkClicked.png", userService, bookmarks);

        add(commentButton);
        add(likePanel);
        add(bookmarkButton);

        updateLikeUI(); // UI 업데이트 함수 호출
    }

    private JButton createIconButton(String defaultIconPath, String hoverIconPath) {
        ImageIcon defaultIcon = loadImageIcon(defaultIconPath);
        ImageIcon hoverIcon = loadImageIcon(hoverIconPath);

        JButton button = new JButton(defaultIcon);
        button.setRolloverIcon(hoverIcon); // 마우스 호버 시 이미지 변경
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private JButton createLikeButton(int postId, String defaultIconPath, String toggledIconPath, likeService likeService, userService userService, Connection connection) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon toggledIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + toggledIconPath));

        JButton button = new JButton(isLiked ? toggledIcon : defaultIcon); // 초기 상태 반영
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (userService.getCurrentUser() == null) {
                    JOptionPane.showMessageDialog(null, "로그인이 필요합니다.");
                    return;
                }
                try {
                    isLiked = !isLiked; 
                    if (isLiked) {
                        likeService.addLike(postId, userService.getCurrentUser().getId());
                        likeCount++;
                    } else {
                        likeService.removeLike(postId, userService.getCurrentUser().getId());
                        likeCount--;
                    }
                    updateLikeUI(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    String errorMessage = "좋아요 처리 중 오류가 발생했습니다: " + ex.getMessage();
                    JOptionPane.showMessageDialog(null, errorMessage);
                    try {
                        isLiked = !isLiked;
                        likeCount = likeService.getLikeCount(postId);
                        updateLikeUI();

                    } catch (SQLException ex2){
                        ex2.printStackTrace();
                        JOptionPane.showMessageDialog(null, "오류 복구 중 오류 발생: "+ ex2.getMessage());
                    }
                }
            }
        });
        return button;
    }

    private JButton createBookmarkButton(int postId, String defaultIconPath, String toggledIconPath, userService userService, bookmarkService bookmarks) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon toggledIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + toggledIconPath));

        JButton button = new JButton();
        boolean isBookmarked = false;
        int bookmarkCount = 0;

        try {
            bookmarkCount = bookmarks.getBookmarkCount(postId);
            if (userService.getCurrentUser() != null) {
                int userId = userService.getCurrentUser().getId();
                isBookmarked = bookmarks.isBookmarkedByUser(postId, userId);
            }
            button.setIcon(isBookmarked ? toggledIcon : defaultIcon);
            button.setText(" " + bookmarkCount);
        } catch (SQLException e) {
            e.printStackTrace();
            button.setText("Error");
        }

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);

        if (userService.getCurrentUser() != null) {
            int userId = userService.getCurrentUser().getId();
            boolean finalIsBookmarked = isBookmarked;
            int finalBookmarkCount = bookmarkCount;

            button.addMouseListener(new MouseAdapter() {
                private boolean isToggled = finalIsBookmarked;
                private int count = finalBookmarkCount;

                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        int postOwnerId = bookmarks.getPostOwnerId(postId);
                        if (postOwnerId == userId) {
                            JOptionPane.showMessageDialog(null, "본인의 게시물은 북마크할 수 없습니다.");
                            return;
                        }
                        if (isToggled) {
                            bookmarks.removeBookmark(postId, userId);
                            isToggled = false;
                            count--;
                            button.setIcon(defaultIcon);
                            JOptionPane.showMessageDialog(null, "북마크가 해제되었습니다.");
                        } else {
                            bookmarks.addBookmark(postId, userId);
                            isToggled = true;
                            count++;
                            button.setIcon(toggledIcon);
                            JOptionPane.showMessageDialog(null, "북마크가 추가되었습니다.");
                        }
                        button.setText(" " + count);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "오류가 발생했습니다.");
                    }
                }
            });
        } else {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    JOptionPane.showMessageDialog(null, "로그인이 필요합니다. 로그인 후 북마크를 사용할 수 있습니다.");
                }
            });
        }
        return button;
    }

    private void updateLikeUI() {
        likeButton.setIcon(isLiked ? new ImageIcon(getClass().getResource("/TwitterIcons/likeclicked.png")) : new ImageIcon(getClass().getResource("/TwitterIcons/likedef.png")));
        likeCountLabel.setText(" " + likeCount); // 좋아요 개수 업데이트
    }

    private ImageIcon loadImageIcon(String imagePath) {
        return new ImageIcon(getClass().getResource("/TwitterIcons/" + imagePath));
    }

    public JButton getCommentButton() {
        return commentButton;
    }
}