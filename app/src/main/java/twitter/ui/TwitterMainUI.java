package twitter.ui;
import twitter.ui.TopBars.FollowerTopPanel;
import twitter.ui.TopBars.MainTopPanel;
import twitter.ui.TopBars.SearchTopPanel;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TwitterMainUI extends JFrame {
    private JTextArea timelineArea;
    private JPanel completeTopPanel;
    private JButton homeButton, searchButton, communityButton, messageButton;

    private final String homeIconDefault = "/twitter/ui/TwitterIcons/home_icondef.png";
 private final String homeIconClicked = "/twitter/ui/TwitterIcons/home_iconclicked.png";
private final String homeIconHover = "/twitter/ui/TwitterIcons/home_iconcursor.png";
private final String searchIconDefault = "/twitter/ui/TwitterIcons/searchdef.png";
private final String searchIconHover = "/twitter/ui/TwitterIcons/searchcursor.png";
private final String searchIconClicked = "/twitter/ui/TwitterIcons/searchclicked.png";
private final String communityIconDefault = "/twitter/ui/TwitterIcons/comdef.png";
private final String communityIconHover = "/twitter/ui/TwitterIcons/comcursor.png";
private final String communityIconClicked = "/twitter/ui/TwitterIcons/comclicked.png";
private final String messageIconDefault = "/twitter/ui/TwitterIcons/messagedef.png";
private final String messageIconHover = "/twitter/ui/TwitterIcons/messagecursor.png";
private final String messageIconClicked = "/twitter/ui/TwitterIcons/messageclicked.png";

    public TwitterMainUI() {
        setTitle("Twitter Main Screen");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TwitterMainUI ui = new TwitterMainUI();
            ui.setVisible(true);
        });
    }
}
