package twitter.service;

import twitter.model.User;

import java.sql.*;

public class commentService {

    // 댓글 작성 메서드
    public static void writeComment(Connection con, User currentUser, int postId, String content, Integer parentCommentId) {
        String path;
        if (parentCommentId != null) {
            // 상위 댓글의 path를 조회하여 새로운 댓글의 path를 구성
            String getPathQuery = "SELECT path FROM comments WHERE comment_id = ?";
            try (PreparedStatement getPathStmt = con.prepareStatement(getPathQuery)) {
                getPathStmt.setInt(1, parentCommentId);
                ResultSet rs = getPathStmt.executeQuery();
                if (rs.next()) {
                    String parentPath = rs.getString("path");
                    path = parentPath + "/" + parentCommentId; // 상위 댓글의 path에 현재 댓글 ID 추가
                } else {
                    System.out.println("Parent comment not found.");
                    return;
                }
            } catch (SQLException e) {
                System.out.println("Error while retrieving parent comment path: " + e.getMessage());
                return;
            }
        } else {
            // 최상위 댓글인 경우 path를 자신의 comment_id로 설정
            path = String.valueOf(postId); // postId로 path 시작
        }

        // 댓글 삽입
        String query = "INSERT INTO comments (post_id, user_id, content, parent_comment_id, path) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, currentUser.getId());
            pstmt.setString(3, content);
            if (parentCommentId != null) {
                pstmt.setInt(4, parentCommentId);
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            pstmt.setString(5, path); // 생성된 path 삽입
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int commentId = generatedKeys.getInt(1);
                    System.out.println("Comment has been created successfully with Comment ID: " + commentId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while creating the comment: " + e.getMessage());
        }
    }

    // 부모 댓글 삭제 시 하위 댓글들까지 모두 삭제하는 메서드
    public static void deleteComment(Connection con, User currentUser, int commentId) {
        // 삭제할 댓글의 path 조회
        String getPathQuery = "SELECT path FROM comments WHERE comment_id = ? AND user_id = ?";
        String path = null;
        try (PreparedStatement getPathStmt = con.prepareStatement(getPathQuery)) {
            getPathStmt.setInt(1, commentId);
            getPathStmt.setInt(2, currentUser.getId());
            ResultSet rs = getPathStmt.executeQuery();
            if (rs.next()) {
                path = rs.getString("path");
            } else {
                System.out.println("Comment not found or you do not have permission to delete this comment.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving comment path: " + e.getMessage());
            return;
        }

        // 하위 댓글까지 모두 삭제
        String deleteQuery = "DELETE FROM comments WHERE path LIKE ?";
        try (PreparedStatement pstmt = con.prepareStatement(deleteQuery)) {
            pstmt.setString(1, path + "/%"); // path에 포함되는 모든 하위 댓글 삭제
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("The comment and all its sub-comments have been deleted successfully.");
            } else {
                System.out.println("Failed to delete the comment.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting the comment and sub-comments: " + e.getMessage());
        }
    }

    // 계층 구조를 고려한 전체 댓글 출력 메서드
    public static void printAllComments(Connection con, int postId) {
        String query = "SELECT comments.comment_id, comments.user_id, comments.content, user.name, comments.path, comments.created_at " +
                "FROM comments " +
                "JOIN user ON comments.user_id = user.user_id " +
                "WHERE comments.post_id = ? " +
                "ORDER BY comments.path"; // path를 기준으로 계층 정렬

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("All comments for post ID " + postId + ":");
            while (rs.next()) {
                int commentId = rs.getInt("comment_id");
                String content = rs.getString("content");
                String name = rs.getString("name");
                String path = rs.getString("path");
                Timestamp createdAt = rs.getTimestamp("created_at");

                // path에 따라 들여쓰기 설정 (계층 수준을 "/" 구분자 개수로 파악)
                int level = path.split("/").length - 1; // 최상위 댓글은 레벨 0
                String indent = "  ".repeat(level); // 계층 레벨에 따른 들여쓰기

                System.out.println(indent + "Comment ID: " + commentId + ", User Name: " + name + ", Content: " + content + ", Created At: " + createdAt);
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving all comments: " + e.getMessage());
        }
    }
}
