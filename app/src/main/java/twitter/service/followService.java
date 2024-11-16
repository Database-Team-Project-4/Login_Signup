package twitter.service;

import java.sql.*;
import twitter.model.User;

public class followService {

    // 팔로우 기능: 팔로우할 사용자의 ID를 인수로 받아 처리
    public static String followUser(Connection conn, User currentUser, int followeeId) {
        int followerId = currentUser.getId();

        if (followeeId == followerId) {
            return "자신을 팔로우할 수 없습니다.";
        }

        // 이미 팔로우하고 있는지 확인
        String checkQuery = "SELECT follower_id FROM Follow WHERE follower_id = ? AND followee_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, followerId);
            checkStmt.setInt(2, followeeId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    return "이미 팔로우하고 있습니다.";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "팔로우 상태 확인 중 오류 발생: " + e.getMessage();
        }

        // 팔로우 추가
        String insertQuery = "INSERT INTO Follow (follower_id, followee_id) VALUES (?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, followerId);
            insertStmt.setInt(2, followeeId);
            insertStmt.executeUpdate();
            return "성공적으로 팔로우했습니다.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "팔로우 중 오류 발생: " + e.getMessage();
        }
    }

    // 팔로워 목록 보기
    public static ResultSet getFollowers(Connection conn, User currentUser) throws SQLException {
        String query = "SELECT follower_id FROM Follow WHERE followee_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, currentUser.getId());
        return stmt.executeQuery(); // ResultSet을 반환하여 UI에서 데이터를 표시할 수 있게 함
    }

    // 팔로잉 목록 보기
    public static ResultSet getFollowing(Connection conn, User currentUser) throws SQLException {
        String query = "SELECT followee_id FROM Follow WHERE follower_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, currentUser.getId());
        return stmt.executeQuery(); // ResultSet을 반환하여 UI에서 데이터를 표시할 수 있게 함
    }
}
