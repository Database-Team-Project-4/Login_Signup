package twitter.ui.post;

import javax.swing.*;
import java.awt.*;

public class PostContentPanel extends JPanel {
    public PostContentPanel(String contentText, String postInfo, int likes, int comments, int bookmarks) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
        /*
        POSTUI에 사용되는 중간패널
         */
        // 본문 내용
        JTextArea contentArea = new JTextArea(contentText);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.BLACK);
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // 작성 날짜 및 조회수 라벨
        JLabel postInfoLabel = new JLabel(postInfo);
        postInfoLabel.setForeground(Color.GRAY);
        postInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        postInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        postInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 패널에 추가
        add(contentArea);
        add(postInfoLabel);
    }


    
}
