package twitter.ui.follow.following;

import twitter.ui.module.CustomScrollbar;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FollowingListPanel extends JPanel {
    private JScrollPane scrollPane;
    private JPanel mainPanel;

    public FollowingListPanel(List<String> userNames, List<String> userHandles, ImageIcon profileImage) {
        setLayout(new BorderLayout());

        // 팔로워 목록을 담을 메인 패널 설정
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);

        // 팔로워 항목 추가
        for (int i = 0; i < userNames.size(); i++) {
            Following_user_HeaderPanel userPanel = new Following_user_HeaderPanel(userNames.get(i), userHandles.get(i), profileImage);
            mainPanel.add(userPanel);
        }

        // 스크롤 가능하게 설정
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar());

        add(scrollPane, BorderLayout.CENTER);
    }
}

