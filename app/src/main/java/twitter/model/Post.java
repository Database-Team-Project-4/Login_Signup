package twitter.model;

import java.sql.Timestamp;

public class Post {
    private int postId; // post_id
    private int userId; // user_id
    private String content; // content
    private Timestamp createdAt; // created_at
    private Timestamp updatedAt; // updated_at

    // 기본 생성자
    public Post() {}

    // 생성자 (createdAt과 updatedAt은 데이터베이스가 관리하므로 제외)
    public Post(int postId, int userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    // postId getter & setter
    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    // content getter & setter
    public String getContent() {
        return content;
    }

    // createdAt getter만 제공
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // updatedAt getter만 제공
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    // toString 메서드 (디버깅 및 로그 출력 용도)
    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
