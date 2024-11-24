package twitter.ui.addPost;

import twitter.main.MainFrame;
import twitter.model.Post;
import twitter.model.User;
import twitter.service.postService;
import twitter.service.userService;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class addPostUi extends JPanel {

    public addPostUi(MainFrame mainframe, Connection connection, userService userService, postService postService) {
        setLayout(new BorderLayout());

        // 상단바 추가
        addPostTopPanel topPanel = new addPostTopPanel(mainframe, connection, userService);
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
                String content = middlePanel.getPostContent().trim(); // 게시물 내용 가져오기
                File images = bottomPanel.getSelectedImageFile(); // 게시물 이미지 가져오기
                User currentUser = userService.getCurrentUser(); // 현재 사용자 정보 가져오기

                // 내용이 비어 있는지 확인
                if (content.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "게시글 내용을 입력하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try{
                    Post post;
                    if (images == null) {
                        // 이미지 없이 게시물 작성
                        post = postService.writePost(connection, currentUser, content);
                    } else {
                        // 이미지 포함하여 게시물 작성
                        List<byte[]> image = new ArrayList<>();
                        try {
                            image.add(Files.readAllBytes(images.toPath()));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "이미지 변환 중 오류가 발생했습니다: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        post = postService.createPostWithImages(connection, currentUser, content, image);
                    }
                    if (post != null) {
                        JOptionPane.showMessageDialog(null, "게시물이 성공적으로 등록되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
                        middlePanel.clearPostContent(); // 게시 후 내용 초기화
                        mainframe.showTwitterMainUiPanel(); // 게시 후 메인 화면으로 이동
                    } else {
                        JOptionPane.showMessageDialog(null, "게시 실패!", "에러", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "게시 중 오류가 발생했습니다: " + ex.getMessage(), "에러", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}