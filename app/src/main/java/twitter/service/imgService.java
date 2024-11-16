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

    private static final String SERVER_IMAGE_DIRECTORY = "http://58.121.110.129:8000/var/lib/img/"; // 임시작성

    // 이미지를 서버 경로에 저장하고 해당 경로를 DB에 저장
    public void saveImagesWithPostId(Connection connection, int postId, List<byte[]> images) throws SQLException, IOException {
        for (byte[] imageBytes : images) {
            // 서버에 저장할 이미지 경로 생성
            String imagePath = SERVER_IMAGE_DIRECTORY + "post_" + postId + "_" + System.currentTimeMillis() + ".jpg";

            // 서버 경로에 이미지 파일 저장
            File imageFile = new File(imagePath);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(imageBytes);
            }

            // DB에 이미지 경로와 post_id 저장
            String query = "INSERT INTO images (post_id, image_url, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, postId);
                pstmt.setString(2, imagePath);
                pstmt.executeUpdate();
            }
        }
    }
}


