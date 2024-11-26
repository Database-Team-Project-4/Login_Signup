package twitter.ui.post;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.imageio.ImageIO;

public class PostContentPanel extends JPanel {
    private static final int IMAGE_WIDTH = 300; // 고정된 이미지 너비
    private static final int IMAGE_HEIGHT = 200; // 고정된 이미지 높이

    public PostContentPanel(String contentText, String postInfo, int likes, int comments, int bookmarks, List<byte[]> images) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

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

        // 이미지 추가 (가운데 정렬)
        if (images != null && !images.isEmpty()) {
            for (byte[] imageBytes : images) {
                try {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    if (img != null) {
                        Image scaledImg = img.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                        ImageIcon icon = new ImageIcon(scaledImg);

                        // 이미지를 감싸는 패널 생성
                        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 이미지 가운데 정렬
                        imagePanel.setBackground(Color.BLACK); // 배경색 설정
                        JLabel imageLabel = new JLabel(icon);
                        imagePanel.add(imageLabel);

                        // 이미지 패널 추가
                        add(imagePanel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 패널에 추가
        add(contentArea);
        add(postInfoLabel);
    }
}
