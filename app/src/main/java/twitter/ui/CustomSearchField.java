package twitter.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CustomSearchField extends JPanel {
    private JTextField searchField;
    private JLabel iconLabel;
    private JLabel placeholderLabel;
    private JPanel overlayPanel;

    public CustomSearchField() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(250, 30));

        String placeholderText = "검색어를 입력하세요...";
        String iconPath = "/TwitterIcons/searchdef.png";

        iconLabel = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
        iconLabel.setForeground(Color.LIGHT_GRAY);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));

        placeholderLabel = new JLabel(placeholderText);
        placeholderLabel.setForeground(Color.LIGHT_GRAY);
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        overlayPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        overlayPanel.setOpaque(false);
        overlayPanel.add(iconLabel);
        overlayPanel.add(placeholderLabel);

        add(overlayPanel, BorderLayout.CENTER);

        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        searchField.setOpaque(false);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setHorizontalAlignment(JTextField.CENTER);
        searchField.setVisible(false);
        add(searchField, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                overlayPanel.setVisible(false);
                searchField.setVisible(true);
                searchField.requestFocus();
            }
        });

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
        g.setColor(new Color(50, 50, 50));  // 진한 회색 배경으로 테스트
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
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
