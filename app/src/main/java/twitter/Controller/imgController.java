package twitter.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class imgController {

    // 이미지를 DB에 BLOB으로 저장
    public void saveImagesWithPostId(Connection connection, int postId, List<byte[]> images) throws SQLException {
        String query = "INSERT INTO Images (post_id, image_data) VALUES (?, ?)";

        for (byte[] imageBytes : images) {
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, postId);
                pstmt.setBytes(2, imageBytes); // BLOB 데이터로 이미지 저장
                pstmt.executeUpdate();
            }
        }
    }


    // DB에서 특정 post_id에 연결된 모든 이미지를 가져오기
    public List<byte[]> retrieveImagesByPostId(Connection connection, int postId) throws SQLException {
        String query = "SELECT image_data FROM Images WHERE post_id = ?"; //Images에서 해당하는 post_id의 image_data를 가져옴
        List<byte[]> images = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, postId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    byte[] imageBytes = rs.getBytes("image_data");
                    images.add(imageBytes);
                }
            }
        }
        return images; // 연결된 모든 이미지 데이터를 반환
    }
}