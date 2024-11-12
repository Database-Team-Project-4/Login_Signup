package twitter.ui;

import twitter.main.MainFrame;

/*
   following_ui에 임시로 사용된 상단바 입니다!
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FollowingTopPanel extends JPanel {
    private JButton backButton;
    private JLabel usernameLabel;

    // 상단바의 배경 색상 및 기본 설정
    private static final Color BACKGROUND_COLOR = new Color(7, 7, 7);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(0, 122, 255);

    public FollowingTopPanel(MainFrame mainframe) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(getWidth(), 40));  // 상단바 높이

        // 상단바 패널 설정
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        // 뒤로 가기 버튼 생성
        backButton = new JButton("<");
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 뒤로 가기 버튼 클릭 시 이전 화면으로 돌아가는 로직 추가
                mainframe.showTwitterMainUiPanel();  // 이전 화면으로 돌아가는 메서드를 호출
            }
        });

        // 사용자 이름 레이블 생성
        usernameLabel = new JLabel("FOLLOWING", SwingConstants.LEFT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
//      usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); //왼쪽에 여백 추가
        // 버튼들을 상단바에 배치
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.setBackground(BACKGROUND_COLOR);
        topButtonPanel.add(backButton);

        // 상단바에 사용자 이름과 팔로우 버튼 추가
        JPanel topContentPanel = new JPanel(new BorderLayout());
        topContentPanel.setBackground(BACKGROUND_COLOR);
        topContentPanel.add(usernameLabel, BorderLayout.CENTER);

        // 상단바 결합
        topPanel.add(topButtonPanel, BorderLayout.WEST);
        topPanel.add(topContentPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
    }
}
