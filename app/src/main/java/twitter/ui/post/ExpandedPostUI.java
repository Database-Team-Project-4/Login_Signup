package twitter.ui.post;

import twitter.main.MainFrame;
import twitter.service.commentService;
import twitter.service.userService;
import twitter.ui.Comment.CommentUI;
import twitter.ui.module.custombutton.MoreButton;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class ExpandedPostUI extends JPanel {
    private MainFrame mainFrame;
    private int userId; // userId 변수 추가

    // 세 개의 인수를 받는 생성자
    public ExpandedPostUI(int postId, Connection connection, MainFrame mainFrame, userService userService) {
        this.mainFrame = mainFrame;
        initializeUI(postId, connection, mainFrame, userService);
    }

    // UI 초기화 메서드
    private void initializeUI(int postId, Connection connection, MainFrame mainFrame, userService userService) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 데이터베이스에서 게시물 데이터 가져오기
        String userName = "Unknown";
        String userEmail = "unknown@example.com";
        String contentText = "내용을 불러올 수 없습니다.";
        String createdAt = "N/A";
        int likes = 0;
        int comments = 0;
        int bookmarks = 0;
        this.userId = -1; // 기본값 설정

        // Query the database using postId
        String query = "SELECT u.user_id, u.name, u.email, p.content, p.created_at " +
                "FROM Posts p JOIN Users u ON p.user_id = u.user_id WHERE p.post_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                this.userId = rs.getInt("user_id"); // userId 저장
                userName = rs.getString("name");
                userEmail = rs.getString("email");
                contentText = rs.getString("content");
                createdAt = rs.getString("created_at");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 상단 뒤로가기 버튼 패널 추가
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BorderLayout());
        backPanel.setBackground(new Color(7, 7, 7)); // 배경색 검은색으로 설정
        backPanel.setPreferredSize(new Dimension(600, 50));
        backPanel.setOpaque(true); // 투명도 해제하여 배경색 적용

        // 뒤로가기 버튼
        JButton backButton = new JButton("<");
        backButton.setPreferredSize(new Dimension(50, 40)); // 버튼 크기 조정
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(7, 7, 7));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 19)); // 폰트 크기와 스타일 설정

        // 뒤로가기 버튼 액션 추가
        backButton.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.showTwitterMainUiPanel();
            } else {
                System.out.println("뒤로가기 버튼 클릭됨");
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame != null) {
                    topFrame.dispose(); // 창 닫기
                }
            }
        });

        // "게시" 텍스트
        JLabel titleLabel = new JLabel("게시", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15)); // 텍스트 크기와 스타일 설정

        // 왼쪽 빈 공간 (뒤로가기 버튼 오른쪽 여백)
        JPanel leftSpacePanel = new JPanel();
        leftSpacePanel.setPreferredSize(new Dimension(50, 40)); // 뒤로가기 버튼 크기만큼 여백
        leftSpacePanel.setBackground(new Color(7, 7, 7));

        // 상단바 구성
        backPanel.add(backButton, BorderLayout.WEST);
        backPanel.add(titleLabel, BorderLayout.CENTER);
        backPanel.add(leftSpacePanel, BorderLayout.EAST);

        add(backPanel, BorderLayout.NORTH);

        // 기존 하단 바 제거
        removeExistingFooterPanel();


        // 기존 PostUI를 중앙에 배치
        PostUI postUI;
        if (mainFrame != null && userId != -1) {
            postUI = new PostUI(mainFrame, postId, userId, userName, userEmail, contentText, likes, comments, bookmarks, createdAt, userService, connection);
        } else {
            postUI = new PostUI(postId, userName, userEmail, contentText, likes, comments, bookmarks, createdAt, userService, connection);
        }

        Component[] components = postUI.getComponents();
        for (Component component : components) {
            if (component instanceof PostFooterPanel) {
                PostFooterPanel footerPanel = (PostFooterPanel) component;
                footerPanel.getCommentButton().addActionListener(e -> {
                    System.out.println("댓글 버튼 클릭됨");
                    if (mainFrame != null) {
                        mainFrame.showExpandedCommentUI(postId); // 댓글 화면 전환
                    }
                });
                break;
            }
        }

        postUI.setFont(new Font("SansSerif", Font.PLAIN, 40)); // 기본 폰트 크기 증가
        postUI.setPreferredSize(new Dimension(600, 350)); // 크기 조정

        add(postUI, BorderLayout.CENTER);

        // Separator 패널 생성 (수정된 코드 적용)
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setPreferredSize(new Dimension(600, 40));
        separatorPanel.setBackground(Color.BLACK);

        JPanel linePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int y = getHeight() / 2;
                g.setColor(Color.GRAY);
                g.drawLine(0, y, getWidth(), y);
            }
        };
        linePanel.setOpaque(false);
        linePanel.setPreferredSize(new Dimension(600, 1));

        JLabel commentLabel = new JLabel("댓글", SwingConstants.CENTER);
        commentLabel.setForeground(Color.WHITE);
        commentLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        separatorPanel.add(linePanel, BorderLayout.CENTER);
        separatorPanel.add(commentLabel, BorderLayout.SOUTH);

        // 하드코딩된 댓글 데이터 생성

        String commentUserName = "Unknown";
        String commentUserEmail = "";
        String commentContent = "댓글 내용을 불러올 수 없습니다.";
        int commentLikes = 42;


        // 데이터베이스에서 댓글 데이터 가져오기
        commentService commentService = new commentService();

        try {
            List<CommentUI> comment = commentService.getCommentsByPostId(postId, connection);
            if (!comment.isEmpty()) {
                commentUserName = comment.get(0).getUserName();
                commentUserEmail = comment.get(0).getUserEmail();
                commentContent = comment.get(0).getContentText();
            } else {
                commentUserName = "Unknown";
                commentUserEmail = "";
                commentContent = "달린 댓글이 없습니다";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // CommentUI 인스턴스 생성
        CommentUI commentUI = new CommentUI(commentUserName, commentUserEmail, commentContent, commentLikes);
        commentUI.setPreferredSize(new Dimension(600, 150));

        // '댓글 더보기' 버튼 생성 및 추가
        MoreButton moreCommentsButton = new MoreButton("댓글 더보기");
        moreCommentsButton.setPreferredSize(new Dimension(170, 40));
        moreCommentsButton.setBackground(new Color(8, 8, 8)); // 하늘색
        moreCommentsButton.setForeground(Color.LIGHT_GRAY);
        moreCommentsButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        moreCommentsButton.setFocusPainted(false);
        moreCommentsButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // 테두리 설정

        moreCommentsButton.addActionListener(e -> {
            System.out.println("댓글 더보기 버튼 클릭됨");
            if (mainFrame != null) {
                mainFrame.showExpandedCommentUI(postId); // 댓글 화면 전환
            } else {
                System.out.println("MainFrame이 null입니다. 댓글 화면을 표시할 수 없습니다.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // 아래쪽에 10픽셀 패딩 추가
        buttonPanel.add(moreCommentsButton);

        // 구성 요소를 담을 메인 패널 생성
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);

        // 구성 요소 추가
        contentPanel.add(postUI);
        contentPanel.add(separatorPanel);
        contentPanel.add(commentUI);
        contentPanel.add(buttonPanel);

        // 메인 패널에 추가
        add(contentPanel, BorderLayout.CENTER);
    }

    private void removeExistingFooterPanel() {
        for (Component component : getComponents()) {
            if (component instanceof PostFooterPanel) {
                remove(component); // 기존 하단 바 제거
                break;
            }
        }
    }
}
