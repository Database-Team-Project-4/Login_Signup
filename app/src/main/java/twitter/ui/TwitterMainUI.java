package twitter.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwitterMainUI extends JPanel {  // JFrame 대신 JPanel로 변경
    private JTextArea timelineArea;
    private JPanel completeTopPanel;
    private JButton homeButton, searchButton, communityButton, messageButton;

    // 아이콘 경로 설정
    private final String homeIconDefault = getClass().getClassLoader().getResource("TwitterIcons/home_icondef.png").getPath();
    private final String homeIconClicked = getClass().getClassLoader().getResource("TwitterIcons/home_iconclicked.png").getPath();
    private final String homeIconHover = getClass().getClassLoader().getResource("TwitterIcons/home_iconcursor.png").getPath();
    private final String searchIconDefault = getClass().getClassLoader().getResource("TwitterIcons/searchdef.png").getPath();
    private final String searchIconHover = getClass().getClassLoader().getResource("TwitterIcons/searchcursor.png").getPath();
    private final String searchIconClicked = getClass().getClassLoader().getResource("TwitterIcons/searchclicked.png").getPath();
    private final String communityIconDefault = getClass().getClassLoader().getResource("TwitterIcons/comdef.png").getPath();
    private final String communityIconHover = getClass().getClassLoader().getResource("TwitterIcons/comcursor.png").getPath();
    private final String communityIconClicked = getClass().getClassLoader().getResource("TwitterIcons/comclicked.png").getPath();
    private final String messageIconDefault = getClass().getClassLoader().getResource("TwitterIcons/messagedef.png").getPath();
    private final String messageIconHover = getClass().getClassLoader().getResource("TwitterIcons/messagecursor.png").getPath();
    private final String messageIconClicked = getClass().getClassLoader().getResource("TwitterIcons/messageclicked.png").getPath();

    public TwitterMainUI() {
        setLayout(new BorderLayout());  // JPanel의 레이아웃 설정

        completeTopPanel = new JPanel(new CardLayout());
        completeTopPanel.add(new MainTopPanel(), "MainTop");
        completeTopPanel.add(new SearchTopPanel(), "SearchTop");
        completeTopPanel.add(new FollowerTopPanel("강동호/AIㆍ소프트웨어학부(인공지능전공)"), "FollowerTop");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(7, 7, 7));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setPreferredSize(new Dimension(getWidth(), 72));

        homeButton = createIconButtonWithHover(homeIconDefault, homeIconHover, homeIconClicked);
        searchButton = createIconButtonWithHover(searchIconDefault, searchIconHover, searchIconClicked);
        communityButton = createIconButtonWithHover(communityIconDefault, communityIconHover, communityIconClicked);
        messageButton = createIconButtonWithHover(messageIconDefault, messageIconHover, messageIconClicked);

        searchButton.addActionListener(e -> showPanel("SearchTop"));
        homeButton.addActionListener(e -> showPanel("MainTop"));
        communityButton.addActionListener(e -> showPanel("FollowerTop"));

        bottomPanel.add(homeButton);
        bottomPanel.add(searchButton);
        bottomPanel.add(communityButton);
        bottomPanel.add(messageButton);

        timelineArea = new JTextArea("Timeline content\n... (More content here) ...\n");
        timelineArea.setEditable(false);
        timelineArea.setBackground(new Color(5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(timelineArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(completeTopPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setBottomButtonSelected(homeButton);
    }

    private void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (completeTopPanel.getLayout());
        cl.show(completeTopPanel, panelName);
    }

    private JButton createIconButtonWithHover(String defaultIconPath, String hoverIconPath, String clickedIconPath) {
        ImageIcon defaultIcon = new ImageIcon(defaultIconPath);
        ImageIcon hoverIcon = new ImageIcon(hoverIconPath);
        ImageIcon clickedIcon = new ImageIcon(clickedIconPath);

        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new IconButtonMouseAdapter(button, hoverIcon, defaultIcon, clickedIcon));
        return button;
    }

    private void resetBottomIcons() {
        homeButton.setIcon(new ImageIcon(homeIconDefault));
        searchButton.setIcon(new ImageIcon(searchIconDefault));
        communityButton.setIcon(new ImageIcon(communityIconDefault));
        messageButton.setIcon(new ImageIcon(messageIconDefault));
    }

    private void setBottomButtonSelected(JButton selectedButton) {
        resetBottomIcons();
        selectedButton.setForeground(Color.WHITE);
    }

    private class IconButtonMouseAdapter extends MouseAdapter {
        private final JButton button;
        private final ImageIcon hoverIcon;
        private final ImageIcon defaultIcon;
        private final ImageIcon clickedIcon;
        private boolean isClicked;

        public IconButtonMouseAdapter(JButton button, ImageIcon hoverIcon, ImageIcon defaultIcon, ImageIcon clickedIcon) {
            this.button = button;
            this.hoverIcon = hoverIcon;
            this.defaultIcon = defaultIcon;
            this.clickedIcon = clickedIcon;
            this.isClicked = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (!isClicked) {
                button.setIcon(hoverIcon);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isClicked) {
                button.setIcon(defaultIcon);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            resetBottomIcons();
            button.setIcon(clickedIcon);
            isClicked = true;
        }
    }
}