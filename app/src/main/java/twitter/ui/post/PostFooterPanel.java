package twitter.ui.post;

import javax.swing.*;
import twitter.main.MainFrame;
import twitter.model.User;
import twitter.service.postService;
import twitter.service.userService;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostFooterPanel extends JPanel {
    private JButton commentButton;
    private final postService postService = new postService();
    private final userService userService;
    private final int postId;
    private JButton likeButton;
    private JLabel likesLabel;
    private static final Logger logger = Logger.getLogger(PostFooterPanel.class.getName());

    public PostFooterPanel(int likes, int comments, int bookmarks, int postId, userService userService) {
        this.postId = postId;
        this.userService = userService;
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(Color.BLACK);

        commentButton = createIconButton("commentdef.png", "commenthover.png");
        commentButton.setText(" " + comments);

        likeButton = createToggleIconButton("likedef.png", "likeclicked.png", likes);

        JButton bookmarkButton = createToggleIconButton("bookmarkdef.png", "bookmarkClicked.png", bookmarks);

        likesLabel = new JLabel(" " + likes);
        likesLabel.setForeground(Color.GRAY);
        likesLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));

        // 좋아요 버튼과 좋아요 개수 레이블을 하나의 패널로 묶습니다.
        JPanel likePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 좌측 정렬, 수평/수직 간격 없음
        likePanel.setBackground(Color.BLACK); // 배경색 일치시키기
        likePanel.add(likeButton);
        likePanel.add(likesLabel);

        add(likePanel); // likePanel을 추가합니다.
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
                User currentUser = userService.getCurrentUser();
                if (currentUser == null) {
                    JOptionPane.showMessageDialog(null, "로그인 후 이용해주세요.", "로그인 필요", JOptionPane.WARNING_MESSAGE);
                    return;
                }
    
                isToggled = !isToggled; // isToggled 변경은 try-catch 블록 밖으로 이동
                try {
                    if (isToggled) {
                        postService.likePost(MainFrame.getConnection(), currentUser, postId);
                    } else {
                        postService.unlikePost(MainFrame.getConnection(), currentUser, postId);
                    }
                    count = postService.getLikeCount(MainFrame.getConnection(), postId); // 좋아요 수를 다시 가져와서 count 갱신
                    button.setText(" " + count);
                    button.setIcon(isToggled ? toggledIcon : defaultIcon);
                    likesLabel.setText(" " + count); // 좋아요 개수 업데이트
    
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, "좋아요 처리 중 오류 발생: " + ex.getMessage(), ex); // 에러 메시지 로그에 기록
                    JOptionPane.showMessageDialog(null, "좋아요 처리 중 오류가 발생했습니다. 이미 좋아요를 눌렀거나, 시스템 오류입니다. 잠시 후 다시 시도해주세요.", "Error", JOptionPane.ERROR_MESSAGE);
                    // isToggled 상태 원상복구
                    isToggled = !isToggled;
                    // UI 갱신
                    try {
                        count = postService.getLikeCount(MainFrame.getConnection(), postId); // 좋아요 수를 다시 가져와서 count 갱신
                        button.setText(" " + count);
                        button.setIcon(isToggled ? toggledIcon : defaultIcon);
                        likesLabel.setText(" " + count); // 좋아요 개수 업데이트
                    } catch (Exception ex2) { // SQLException 대신 다른 예외를 처리
                        logger.log(Level.SEVERE, "좋아요 개수 가져오기 실패: " + ex2.getMessage(), ex2);
                        JOptionPane.showMessageDialog(null, "좋아요 개수를 가져오는 중 오류가 발생했습니다. 관리자에게 문의하세요.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } finally {
                    // 필요에 따라 추가적인 UI 업데이트 로직을 추가할 수 있습니다.
                }
            }
        });
        return button;
    }

    public void setLikes(int likes) {
        likesLabel.setText(" " + likes);
    }
}