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


        // 팔로우 버튼
        followButton = new RoundedRectangleButton("팔로우");
        followButton.setPreferredSize(new Dimension(70, 20));
        followButton.setFont(new Font("SansSerif", Font.PLAIN, 11));

        if (userService.getCurrentUser() != null && userService.getCurrentUser().getId() == displayedUser) {
            hideFollowButton(); // 팔로우 버튼 숨기기
        } else {
            // 로그인 상태 확인
            if (userService.getCurrentUser() == null) {
                followButton.setEnabled(false); // 로그인되지 않은 경우 버튼 비활성화
                followButton.setText("로그인 필요");
            } else {
                // 현재 로그인된 사용자와 표시된 사용자의 팔로우 상태 확인
                this.isFollowing = followService.isAlreadyFollowing(conn, userService.getCurrentUser().getId(), displayedUser);
                updateFollowButtonStyle(); // 초기 스타일 설정

                // 버튼 클릭 이벤트 추가
                followButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isFollowing) {
                            // 언팔로우 동작
                            String result = followService.unfollowUser(conn, userService.getCurrentUser(), displayedUser);
                            JOptionPane.showMessageDialog(null, result); // 결과 메시지 출력
                            isFollowing = false;
                        } else {
                            // 팔로우 동작
                            String result = followService.followUser(conn, userService.getCurrentUser(), displayedUser);
                            JOptionPane.showMessageDialog(null, result); // 결과 메시지 출력
                            isFollowing = true;
                        }
                        updateFollowButtonStyle(); // 스타일 업데이트
                    }
                });

                followButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        if (isFollowing) {
                            // 팔로잉 상태에서 호버
                            followButton.setBackground(Color.GRAY); // 배경색 회색
                            followButton.setForeground(Color.BLACK); // 텍스트 색상 검정
                        } else {
                            // 팔로우 상태에서 호버
                            followButton.setBackground(new Color(180, 180, 180)); // 밝은 회색
                        }
                        followButton.repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        // 마우스가 버튼을 벗어나면 기본 스타일로 복원
                        updateFollowButtonStyle();
                    }

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        // 클릭 시 스타일 변경
                        if (isFollowing) {
                            followButton.setBackground(new Color(100, 100, 100)); // 어두운 회색
                            followButton.setForeground(Color.WHITE); // 텍스트 색상 흰색
                        } else {
                            followButton.setBackground(new Color(150, 150, 150)); // 중간 회색
                        }
                        followButton.repaint();
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        // 클릭 해제 시 기본 스타일 복원
                        updateFollowButtonStyle();
                    }
                });

            }
        }
        // 팔로우 버튼을 감싸는 패널 추가
        JPanel followButtonWrapper = new JPanel(new BorderLayout());
        followButtonWrapper.setBackground(Color.BLACK);
        followButtonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // 상하 여백 설정
        followButtonWrapper.add(followButton, BorderLayout.CENTER);

        // 구성
        add(profilePic, BorderLayout.WEST);
        add(userInfoPanel, BorderLayout.CENTER);
        add(followButtonWrapper, BorderLayout.EAST);
    }

    public UserHeaderPanel(String userNameText, String userHandleText, ImageIcon profileImage) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

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
    public void hideFollowButton() {
        if (followButton != null) {
            followButton.setVisible(false); // 팔로우 버튼 숨기기
        }
    }

    public void addProfileImageMouseListener(MouseListener listener) {
        profilePic.addMouseListener(listener);
    }
    private void updateFollowButtonStyle() {
        if (isFollowing) {
            // 팔로잉 상태
            followButton.setText("팔로잉");
            followButton.setBackground(Color.BLACK); // 배경색 검정
            followButton.setForeground(Color.LIGHT_GRAY); // 텍스트 색상 회색
            followButton.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 테두리 회색
        } else {
            // 팔로우 상태
            followButton.setText("팔로우");
            followButton.setBackground(new Color(200, 200, 200)); // 배경색 밝은 회색
            followButton.setForeground(Color.BLACK); // 텍스트 색상 검정
            followButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // 테두리 밝은 회색
        }

        // 버튼 다시 그리기
        followButton.repaint();
        followButton.revalidate();
    }

}
