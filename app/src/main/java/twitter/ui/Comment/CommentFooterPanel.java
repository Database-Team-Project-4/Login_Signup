package twitter.ui.Comment;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CommentFooterPanel extends JPanel {
    public CommentFooterPanel(int likes) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
        setBackground(Color.BLACK);

        JButton likeButton = createLikeButton("/TwitterIcons/likedef.png", "/TwitterIcons/likeclicked.png", likes);
        add(likeButton);
    }

    private JButton createLikeButton(String defaultIconPath, String toggledIconPath, int initialCount) {
        ImageIcon defaultIcon = new ImageIcon(getClass().getResource(defaultIconPath));
        ImageIcon toggledIcon = new ImageIcon(getClass().getResource(toggledIconPath));

        JButton button = new JButton(defaultIcon);
        button.setText(" " + initialCount);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);

        button.addMouseListener(new MouseAdapter() {
            private boolean toggled = false;
            private int count = initialCount;

            @Override
            public void mousePressed(MouseEvent e) {
                toggled = !toggled;
                count = toggled ? count + 1 : count - 1;
                button.setText(" " + count);
                button.setIcon(toggled ? toggledIcon : defaultIcon);
            }
        });
        return button;
    }
}