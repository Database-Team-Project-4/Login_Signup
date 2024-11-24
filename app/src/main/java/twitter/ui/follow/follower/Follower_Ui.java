package twitter.ui.follow.follower;

import twitter.main.MainFrame;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class Follower_Ui extends JPanel {
    private static final String[] userNameArray = {
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"
    };
    private static final String[] userHandleArray = {
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"
    };
    ImageIcon profileImage = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));

    public Follower_Ui(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());

        // 상단바 추가
        FollowerTopPanel topPanel = new FollowerTopPanel(mainframe);
        add(topPanel, BorderLayout.NORTH);

        // 팔로워 목록 패널 추가
        List<String> userNames = Arrays.asList(userNameArray);
        List<String> userHandles = Arrays.asList(userHandleArray);
        FollowerListPanel followerListPanel = new FollowerListPanel(userNames, userHandles, profileImage);
        add(followerListPanel, BorderLayout.CENTER);
    }
}