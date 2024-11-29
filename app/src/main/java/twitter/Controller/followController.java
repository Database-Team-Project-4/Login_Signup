package twitter.Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import twitter.model.User;

public class followController {

    // 팔로우 기능: 팔로우할 사용자의 ID를 인수로 받아 처리
    public String followUser(Connection conn, User currentUser, int followeeId) {
        int followerId = currentUser.getId();

        if (followeeId == followerId) {
            return "자신을 팔로우할 수 없습니다.";
        }

        // 이미 팔로우하고 있는지 확인
        if (isAlreadyFollowing(conn, followerId, followeeId)) {
            return "이미 팔로우하고 있습니다.";
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

    // 언팔로우 기능: 언팔로우할 사용자의 ID를 인수로 받아 처리
    public String unfollowUser(Connection conn, User currentUser, int followeeId) {
        int followerId = currentUser.getId();

        if (!isAlreadyFollowing(conn, followerId, followeeId)) {
            return "팔로우 상태가 아닙니다.";
        }

        // 팔로우 관계 삭제
        String deleteQuery = "DELETE FROM Follow WHERE follower_id = ? AND followee_id = ?";
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, followerId);
            deleteStmt.setInt(2, followeeId);
            deleteStmt.executeUpdate();
            return "성공적으로 언팔로우했습니다.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "언팔로우 중 오류 발생: " + e.getMessage();
        }
    }

    // 이미 팔로우 상태인지 확인하는 메서드
    public boolean isAlreadyFollowing(Connection conn, int followerId, int followeeId) {
        String checkQuery = "SELECT 1 FROM Follow WHERE follower_id = ? AND followee_id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, followerId);
            checkStmt.setInt(2, followeeId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next(); // 팔로우 관계가 존재하면 true 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("팔로우 상태 확인 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 팔로워 목록 가져오기
    public List<User> getFollowers(Connection conn, User currentUser) {
        return getFollowList(conn, "follower_id", "followee_id", currentUser.getId());
    }

    // 팔로잉 목록 가져오기
    public List<User> getFollowing(Connection conn, User currentUser) {
        return getFollowList(conn, "followee_id", "follower_id", currentUser.getId());
    }

    // 팔로워/팔로잉 공통 로직 추출
    private List<User> getFollowList(Connection conn, String userIdColumn, String targetIdColumn, int currentUserId) {
        List<User> users = new ArrayList<>();
        String query = String.format("""
            SELECT u.user_id, u.email, u.name
            FROM Users u
            JOIN Follow f ON u.user_id = f.%s
            WHERE f.%s = ?
        """, userIdColumn, targetIdColumn);

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("user_id");
                String email = rs.getString("email");
                String name = rs.getString("name");
                users.add(new User(id, email, name)); // User 객체 생성 후 리스트에 추가
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("팔로워/팔로잉 데이터를 가져오는 중 오류 발생: " + e.getMessage());
        }

        return users;
    }
}
