package twitter.ui.addPost;

import twitter.main.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.awt.event.*;

public class addPostBottomPanel extends JPanel {
    private JButton imageButton;
    private final String imageIconPath = "/TwitterIcons/addImage.png";

    // 하단바의 배경 색상 및 기본 설정
    private static final Color BACKGROUND_COLOR = new Color(7, 7, 7);
    private static final Color TEXT_COLOR = new Color(111, 152, 209);

    private addPostMiddlePanel middlePanel;
    private File selectedImageFile; // 선택된 이미지 파일 저장

    public addPostBottomPanel(MainFrame mainframe, addPostMiddlePanel middlePanel) {
        this.middlePanel = middlePanel;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(getWidth(), 40)); // 하단바 높이 설정

        // 하단바 패널 설정
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);

        // 이미지 아이콘 로드 및 크기 조정
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imageIconPath));
        Image scaledImage = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        // 이미지 삽입 버튼 생성
        imageButton = new JButton(resizedIcon);
        imageButton.setFocusPainted(false);
        imageButton.setBorderPainted(false);
        imageButton.setContentAreaFilled(false);
        imageButton.setOpaque(false);
        imageButton.setBackground(BACKGROUND_COLOR);

        // 아이콘 위치 조정을 위한 마진 설정 (위쪽으로 살짝 이동)
        imageButton.setMargin(new Insets(-4, 4, 0, 0)); // 상단 마진 조정

        // 이미지 버튼 클릭 시 파일 선택
        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fileChooser.getSelectedFile(); // 선택된 파일 저장
                middlePanel.setImage(selectedImageFile);
            }
        });

        // 오른쪽 패널 (답글 가능 문구 추가)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(BACKGROUND_COLOR);
        JLabel rightLabel = new JLabel("Everyone can reply.");
        rightLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        rightLabel.setForeground(TEXT_COLOR);
        rightPanel.add(rightLabel);

        // 왼쪽 패널 (이미지 버튼 추가)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(imageButton);

        // 하단바에 왼쪽과 오른쪽 패널 추가
        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.CENTER);
    }

    // 이미지 버튼에 접근할 수 있도록 메서드 제공
    public JButton getImageButton() {
        return imageButton;
    }

    public File getSelectedImageFile() {
        return selectedImageFile;
    }
}
