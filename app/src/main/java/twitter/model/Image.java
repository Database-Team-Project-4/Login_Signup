package twitter.model;

import java.sql.Timestamp;

public class Image {
    private int imageId; // image_id
    private int postId; // post_id
    private byte[] imageData; // image_data
    private Timestamp createdAt; // created_at

    // 기본 생성자
    public Image() {}

    // 생성자 (필요한 필드만 포함)
    public Image(int imageId, int postId, byte[] imageData, Timestamp createdAt) {
        this.imageId = imageId;
        this.postId = postId;
        this.imageData = imageData;
        this.createdAt = createdAt;
    }

    // Getter 및 Setter 메서드
    public int getImageId() {
        return imageId;
    }

    public int getPostId() {
        return postId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", postId=" + postId +
                ", imageData size=" + (imageData != null ? imageData.length : 0) +
                ", createdAt=" + createdAt +
                '}';
    }
}
