package twitter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class likeService {

    private final Connection connection;

    public likeService(Connection connection) {
        this.connection = connection;
    }

    // 좋아요 추가 메서드
    public void addLike(int postId, int userId) throws SQLException {
        String query = "INSERT INTO Post_Likes (user_id, post_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        }
    }

    // 좋아요 삭제 메서드
    public void removeLike(int postId, int userId) throws SQLException {
        String query = "DELETE FROM Post_Likes WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    // 게시물의 좋아요 개수를 가져오는 메서드
    public int getLikeCount(int postId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Post_Likes WHERE post_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public boolean isLikedByUser(int postId, int userId) throws SQLException {
        String query = "SELECT 1 FROM Post_Likes WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}