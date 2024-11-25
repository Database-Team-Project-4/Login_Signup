package twitter.model;


import java.time.LocalDateTime;

public class comment_like {
    private final int likeId; // Primary Key
    private final int commentId;
    private final int userId;
    private final LocalDateTime likedAt;

    public comment_like(int likeId, int commentId, int userId, LocalDateTime likedAt) {
        this.likeId = likeId;
        this.commentId = commentId;
        this.userId = userId;
        this.likedAt = likedAt;
    }

    // Getters
    public int getLikeId() {
        return likeId;
    }

    public int getCommentId() {
        return commentId;
    }

    public int getUserId() {
        return userId;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

}
