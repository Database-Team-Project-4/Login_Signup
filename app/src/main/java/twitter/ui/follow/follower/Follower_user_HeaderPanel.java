package twitter.ui.follow.follower;

import twitter.ui.module.custombutton.RoundedRectangleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Follower_user_HeaderPanel extends JPanel {
    public Follower_user_HeaderPanel(String userNameText, String userHandleText, ImageIcon profileImage) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));
        setMaximumSize(new Dimension(400, 70));

        /*
        Follower에 사용되는 중간패널입니다, 버튼 눌렀을때 event를 넣기 편하기위해 post의
        UserHeaderPanel과 분리했습니다
         */

        // 프로필 사진 (크기 조정: 1.8배)
        Image scaledImage = profileImage.getImage().getScaledInstance(
            (int) (profileImage.getIconWidth() * 1.8),
            (int) (profileImage.getIconHeight() * 1.8),
            Image.SCALE_SMOOTH
        );
        JLabel profilePic = new JLabel(new ImageIcon(scaledImage));
        profilePic.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        // 사용자 정보 패널
        JPanel userInfoPanel = new JPanel(new GridLayout(2, 1));
        userInfoPanel.setBackground(Color.BLACK);

        JLabel userName = new JLabel(userNameText);
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("SansSerif", Font.BOLD, 14));

        JLabel userHandle = new JLabel(userHandleText);
        userHandle.setForeground(Color.GRAY);
        userHandle.setFont(new Font("SansSerif", Font.PLAIN, 12));

        userInfoPanel.add(userName);
        userInfoPanel.add(userHandle);
        /*
        // 팔로우 버튼을 감싸는 패널 추가
        JPanel followButtonWrapper = new JPanel(new BorderLayout());
        followButtonWrapper.setBackground(Color.BLACK);
        followButtonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 상하 여백 설정

        // 팔로우 버튼
        JButton followButton = new RoundedRectangleButton("팔로우"); //팔로잉을 하고있다는 가정
        followButton.setPreferredSize(new Dimension(70, 20));
        followButton.setFont(new Font("SansSerif", Font.PLAIN, 11));

        // 팔로잉 버튼 클릭 시 이벤트 (팔로우/언팔로우 기능)
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            //FollowService의 기능을 넣으면 될것 같습니다.
            }
        });

        followButtonWrapper.add(followButton, BorderLayout.CENTER);
        */
        // 구성
        add(profilePic, BorderLayout.WEST);
        add(userInfoPanel, BorderLayout.CENTER);
        //add(followButtonWrapper, BorderLayout.EAST);
    }
}
