package twitter.ui.post;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PostFooterPanel extends JPanel {
    public PostFooterPanel(int likes, int comments, int bookmarks) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        setBackground(Color.BLACK);

        // 댓글 버튼 (댓글 수 표시)
        JButton commentButton = createIconButton("commentdef.png", "commenthover.png");
        commentButton.setText(" " + comments);

        // 좋아요 버튼 (좋아요 수 표시, 토글 기능)
        JButton likeButton = createToggleIconButton("likedef.png", "likeclicked.png", likes);

        // 북마크 버튼 (북마크 수 표시, 토글 기능)
        JButton bookmarkButton = createToggleIconButton("bookmarkdef.png", "bookmarkClicked.png", bookmarks);

        // 버튼 추가
        add(commentButton);
        add(likeButton);
        add(bookmarkButton);
    }

    // 댓글 버튼 생성 메서드
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

    // 토글 가능한 아이콘 버튼 생성 메서드 (좋아요 및 북마크용, 수 증가/감소 기능 포함)
    private JButton createToggleIconButton(String defaultIconPath, String toggledIconPath, int initialCount) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + defaultIconPath));
        ImageIcon toggledIcon = new ImageIcon(getClass().getResource("/TwitterIcons/" + toggledIconPath));

        JButton button = new JButton(defaultIcon);
        button.setText(" " + initialCount);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // 텍스트 오른쪽 배치

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
}
