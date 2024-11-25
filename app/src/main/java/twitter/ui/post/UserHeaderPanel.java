package twitter.ui.post;

import twitter.model.User;
import twitter.service.followService;
import twitter.service.userService;
import twitter.ui.module.custombutton.RoundedRectangleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.Connection;

public class UserHeaderPanel extends JPanel {
    private JLabel profilePic;
    private JButton followButton; // 팔로우 버튼
    private boolean isFollowing; // 팔로잉 여부
    private followService followService; // 팔로우/언팔로우 처리 서비스 객체


    public UserHeaderPanel(String userNameText, String userHandleText, ImageIcon profileImage, int displayedUser, userService userService, Connection conn) {
        this.followService = new followService(); // 팔로우 서비스 객체 초기화

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        /*
        POSTUI에 사용되는 header panel
         */

        // 프로필 사진 (크기 조정: 1.8배)
        Image scaledImage = profileImage.getImage().getScaledInstance(
                (int) (profileImage.getIconWidth() * 1.8),
                (int) (profileImage.getIconHeight() * 1.8),
                Image.SCALE_SMOOTH
        );
        profilePic = new JLabel(new ImageIcon(scaledImage));
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

        // 팔로우 버튼을 감싸는 패널 추가
        JPanel followButtonWrapper = new JPanel(new BorderLayout());
        followButtonWrapper.setBackground(Color.BLACK);
        followButtonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 상하 여백 설정

        // 팔로우 버튼
        followButton = new RoundedRectangleButton("팔로우");
        followButton.setPreferredSize(new Dimension(70, 20));
        followButton.setFont(new Font("SansSerif", Font.PLAIN, 11));

        // 로그인 상태 확인
        if (userService.getCurrentUser() == null) {
            followButton.setEnabled(false); // 로그인되지 않은 경우 버튼 비활성화
            followButton.setText("로그인 필요");
        } else {
            // 현재 로그인된 사용자와 표시된 사용자의 팔로우 상태 확인
            this.isFollowing = followService.isAlreadyFollowing(conn, userService.getCurrentUser().getId(), displayedUser);
            followButton.setText(isFollowing ? "팔로잉" : "팔로우");

            // 버튼 클릭 이벤트 추가
            followButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isFollowing) {
                        // 언팔로우 동작
                        String result = followService.unfollowUser(conn, userService.getCurrentUser(), displayedUser);
                        JOptionPane.showMessageDialog(null, result); // 결과 메시지 출력
                        isFollowing = false;
                        followButton.setText("팔로우");
                    } else {
                        // 팔로우 동작
                        String result = followService.followUser(conn, userService.getCurrentUser(), displayedUser);
                        JOptionPane.showMessageDialog(null, result); // 결과 메시지 출력
                        isFollowing = true;
                        followButton.setText("팔로잉");
                    }
                }
            });
        }

        followButtonWrapper.add(followButton, BorderLayout.CENTER);

        // 구성
        add(profilePic, BorderLayout.WEST);
        add(userInfoPanel, BorderLayout.CENTER);
        add(followButtonWrapper, BorderLayout.EAST);
    }

    public UserHeaderPanel(String userNameText, String userHandleText, ImageIcon profileImage) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        /*
        POSTUI에 사용되는 header panel
         */

        // 프로필 사진 (크기 조정: 1.8배)
        Image scaledImage = profileImage.getImage().getScaledInstance(
                (int) (profileImage.getIconWidth() * 1.8),
                (int) (profileImage.getIconHeight() * 1.8),
                Image.SCALE_SMOOTH
        );
        profilePic = new JLabel(new ImageIcon(scaledImage));
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



        // 구성
        add(profilePic, BorderLayout.WEST);
        add(userInfoPanel, BorderLayout.CENTER);

    }

    public void addProfileImageMouseListener(MouseListener listener) {
        profilePic.addMouseListener(listener);
    }
}
