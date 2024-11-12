package twitter.ui.main;

import twitter.main.MainFrame;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

public class Main_Ui extends JPanel {  // JFrame 대신 JPanel로 변경
    private JTextArea timelineArea;
    private JPanel completeTopPanel;
    private JButton homeButton, searchButton, communityButton, messageButton;

    // 아이콘 경로 설정
    private final String homeIconDefault = "/TwitterIcons/home_icondef.png";
    private final String homeIconClicked = "/TwitterIcons/home_iconclicked.png";
    private final String homeIconHover = "/TwitterIcons/home_iconcursor.png";
    private final String searchIconDefault = "/TwitterIcons/searchdef.png";
    private final String searchIconHover = "/TwitterIcons/searchcursor.png";
    private final String searchIconClicked = "/TwitterIcons/searchclicked.png";
    private final String communityIconDefault = "/TwitterIcons/comdef.png";
    private final String communityIconHover = "/TwitterIcons/comcursor.png";
    private final String communityIconClicked = "/TwitterIcons/comclicked.png";
    private final String messageIconDefault = "/TwitterIcons/messagedef.png";
    private final String messageIconHover = "/TwitterIcons/messagecursor.png";
    private final String messageIconClicked = "/TwitterIcons/messageclicked.png";

    public Main_Ui(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());

        completeTopPanel = new JPanel(new CardLayout());
        completeTopPanel.add(new MainTopPanel(mainframe, connection, userService), "MainTop");
        completeTopPanel.add(new SearchTopPanel(mainframe, connection, userService), "SearchTop");
        completeTopPanel.add(new FollowerTopPanel(mainframe, connection, userService), "FollowerTop");

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
        ImageIcon defaultIcon = loadIcon(defaultIconPath);
        ImageIcon hoverIcon = loadIcon(hoverIconPath);
        ImageIcon clickedIcon = loadIcon(clickedIconPath);

        JButton button = new JButton(defaultIcon);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new IconButtonMouseAdapter(button, hoverIcon, defaultIcon, clickedIcon));
        return button;
    }

    private ImageIcon loadIcon(String iconPath) {
        java.net.URL resource = getClass().getResource(iconPath);
        System.out.println("Loading icon: " + iconPath + " | URL: " + resource);
        if (resource != null) {
            return new ImageIcon(resource);
        } else {
            System.err.println("Icon not found: " + iconPath);
            return null;
        }
    }

    private void resetBottomIcons() {
        homeButton.setIcon(loadIcon(homeIconDefault));
        searchButton.setIcon(loadIcon(searchIconDefault));
        communityButton.setIcon(loadIcon(communityIconDefault));
        messageButton.setIcon(loadIcon(messageIconDefault));
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
            if (!isClicked && hoverIcon != null) {
                button.setIcon(hoverIcon);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (!isClicked && defaultIcon != null) {
                button.setIcon(defaultIcon);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            resetBottomIcons();
            if (clickedIcon != null) {
                button.setIcon(clickedIcon);
            }
            isClicked = true;
        }
    }
}
