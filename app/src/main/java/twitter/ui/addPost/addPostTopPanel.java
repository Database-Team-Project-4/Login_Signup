package twitter.ui.addPost;

import twitter.main.MainFrame;
import twitter.service.userService;
import twitter.ui.module.custombutton.RoundedRectangleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class addPostTopPanel extends JPanel {
    private JButton backButton;
    private JButton postButton;

    // 상단바의 배경 색상 및 기본 설정
    private static final Color BACKGROUND_COLOR = new Color(7, 7, 7);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(0, 122, 255);
    private final String imageIconPath = "/TwitterIcons/X.png";

    public addPostTopPanel(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(getWidth(), 40)); // 상단바 높이 설정

        // 상단바 패널 설정
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imageIconPath));
        Image scaledImage = originalIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        // 뒤로 가기 버튼 생성
        backButton = new JButton(resizedIcon);
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setMargin(new Insets(7, 4, 0, 0)); // 상단 마진 조정
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.showTwitterMainUiPanel(); // 뒤로가기 버튼을 누르면 메인화면으로 갑니다.
            }
        });

        // 게시하기 버튼 생성
        postButton = new RoundedRectangleButton("POST");
        postButton.setPreferredSize(new Dimension(70, 20));
        postButton.setFont(new Font("SansSerif", Font.PLAIN, 11));

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainframe.showTwitterMainUiPanel(); // 게시하기 버튼을 누르면 메인화면으로 갑니다.
            }
        });



        // 왼쪽 패널 (뒤로 가기 버튼)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(backButton);

        // 오른쪽 패널 (게시하기 버튼)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5,0,5,0));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(postButton);

        // 상단바에 왼쪽과 오른쪽 패널 추가
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
    }
    public JButton getPostButton(){
        return postButton;
    }

}
