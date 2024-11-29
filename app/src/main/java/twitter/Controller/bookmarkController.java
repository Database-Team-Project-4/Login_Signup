package twitter.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class bookmarkController {

    private final Connection connection;

    public bookmarkController(Connection connection) {
        this.connection = connection;
    }

    // 현재 게시물의 북마크 수를 가져오는 메서드
    public int getBookmarkCount(int postId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Bookmarks WHERE post_id = ?";
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

    // 사용자가 특정 게시물을 북마크했는지 확인하는 메서드
    public boolean isBookmarkedByUser(int postId, int userId) throws SQLException {
        String query = "SELECT 1 FROM Bookmarks WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int getPostOwnerId(int postId) throws SQLException {
        String query = "SELECT user_id FROM Posts WHERE post_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
        throw new SQLException("해당 postId에 대한 작성자를 찾을 수 없습니다.");
    }


    // 북마크 추가 메서드
    public void addBookmark(int postId, int userId) throws SQLException {
        String query = "INSERT INTO Bookmarks (user_id, post_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.executeUpdate();
        }
    }

    // 북마크 삭제 메서드
    public void removeBookmark(int postId, int userId) throws SQLException {
        String query = "DELETE FROM Bookmarks WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }
}
