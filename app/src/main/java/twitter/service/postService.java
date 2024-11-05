package twitter.service;

import java.sql.*;

import twitter.User;

public class postService {

    // Method to write a post
    public static void writePost(Connection con, User currentUser, String content) {
        String query = "INSERT INTO post (user_id, content) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int postId = generatedKeys.getInt(1);
                    System.out.println("The post has been created successfully with Post ID: " + postId);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while creating the post: " + e.getMessage());
        }
    }

    // Method to delete a post
    public static void deletePost(Connection con, User currentUser, int postId) {
        String query = "DELETE FROM post WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, currentUser.getId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("The post has been deleted successfully.");
            } else {
                System.out.println("Failed to delete the post. Either the post ID is invalid or you do not have permission to delete this post.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting the post: " + e.getMessage());
        }
    }

    // Method to search posts
    public static void searchPosts(Connection con, String keyword) {
        // Join query to include the user's name with the post information
        String query = "SELECT post.post_id, post.user_id, post.content, user.name " +
                "FROM post " +
                "JOIN user ON post.user_id = user.user_id " +
                "WHERE post.content LIKE ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("Search results:");
            while (rs.next()) {
                int postId = rs.getInt("post_id");
                String content = rs.getString("content");
                String name = rs.getString("name");
                System.out.println("Post ID: " + postId + ", User Name: " + name + ", Content: " + content);
            }
        } catch (SQLException e) {
            System.out.println("Error while searching posts: " + e.getMessage());
        }
    }

    // Method to like a post
    public static void likePost(Connection con, User currentUser, int postId) {
        // Check if the user already liked the post
        String checkQuery = "SELECT * FROM like_post WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentUser.getId());
            checkStmt.setInt(2, postId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // User has already liked the post
                System.out.println("You have already liked this post.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error while checking likes: " + e.getMessage());
            return;
        }

        // Insert like if the user has not liked the post yet
        String insertQuery = "INSERT INTO like_post (user_id, post_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, currentUser.getId());
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
            System.out.println("You have liked the post.");
        } catch (SQLException e) {
            System.out.println("Error while liking the post: " + e.getMessage());
            return;
        }

        // Retrieve the updated like count for the post
        String likeCountQuery = "SELECT COUNT(*) AS like_count FROM like_post WHERE post_id = ?";
        try (PreparedStatement countStmt = con.prepareStatement(likeCountQuery)) {
            countStmt.setInt(1, postId);
            ResultSet rs = countStmt.executeQuery();

            if (rs.next()) {
                int likeCount = rs.getInt("like_count");
                System.out.println("Now the post with ID " + postId + " has " + likeCount + " likes.");
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving like count: " + e.getMessage());
        }
    }
}
