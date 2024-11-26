package twitter.model;

import java.time.LocalDateTime;

public class Comment {

    private final int commentId; // Primary Key
    private final int postId;
    private final int userId;
    private final String content;
    private final Integer parentCommentId; // Nullable
    private final String path; // Used for nested comment hierarchy
    private final LocalDateTime createdAt;

    public Comment(int commentId, int postId, int userId, String content, Integer parentCommentId, String path, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.path = path;
        this.createdAt = createdAt;
    }

    // Getters
    public int getCommentId() {
        return commentId;
    }

    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public String getPath() {
        return path;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
