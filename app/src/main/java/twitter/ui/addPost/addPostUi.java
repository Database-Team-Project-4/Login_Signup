package twitter.ui.addPost;

import twitter.main.MainFrame;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.awt.event.ActionEvent;

public class addPostUi extends JPanel {

    public addPostUi(MainFrame mainframe, Connection connection, userService userService) {
        setLayout(new BorderLayout());

        // 상단바 추가
        addPostTopPanel topPanel = new addPostTopPanel(mainframe);
        add(topPanel, BorderLayout.NORTH);

        // 중간 패널 추가 (게시글 작성용)
        addPostMiddlePanel middlePanel = new addPostMiddlePanel(mainframe, topPanel.getPostButton());
        add(middlePanel, BorderLayout.CENTER);

        // 하단 패널 추가
        addPostBottomPanel bottomPanel = new addPostBottomPanel(mainframe, middlePanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // "POST" 버튼 클릭 시 게시글 내용을 가져와서 처리
        topPanel.getPostButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String content = middlePanel.getPostContent().trim();
                // 내용이 비어 있는지 확인
                if (content.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "게시글 내용을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // DB에 게시글 저장 (추후 구현)
                if (userService != null) {
                    boolean success = true;
                    //boolean success = userService.addPost(content, connection); //sql구문에 post내용을 넣고 성공여부를 반환하는 메소드 구현해야함
                    if (success) {
                        JOptionPane.showMessageDialog(null, "게시되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                        middlePanel.clearPostContent(); // 게시 후 내용 초기화
                        mainframe.showTwitterMainUiPanel(); //post가 끝나면 다시 메인화면으로 갑니다.
                    } else {
                        JOptionPane.showMessageDialog(null, "게시 실패!", "에러", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    System.out.println("테스트 모드: 게시 내용 - " + content);
                    middlePanel.clearPostContent(); // 게시 후 내용 초기화
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("addPost");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setContentPane(new addPostUi(null, null, null));
        frame.setVisible(true);
    }
}