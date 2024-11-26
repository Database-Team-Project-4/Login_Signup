package twitter.ui.follow.follower;

import twitter.ui.module.CustomScrollbar;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FollowerListPanel extends JPanel {
    private JScrollPane scrollPane;
    private JPanel mainPanel;

    public FollowerListPanel(List<String> userNames, List<String> userHandles, ImageIcon profileImage) {
        setLayout(new BorderLayout());

        // 팔로워 목록을 담을 메인 패널 설정
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);

        // 팔로워 항목 추가
        for (int i = 0; i < userNames.size(); i++) {
            Follower_user_HeaderPanel userPanel = new Follower_user_HeaderPanel(userNames.get(i), userHandles.get(i), profileImage);
            mainPanel.add(userPanel);
        }

        // 팔로워 목록의 전체 높이를 계산
        int itemCount = userNames.size();
        int itemHeight = 70; // 각 항목의 예상 높이
        int maxVisibleHeight = 540; // 최대 표시 가능한 높이
        int totalHeight = itemCount * itemHeight;

        // 스크롤 필요 여부 결정
        boolean scrollNeeded = totalHeight > maxVisibleHeight;

        mainPanel.setPreferredSize(new Dimension(400, totalHeight));

        // 메인 패널을 감싸는 래퍼 패널
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setPreferredSize(new Dimension(400, Math.min(totalHeight, maxVisibleHeight)));
        wrapperPanel.setMaximumSize(new Dimension(400, Math.min(totalHeight, maxVisibleHeight)));
        wrapperPanel.setMinimumSize(new Dimension(400, Math.min(totalHeight, maxVisibleHeight)));
        wrapperPanel.setBackground(Color.BLACK);
        wrapperPanel.add(mainPanel, BorderLayout.CENTER);

        // 스크롤 패널 생성
        scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(scrollNeeded ? JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED : JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar());

        add(scrollPane, BorderLayout.CENTER);
    }
}

