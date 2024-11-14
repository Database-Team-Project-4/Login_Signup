package twitter.ui.module.custombutton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class RoundedRectangleButton extends JButton {
    private Color defaultBackground = Color.WHITE; 
    private Color hoverBackground = Color.LIGHT_GRAY;
    private Color clickedBackground = Color.GRAY;

    /*
    POSTUI에 사용되는 팔로우 버튼 모양
     */
    public RoundedRectangleButton(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(defaultBackground);
        setForeground(Color.BLACK);
        setFont(new Font("SansSerif", Font.BOLD, getFont().getSize() + 1)); // 텍스트 크기 1포인트 증가 및 BOLD 적용
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);

        // Mouse listener for hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackground);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(defaultBackground);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(clickedBackground);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackground);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 양쪽 끝만 둥근 직사각형 배경
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 양쪽 끝만 둥근 직사각형 테두리
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
        g2.dispose();
    }
}
