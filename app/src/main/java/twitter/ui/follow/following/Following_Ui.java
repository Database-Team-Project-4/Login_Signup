package twitter.ui.follow.following;

import twitter.main.MainFrame;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class Following_Ui extends JPanel {
    private static final String[] userNameArray = {
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"
    };
    private static final String[] userHandleArray = {
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"
    };
    ImageIcon profileImage = new ImageIcon(getClass().getResource("/TwitterIcons/icondef.png"));

    public Following_Ui(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());

        // 상단바 추가
        FollowingTopPanel topPanel = new FollowingTopPanel(mainframe);
        add(topPanel, BorderLayout.NORTH);

        // 팔로잉 목록 패널 추가
        List<String> userNames = Arrays.asList(userNameArray);
        List<String> userHandles = Arrays.asList(userHandleArray);
        FollowingListPanel followerListPanel = new FollowingListPanel(userNames, userHandles, profileImage);
        add(followerListPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("팔로잉 목록");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setContentPane(new Following_Ui(null, null, null));
        frame.setVisible(true);
    }
}