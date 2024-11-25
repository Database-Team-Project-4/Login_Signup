package twitter.ui.post;

import twitter.service.bookmarkService;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class PostFooterPanel extends JPanel {
    private JButton commentButton;

    public PostFooterPanel(int likes, int comments, int postId, int userId, userService userService, Connection connection) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(Color.BLACK);

        // 댓글 버튼 (댓글 수 표시)
        commentButton = createIconButton("commentdef.png", "commenthover.png");
        commentButton.setText(" " + comments);

        // 좋아요 버튼 (좋아요 수 표시, 토글 기능)
        JButton likeButton = createToggleIconButton("likedef.png", "likeclicked.png", likes);

        // 북마크 버튼 (북마크 수 표시, 토글 기능)

        bookmarkService bookmarks = new bookmarkService(connection);

        JButton bookmarkButton = createBookmarkButton(postId, "bookmarkdef.png", "bookmarkClicked.png", userService, bookmarks);


        // 버튼 추가
        add(commentButton);
        add(likeButton);
        add(bookmarkButton);
    }

    // 댓글 버튼 가져오기
    // PostFooterPanel 클래스에 댓글 버튼 반환 메서드 추가
    public JButton getCommentButton() {
        return commentButton; // 댓글 버튼 반환
    }


    private JButton createIconButton(String defaultIconPath, String hoverIconPath) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + hoverIconPath));

        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // 텍스트 오른쪽 배치

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

    private JButton createToggleIconButton(String defaultIconPath, String toggledIconPath, int initialCount) {
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
                isToggled = !isToggled;
                count = isToggled ? count + 1 : count - 1;
                button.setText(" " + count);
                button.setIcon(isToggled ? toggledIcon : defaultIcon);
            }
        });
        return button;
    }

// 북마크 버튼 생성 메서드 추가
private JButton createBookmarkButton(int postId, String defaultIconPath, String toggledIconPath, userService userService, bookmarkService bookmarks) {
    // 기본 아이콘 설정
    ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
    ImageIcon toggledIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + toggledIconPath));

    JButton button = new JButton();

    // 초기 값 설정 (북마크 상태와 북마크 수 표시)
    boolean isBookmarked = false; // 기본값
    int bookmarkCount = 0; // 기본값

    try {
        // 데이터베이스에서 북마크 수를 가져옴
        bookmarkCount = bookmarks.getBookmarkCount(postId);

        // 로그인된 경우 북마크 상태 확인
        if (userService.getCurrentUser() != null) {
            int userId = userService.getCurrentUser().getId();
            isBookmarked = bookmarks.isBookmarkedByUser(postId, userId);
        }

        // 버튼 아이콘과 텍스트 설정
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

    // 버튼 클릭 이벤트 (로그인된 경우에만 추가)
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
                    // 사용자가 본인 게시물을 북마크하려는지 확인
                    int postOwnerId = bookmarks.getPostOwnerId(postId);
                    if (postOwnerId == userId) {
                        JOptionPane.showMessageDialog(null, "본인의 게시물은 북마크할 수 없습니다.");
                        return;
                    }

                    // 북마크 추가/삭제 처리
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
        // 로그인되지 않은 경우 클릭 이벤트 비활성화
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "로그인이 필요합니다. 로그인 후 북마크를 사용할 수 있습니다.");
            }
        });
    }

    return button;
}


}
