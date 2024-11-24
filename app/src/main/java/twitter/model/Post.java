package twitter.model;

import java.sql.Timestamp;
import java.util.List;

public class Post {
    private int postId; // post_id
    private int userId; // user_id
    private String content; // content
    private Timestamp createdAt; // created_at
    private Timestamp updatedAt; // updated_at

    private List<Image> images; // 다중 이미지를 위한 리스트


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

    //사진 목록을 출력받는 List
    public List<Image> getImages() {
        return images;
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
