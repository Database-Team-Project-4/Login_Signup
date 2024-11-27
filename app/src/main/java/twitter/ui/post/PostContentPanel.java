package twitter.ui.post;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.imageio.ImageIO;

public class PostContentPanel extends JPanel {
    public PostContentPanel(String contentText, String postInfo, int likes, int comments, int bookmarks, List<byte[]> images) {
    int IMAGE_WIDTH = 300; // 고정된 이미지 너비
    int IMAGE_HEIGHT = 200; // 고정된 이미지 높이
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        // 본문 텍스트 설정

        // 본문 내용
        JTextArea contentArea = new JTextArea(contentText);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.BLACK);
        contentArea.setForeground(Color.WHITE);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // 본문 텍스트 패널에 추가
        add(contentArea);

        // 이미지가 있을 경우 처리
        if (images != null && !images.isEmpty()) {
            for (byte[] imageData : images) {
                // 이미지 크기 조절 (필요 시)
                ImageIcon imageIcon = new ImageIcon(imageData);
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(300, -1, Image.SCALE_SMOOTH); // 너비 300으로 조정, 높이는 비율 유지
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                JLabel imageLabel = new JLabel(scaledIcon);

                // 이미지 라벨을 감싸는 패널 생성 (가운데 정렬)
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                imagePanel.setBackground(Color.BLACK); // 배경색 설정
                imagePanel.add(imageLabel);

                // 이미지 패널의 최대 크기 설정 (BoxLayout이 가로로 늘어나지 않도록)
                imagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, scaledImage.getHeight(null) + 20)); // 여백 포함

                // 이미지 패널의 alignmentX를 LEFT_ALIGNMENT로 설정
                imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // 이미지 패널에 상하 여백 추가
                imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

                // 이미지 패널을 메인 패널에 추가
                add(imagePanel);
            }
        }

        // 작성 날짜 및 조회수 라벨 설정
        JLabel postInfoLabel = new JLabel(postInfo);
        postInfoLabel.setForeground(Color.GRAY);
        postInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        postInfoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        postInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // 왼쪽 정렬

        // 작성 날짜 및 조회수 패널에 추가
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
