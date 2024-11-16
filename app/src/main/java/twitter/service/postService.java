package twitter.service;

import twitter.model.Post;
import twitter.model.User;
import twitter.ui.post.PostUI;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class postService {

    private final imgService imgService = new imgService();

    public Post post = new Post();


    // 게시물 작성 메서드, 작성 후 Post 객체에 값 저장
    public Post writePost(Connection con, User currentUser, String content) throws SQLException {
        String query = "INSERT INTO Posts (user_id, content) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, currentUser.getId());
            pstmt.setString(2, content);
            pstmt.executeUpdate();

            // 생성된 게시물 ID 가져오기
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int postId = generatedKeys.getInt(1);
                    System.out.println("게시물이 성공적으로 생성되었습니다. Post ID: " + postId);
                    return new Post(postId, currentUser.getId(), content);
                }
            }
        }
        throw new SQLException("게시물 생성 실패, ID를 가져올 수 없습니다.");
    }

    // 게시물과 이미지 생성 메서드
    public Post createPostWithImages(Connection connection, User currentUser, String content, List<byte[]> images) throws SQLException, IOException {
        // 1. 게시물 작성 및 Post 객체 생성
        Post post = writePost(connection, currentUser, content);

        // 2. 생성된 postId로 이미지를 저장
        imgService.saveImagesWithPostId(connection, post.getPostId(), images);
        return post;
    }

    // 게시물 삭제 메서드
    public boolean deletePost(Connection con, User currentUser, int postId) throws SQLException {
        String query = "DELETE FROM post WHERE post_id = ? AND user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, currentUser.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 삭제 성공 시 true 반환
        }
    }

    // 게시물 좋아요 메서드
    public void likePost(Connection con, User currentUser, int postId) throws SQLException {
        // 이미 좋아요를 눌렀는지 확인
        String checkQuery = "SELECT * FROM like_post WHERE user_id = ? AND post_id = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, currentUser.getId());
            checkStmt.setInt(2, postId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("이미 이 게시물에 좋아요를 눌렀습니다.");
                return;
            }
        }

        // 좋아요가 없으면 새로 추가
        String insertQuery = "INSERT INTO like_post (user_id, post_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, currentUser.getId());
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
            System.out.println("게시물에 좋아요를 눌렀습니다.");
        }

        // 업데이트된 좋아요 개수 가져오기
        String likeCountQuery = "SELECT COUNT(*) AS like_count FROM like_post WHERE post_id = ?";
        try (PreparedStatement countStmt = con.prepareStatement(likeCountQuery)) {
            countStmt.setInt(1, postId);
            ResultSet rs = countStmt.executeQuery();
            if (rs.next()) {
                int likeCount = rs.getInt("like_count");
                System.out.println("이제 게시물 ID " + postId + "의 좋아요 수는 " + likeCount + "개입니다.");
            }
        }
    }



    // 모든 게시물 조회 메서드
    public List<PostUI> getAllPosts(Connection con) {
        List<PostUI> postUIs = new ArrayList<>();
        String query = "SELECT Posts.post_id, Posts.user_id, Posts.content, Posts.created_at, Posts.updated_at, " +
                "Users.name, Users.email " +
                "FROM Posts " +
                "JOIN Users ON Posts.user_id = Users.user_id";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // ResultSet에서 데이터 읽기
                int postId = rs.getInt("post_id");
                String userName = rs.getString("name");
                String email = rs.getString("email");
                String content = rs.getString("content");
                String created_at =rs.getString("created_at");

                int likes = getLikeCount(con, postId); // 게시물 좋아요 수 가져오기
                int comments = getCommentCount(con, postId); // 게시물 댓글 수 가져오기
                int bookmarks = getBookmarkCount(con, postId); // 게시물 북마크 수 가져오기

                // PostUI 객체 생성 및 리스트에 추가
                PostUI postUI = new PostUI(userName, email, content, likes, comments, bookmarks, created_at);
                postUIs.add(postUI);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching posts: " + e.getMessage());
        }
        return postUIs;
    }



    private int getLikeCount(Connection con, int postId) {
        String query = "SELECT COUNT(*) AS like_count FROM Post_Likes WHERE post_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("like_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching like count: " + e.getMessage());
        }
        return 0; // 기본값 반환
    }


    private int getCommentCount(Connection con, int postId) {
        String query = "SELECT COUNT(*) AS comment_count FROM Comments WHERE post_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("comment_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching comment count: " + e.getMessage());
        }
        return 0; // 기본값 반환
    }


    private int getBookmarkCount(Connection con, int postId) {
        String query = "SELECT COUNT(*) AS bookmark_count FROM Bookmarks WHERE post_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("bookmark_count");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bookmark count: " + e.getMessage());
        }
        return 0; // 기본값 반환
    }
}
