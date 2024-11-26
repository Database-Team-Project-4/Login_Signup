package twitter.ui.Comment;

import org.checkerframework.checker.units.qual.C;
import twitter.main.MainFrame;
import twitter.service.commentService;
import twitter.service.userService;
import twitter.ui.module.CustomScrollbar;
import twitter.ui.module.CustomSearchField;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpandedCommentUI extends JPanel {
    private int postId; // Post ID 저장
    private MainFrame mainFrame;

    private userService userService;


    public ExpandedCommentUI(int postId, MainFrame mainFrame, userService userService, Connection connection) {
        this.postId = postId; // postId 설정
        this.mainFrame = mainFrame;

        this.userService = userService;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 상단 패널: 뒤로가기 버튼과 "댓글" 제목
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(7, 7, 7));
        topPanel.setPreferredSize(new Dimension(600, 50));

        JButton backButton = new JButton("<");
        backButton.setPreferredSize(new Dimension(50, 40));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(7, 7, 7));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 19));

        // 뒤로가기 버튼 액션 추가
        backButton.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.showExpandedPostPanel(postId); // ExpandedPostUI로 돌아가기
            } else {
                System.out.println("MainFrame이 null입니다. 뒤로가기 동작을 수행할 수 없습니다.");
            }
        });

        JLabel titleLabel = new JLabel("댓글", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JPanel rightSpacer = new JPanel(); // 오른쪽 빈 공간
        rightSpacer.setPreferredSize(new Dimension(50, 40));
        rightSpacer.setBackground(new Color(7, 7, 7));

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(rightSpacer, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 중앙 패널: 댓글 목록
        JPanel commentListPanel = new JPanel();
        commentListPanel.setLayout(new BoxLayout(commentListPanel, BoxLayout.Y_AXIS));
        commentListPanel.setBackground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(commentListPanel);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollbar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        List<CommentUI> comments = getHardcodedComments(postId, connection);
        for (int i = 0; i < comments.size(); i++) {
            commentListPanel.add(comments.get(i));

            // 마지막 댓글이 아니면 구분선 추가
            if (i < comments.size() - 1) {
                commentListPanel.add(createSeparator()); // 구분선 추가
            }
        }

        add(scrollPane, BorderLayout.CENTER);

        // 스크롤바 초기 위치를 맨 위로 설정
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));

        // 하단 패널: 댓글 입력창
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(new Color(7, 7, 7));
        bottomPanel.setPreferredSize(new Dimension(600, 70)); // 높이 조정

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10); // 패딩 추가

        // 댓글 입력창
        CustomSearchField commentInputField = new CustomSearchField();
        commentInputField.setSearchText("댓글을 입력하세요...");
        commentInputField.setPreferredSize(new Dimension(500, 35)); // 입력창 길이 조정
        gbc.gridx = 0;
        gbc.weightx = 0.85; // 입력창의 너비 비율
        bottomPanel.add(commentInputField, gbc);

        // 전송 버튼
        JButton sendButton = new JButton("전송");
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        sendButton.addActionListener(e -> {
            if (!userService.isLoggedIn()) {
                JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "로그인 필요", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String commentText = commentInputField.getSearchText();

            if (!commentText.isEmpty()) {
                try {
                    if (connection == null || connection.isClosed()) {
                        // 연결 재시도 로직 추가 또는 예외 처리
                    }
                    // 댓글 추가
                    commentService commentService = new commentService();
                    commentService.addComment(postId, commentText, null, connection, userService.getCurrentUser().getId());

                    // 입력창 초기화
                    commentInputField.setSearchText("댓글을 입력하세요...");

                    // UI 새로고침
                    SwingUtilities.invokeLater(() -> {
                        mainFrame.showExpandedCommentUI(postId);
                    });

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "댓글 추가 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 0.15; // 전송 버튼의 너비 비율
        bottomPanel.add(sendButton, gbc);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.DARK_GRAY);
                g.fillRect(0, 0, getWidth(), 1);
            }
        };
        separator.setPreferredSize(new Dimension(600, 1));
        separator.setBackground(Color.BLACK);
        return separator;
    }


    private List<CommentUI> getHardcodedComments(int postId, Connection connection) {
        List<CommentUI> comments = new ArrayList<>();
        try {
            commentService commentService = new commentService();
            comments = commentService.getCommentsByPostId(postId, connection); // 댓글 데이터 가져오기
        } catch (SQLException e) {
            e.printStackTrace(); // 예외 로그 출력
        }
        return comments;
    }
}
