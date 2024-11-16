package twitter.ui.module;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/*
Follow_ui에 사용되는 custom scrollbar입니다.
 */

public class CustomScrollbar extends BasicScrollBarUI {
    private static final int THUMB_SIZE = 8;
    private static final Color THUMB_COLOR = Color.WHITE; // 스크롤 thumb 기본 색상
    private static final Color THUMB_HOVER_COLOR = new Color(200, 200, 200); // hover 시 색상
    private static final Color TRACK_COLOR = Color.BLACK; // 트랙(배경) 색상

    private boolean isThumbHovered = false;

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = THUMB_COLOR;
        trackColor = TRACK_COLOR;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (!thumbBounds.isEmpty() && scrollbar.isEnabled()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 마우스 hover 상태에 따라 thumb 색상 변경
            Color color = isThumbHovered ? THUMB_HOVER_COLOR : THUMB_COLOR;
            g2.setColor(color);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
            g2.dispose();
        }
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // 트랙(배경)을 검은색으로 설정
        g.setColor(TRACK_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        scrollbar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                isThumbHovered = true;
                scrollbar.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                isThumbHovered = false;
                scrollbar.repaint();
            }
        });
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(THUMB_SIZE, THUMB_SIZE);
    }

    @Override
    protected Dimension getMaximumThumbSize() {
        return new Dimension(THUMB_SIZE, THUMB_SIZE);
    }
}
