package twitter.ui;

import twitter.main.MainFrame;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class Follow_Ui extends JPanel {
    private JScrollPane scrollPane;
    private JPanel mainPanel;

    /*
       팔로잉 목록을 표시하는 UI입니다, mainPanel에 각 팔로잉 계정의 panel을 추가하는 방식으로 구현하였습니다.
     */

    // 예시 데이터 차후 DB연결해야합니다
    private String[] usernames = {"tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content",
            "tvN drama", "KBS DRAMA", "TVING", "쿠팡플레이", "Netflix K-Content"};

    public Follow_Ui(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());

        // 상단바 추가 (FollowingTopPanel)
        FollowingTopPanel topPanel = new FollowingTopPanel(mainframe);
        add(topPanel, BorderLayout.NORTH);

        // 팔로잉 목록 패널 추가
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.BLACK);

        // 팔로잉 항목을 mainPanel에 추가
        for (int i = 0; i < usernames.length; i++) {
            mainPanel.add(createFollowingItem(usernames[i]));
        }

        // 스크롤 가능하게 만들기
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 스크롤 패널 테두리 제거
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar());
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createFollowingItem(String username) {
        // 팔로잉하고있는 계정을 각각 표시해줍니다.
        JPanel panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setLayout(new BorderLayout(10, 10));  // 상하 간격 10px, 좌우 간격 10px로 설정

        /*
        프로필사진추가예정
         */
        

        // 팔로잉 계정 이름 레이블
        JLabel usernameLabel = new JLabel(" " + username);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // 팔로잉 버튼 생성
        RoundedButtonforFollowingUi followingButton = new RoundedButtonforFollowingUi("팔로잉");
        followingButton.setPreferredSize(new Dimension(70, 30));  // 버튼 크기를 적당히 늘림
        followingButton.setBackground(Color.black);
        followingButton.setForeground(Color.WHITE);

        // 팔로잉 버튼 클릭 시 이벤트 (팔로우/언팔로우 기능)
        followingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (followingButton.getText().equals("팔로잉")) {
                    followingButton.setText("언팔로우");
                    followingButton.setBackground(Color.WHITE);
                    followingButton.setForeground(Color.BLACK);
                } else {
                    followingButton.setText("팔로잉");
                    followingButton.setBackground(Color.black);
                    followingButton.setForeground(Color.WHITE);
                }
            }
        });

        // 사용자 이름을 왼쪽에 배치 (CENTER)
        panel.add(usernameLabel, BorderLayout.CENTER);

        // 버튼을 오른쪽에 배치 (EAST)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.black);  // 버튼이 들어가는 패널의 배경색도 설정
        buttonPanel.add(followingButton);  // 버튼을 이 패널에 추가
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(buttonPanel, BorderLayout.EAST);  // 버튼 패널을 EAST로 배치

        return panel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("팔로잉 목록");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setContentPane(new Follow_Ui(null, null, null));
        frame.setVisible(true);
    }
}
