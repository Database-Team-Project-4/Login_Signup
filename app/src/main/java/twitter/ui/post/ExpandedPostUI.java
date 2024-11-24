package twitter.ui.post;

import twitter.main.MainFrame;
import twitter.service.userService;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ExpandedPostUI extends JPanel {
    private MainFrame mainFrame;
    private int userId; // userId 변수 추가

    // 세 개의 인수를 받는 생성자
    public ExpandedPostUI(int postId, Connection connection, MainFrame mainFrame, userService userService) {
        this.mainFrame = mainFrame;
        initializeUI(postId, connection, mainFrame, userService);
    }

    // 두 개의 인수를 받는 생성자 (테스트용)
    public ExpandedPostUI(int postId, Connection connection) {
        this.mainFrame = null;
        initializeUI(postId, connection, null, null);
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
        postUI.setPreferredSize(new Dimension(600, 800)); // 크기 조정

        add(postUI, BorderLayout.CENTER);
    }

    private void removeExistingFooterPanel() {
        for (Component component : getComponents()) {
            if (component instanceof PostFooterPanel) {
                remove(component); // 기존 하단 바 제거
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://58.121.110.129:4472/twitter", "root", "ckwnsgk@1");

                int testPostId = 1;

                ExpandedPostUI expandedPostUI = new ExpandedPostUI(testPostId, connection);

                JFrame frame = new JFrame("Expanded Post - Design Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 900);
                frame.add(expandedPostUI);
                frame.pack();
                frame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
