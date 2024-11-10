package twitter.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.Timestamp;
import java.io.File;

public class imgService {

    private static final String IMAGE_DIRECTORY = "C:/~~"; // 로컬 이미지 저장 경로 (임의로 작성해둠)

    // 이미지 파일을 로컬에 저장하고 경로를 DB에 저장
    public void saveImagesWithPostId(Connection connection, int postId, List<byte[]> images) throws SQLException, IOException {
        for (byte[] imageBytes : images) {
            // 이미지 경로 생성
            String imagePath = IMAGE_DIRECTORY + "post_" + postId + "_" + System.currentTimeMillis() + ".jpg";

            // 로컬에 이미지 파일 저장
            File imageFile = new File(imagePath);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes);
            }

            // DB에 이미지 경로와 post_id 저장
            String query = "INSERT INTO images (post_id, image_url) VALUES (?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, postId);
                pstmt.setString(2, imagePath);
                pstmt.executeUpdate();
            }
        }
    }
}
