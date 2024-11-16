package twitter.ui.addPost;

import twitter.main.MainFrame;
import twitter.ui.module.CustomScrollbar;

import javax.swing.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class addPostMiddlePanel extends JPanel {
    // 상수 설정: 기본 텍스트 및 색상
    private JTextArea postTextArea;
    private static final String DEFAULT_TEXT = "What's happening?";
    private static final Color BACKGROUND_COLOR = new Color(7, 7, 7);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color PLACEHOLDER_COLOR = Color.GRAY;

    // 이미지 경로 및 UI 요소 선언
    private final String imageIconPath = "/TwitterIcons/X.png";
    private JScrollPane scrollPane;
    private JButton postButton;
    private JLabel imagePreviewLabel;
    private ImageIcon selectedImageIcon;
    private JButton removeImageButton;

    public addPostMiddlePanel(MainFrame mainframe, JButton postButton) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // 게시글 입력 영역 생성
        createTextArea();

        // 스크롤 생성
        createScrollPane();

        // 이미지 미리보기 및 삭제 버튼 생성
        createImageComponents();

        // 레이아웃 설정 및 패널 구성
        configureLayout();

        // Post 버튼 참조 설정
        this.postButton = postButton;
        postButton.setEnabled(false);
    }

    /*
      게시글 입력을 위한 텍스트 영역 생성
     */
    private void createTextArea() {
        postTextArea = new JTextArea(10, 30);
        postTextArea.setLineWrap(true);
        postTextArea.setWrapStyleWord(true);
        postTextArea.setForeground(PLACEHOLDER_COLOR);
        postTextArea.setBackground(BACKGROUND_COLOR);
        postTextArea.setCaretColor(TEXT_COLOR);
        postTextArea.setFont(new Font("SansSerif", Font.BOLD, 17));
        postTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        postTextArea.setText(DEFAULT_TEXT);

        // 텍스트 영역에 포커스 리스너 추가
        postTextArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 포커스가 생기면 기본 텍스트 제거
                if (postTextArea.getText().equals(DEFAULT_TEXT)) {
                    postTextArea.setText("");
                    postTextArea.setForeground(TEXT_COLOR);
                }
                updatePostButtonState();
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 텍스트가 비어 있으면 기본 텍스트 복원
                if (postTextArea.getText().trim().isEmpty()) {
                    postTextArea.setText(DEFAULT_TEXT);
                    postTextArea.setForeground(PLACEHOLDER_COLOR);
                }
                updatePostButtonState();
            }
        });

        // 텍스트 변경 시 Post 버튼 상태 업데이트
        postTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updatePostButtonState(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updatePostButtonState(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updatePostButtonState(); }
        });
    }

    /*
      스크롤 패널 생성
     */
    private void createScrollPane() {
        scrollPane = new JScrollPane(postTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar());
    }

    /*
      이미지 미리보기 및 삭제 버튼 생성
     */
    private void createImageComponents() {
        // 이미지 미리보기 라벨 생성
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setHorizontalAlignment(JLabel.CENTER);
        imagePreviewLabel.setVerticalAlignment(JLabel.CENTER);
        imagePreviewLabel.setPreferredSize(new Dimension(170, 170));
        imagePreviewLabel.setBackground(BACKGROUND_COLOR);
        imagePreviewLabel.setOpaque(true);

        // 삭제 버튼 생성 및 아이콘 설정
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imageIconPath));
        Image scaledImage = originalIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        removeImageButton = new JButton(resizedIcon);
        removeImageButton.setFocusPainted(false);
        removeImageButton.setBorderPainted(false);
        removeImageButton.setContentAreaFilled(false);
        removeImageButton.setOpaque(false);
        removeImageButton.setBackground(BACKGROUND_COLOR);
        removeImageButton.setVisible(false);

        // 삭제 버튼 클릭 시 이미지 제거
        removeImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearImage();
            }
        });
    }

    /*
      레이아웃 설정 및 구성
     */
    private void configureLayout() {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new GridBagLayout());
        layeredPane.setPreferredSize(new Dimension(200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // 이미지 미리보기 추가
        imagePreviewLabel.setPreferredSize(new Dimension(170, 170));
        layeredPane.add(imagePreviewLabel, gbc, 0);

        // 삭제 버튼 위치 설정
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(-25, 5, -10, -5);
        removeImageButton.setPreferredSize(new Dimension(30, 30));
        layeredPane.add(removeImageButton, gbc, 1);

        JPanel contentsPanel = new JPanel(new BorderLayout());
        contentsPanel.setBackground(BACKGROUND_COLOR);
        contentsPanel.add(scrollPane, BorderLayout.NORTH);
        contentsPanel.add(layeredPane, BorderLayout.CENTER);

        add(contentsPanel, BorderLayout.CENTER);
    }

    /*
      이미지 파일 설정 및 미리보기 표시
     */
    public void setImage(File imageFile) {
        if (imageFile != null) {
            selectedImageIcon = new ImageIcon(new ImageIcon(imageFile.getAbsolutePath())
                    .getImage()
                    .getScaledInstance(170, 170, Image.SCALE_SMOOTH));
            imagePreviewLabel.setIcon(selectedImageIcon);
            removeImageButton.setVisible(true);
        } else {
            clearImage();
        }
        updatePostButtonState();
    }

    /*
      이미지 제거 메소드
     */
    public void clearImage() {
        imagePreviewLabel.setIcon(null);
        selectedImageIcon = null;
        removeImageButton.setVisible(false);
        updatePostButtonState();
    }

    /*
      게시글 내용을 가져오는 메소드
     */
    public String getPostContent() {
        String content = postTextArea.getText().trim();
        if (content.equals(DEFAULT_TEXT)) {
            return "";
        }
        return content;
    }

    /*
     게시글 내용을 초기화하는 메소드
     */
    public void clearPostContent() {
        postTextArea.setText(DEFAULT_TEXT);
        postTextArea.setForeground(PLACEHOLDER_COLOR);
        clearImage();
        updatePostButtonState();
    }

    /*
      Post 버튼 활성화/비활성화 상태 업데이트
     */
    public void updatePostButtonState() {
        String content = postTextArea.getText().trim();
        boolean isDefaultOrEmpty = content.equals(DEFAULT_TEXT) || content.isEmpty();
        postButton.setEnabled(!isDefaultOrEmpty || selectedImageIcon != null);
    }
}
