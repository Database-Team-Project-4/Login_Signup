package twitter.ui.module.custombutton;

import javax.swing.*;
import java.awt.*;

public class MoreButton extends JButton {
    public MoreButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setBorderPainted(false); // 테두리 제거
        setMargin(new Insets(5, 0, 20, 0)); // 하단 외부 패딩 추가
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // 안티앨리어싱 적용
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 버튼 배경 그리기 (좌우 반원형 형태)
        g2.setColor(getBackground());
        int radius = getHeight(); // 버튼 높이를 기준으로 반원의 반지름 설정
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        // 버튼 텍스트 그리기
        Font originalFont = getFont();
        Font resizedFont = originalFont.deriveFont(originalFont.getSize2D() - 1); // 텍스트 크기 1px 감소
        g2.setFont(resizedFont);
        FontMetrics fm = g2.getFontMetrics();
        Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // 테두리 그리지 않음
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension original = super.getPreferredSize();
        return new Dimension(original.width, original.height - 2); // 텍스트 크기 줄임에 따라 버튼 크기 줄임
    }
}
