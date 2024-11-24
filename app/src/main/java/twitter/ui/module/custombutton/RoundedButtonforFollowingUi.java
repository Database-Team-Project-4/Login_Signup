package twitter.ui.module.custombutton;

import javax.swing.*;
import java.awt.*;

/*
    following_ui에 사용된 버튼입니다
 */

public class RoundedButtonforFollowingUi extends JButton {

    public RoundedButtonforFollowingUi() {
        super();
        decorate();
    }

    public RoundedButtonforFollowingUi(String text) {
        super(text);
        decorate();
    }

    public RoundedButtonforFollowingUi(Action action) {
        super(action);
        decorate();
    }

    public RoundedButtonforFollowingUi(Icon icon) {
        super(icon);
        decorate();
    }

    public RoundedButtonforFollowingUi(String text, Icon icon) {
        super(text, icon);
        decorate();
    }

    // 버튼 스타일 설정
    protected void decorate() {
        setBorderPainted(false);  // 기본 테두리 제거
        setOpaque(false);  // 배경 투명
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 색상 변경
        if (getModel().isArmed()) {
            graphics.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            graphics.setColor(Color.darkGray);  // 마우스 롤오버 상태
        } else {
            graphics.setColor(getBackground());  // 기본 상태
        }

        // "실린더" 모양을 그리기 위해 fillRoundRect 사용
        graphics.fillRoundRect(0, 0, width, height, 10, 10);  // 둥글게 만들기

        // 테두리 색상 추가 (어두운 파란색, 두께 2px)
        graphics.setColor(new Color(32, 53, 92));  // 버튼테두리 색상
        graphics.setStroke(new BasicStroke(1));  // 테두리 두께
        graphics.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);  // 둥글게 테두리 그리기

        // 텍스트 그리기
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();

        int textX = (width - stringBounds.width) / 2;
        int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();

        graphics.setColor(getForeground());
        graphics.setFont(getFont());
        graphics.drawString(getText(), textX, textY);
        graphics.dispose();

        super.paintComponent(g);
    }
}
