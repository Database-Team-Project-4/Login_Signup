package twitter.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class imgService {

    // 이미지를 DB에 BLOB으로 저장
    public void saveImagesWithPostId(Connection connection, int postId, List<byte[]> images) throws SQLException {
        String query = "INSERT INTO Images (post_id, image_data) VALUES (?, ?)";

        for (byte[] imageBytes : images) {
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, postId);
                pstmt.setBytes(2, imageBytes); // BLOB 데이터 설정
                pstmt.executeUpdate();
            }
        }
    }


    // DB에서 특정 post_id에 연결된 모든 이미지를 가져오기
    public List<byte[]> retrieveImagesByPostId(Connection connection, int postId) throws SQLException {
        String query = "SELECT image_data FROM Images WHERE post_id = ?";
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
