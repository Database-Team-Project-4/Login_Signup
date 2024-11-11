package twitter.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomSearchField extends JPanel {
    private JTextField searchField;
    private JLabel iconLabel;
    private JLabel placeholderLabel;
    private JPanel overlayPanel;

    public CustomSearchField() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(250, 30));

        // 내부에서 정의된 placeholderText와 iconPath
        String placeholderText = "검색어를 입력하세요...";
        String iconPath = "TwitterIcons/magnifying_icon.png";  // 실제 경로로 변경 필요

        // 돋보기 아이콘 설정
        iconLabel = new JLabel(new ImageIcon(iconPath));
        iconLabel.setForeground(Color.LIGHT_GRAY);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));

        // placeholder 설정
        placeholderLabel = new JLabel(placeholderText);
        placeholderLabel.setForeground(Color.WHITE);
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // overlay 설정 : 아이콘과 placeholder를 중앙에 배치
        overlayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        overlayPanel.setOpaque(false);
        overlayPanel.add(iconLabel);
        overlayPanel.add(placeholderLabel);
        add(overlayPanel, BorderLayout.CENTER);

        // 검색 필드 설정
        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        searchField.setOpaque(false);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setHorizontalAlignment(JTextField.CENTER);
        searchField.setVisible(false); // 처음에는 숨김 상태
        add(searchField, BorderLayout.CENTER);

        // 검색창 클릭 시 overlay 숨기고 검색 필드 표시
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                overlayPanel.setVisible(false);
                searchField.setVisible(true);
                searchField.requestFocus();
            }
        });

        // 검색 필드 포커스 해제 시 overlay 표시
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    overlayPanel.setVisible(true);
                    searchField.setVisible(false);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // 테두리 비활성화
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void setSearchText(String text) {
        searchField.setText(text);
        boolean hasText = !text.isEmpty();
        overlayPanel.setVisible(!hasText);
        searchField.setVisible(hasText);
    }
}