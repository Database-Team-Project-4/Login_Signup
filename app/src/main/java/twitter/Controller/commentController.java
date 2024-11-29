package twitter.Controller;

import twitter.ui.Comment.CommentUI;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class commentController {

    // 댓글 작성 메서드
    public void addComment(int postId, String content, Integer parentCommentId, Connection connection, int userId) throws SQLException {
        String path = (parentCommentId == null) ? null : getPathByParentCommentId(parentCommentId, connection);
        String sql = "INSERT INTO Comments (post_id, user_id, content, parent_comment_id, path) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, content);
            if (parentCommentId == null) {
                stmt.setNull(4, Types.INTEGER);
            } else {
                stmt.setInt(4, parentCommentId);
            }
            stmt.setString(5, path);
            stmt.executeUpdate();
        }
    }

    // 댓글 삭제 메서드
    public void deleteComment(int commentId, Connection connection) throws SQLException {
        String sql = "DELETE FROM Comments WHERE comment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            stmt.executeUpdate();
        }
    }

    // 댓글 조회 메서드 (Users와 조인)
    public List<CommentUI> getCommentsByPostId(int postId, Connection connection) throws SQLException {
        String sql = "SELECT c.content, c.created_at, u.name AS user_name, u.email AS user_email, " +
                "(SELECT COUNT(*) FROM Comment_Likes cl WHERE cl.comment_id = c.comment_id) AS likes " +
                "FROM Comments c " +
                "JOIN Users u ON c.user_id = u.user_id " +
                "WHERE c.post_id = ? " +
                "ORDER BY c.created_at ASC";

        List<CommentUI> commentUIList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String userName = rs.getString("user_name");
                    String userEmail = rs.getString("user_email");
                    String content = rs.getString("content");
                    int likes = rs.getInt("likes");

                    CommentUI commentUI = new CommentUI(userName, userEmail, content);
                    commentUIList.add(commentUI);
                }
            }
        }
        return commentUIList;
    }

    // 부모 댓글 ID로 path 조회
    private String getPathByParentCommentId(int parentCommentId, Connection connection) throws SQLException {
        String sql = "SELECT path FROM Comments WHERE comment_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, parentCommentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("path");
                }
            }
        }
        return null;
    }

    public int getCommentCount(int postId, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM Comments WHERE post_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

}
