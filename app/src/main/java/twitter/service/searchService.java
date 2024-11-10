package twitter.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class searchService {

    // 기본 통합 검색 (최신 순)
    public boolean defaultSearch(Connection con, String keyword) {
        String query = "SELECT user, content, tags FROM post WHERE user LIKE ? OR content LIKE ? OR tags LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%"); // 사용자 검색용
            pstmt.setString(2, "%" + keyword + "%"); // 콘텐츠 검색용
            pstmt.setString(3, "%" + keyword + "%"); // 태그 검색용
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- Top ---");
                hasResults = true;
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
        return hasResults;
    }

    // People 기준 검색
    public void peopleSearch(Connection con, String keyword) {
        String query = "SELECT user, content FROM post WHERE user LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- People ---");
                hasResults = true;
            }

            if (!hasResults) {
                printNoResultsMessage(keyword);
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
    }

    // Media 기준 검색
    public void mediaSearch(Connection con, String keyword) {
        String query = "SELECT user, content FROM post WHERE content LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- Media ---");
                hasResults = true;
            }

            if (!hasResults) {
                printNoResultsMessage(keyword);
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
    }

    // 태그로 검색
    public void searchByTag(Connection con, String keyword) {
        String query = "SELECT user, content FROM post WHERE tags LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- Tags ---");
                hasResults = true;
            }

            if (!hasResults) {
                printNoResultsMessage(keyword);
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
    }

    // 검색 결과 출력 메서드
    private void printResults(ResultSet results, String header) throws SQLException {
        System.out.println("\n" + header);
        do {
            String user = results.getString("user");
            String content = results.getString("content");
            String tags = results.getString("tags"); // 태그 출력 추가
            System.out.println("User: " + user + ", Content: " + content + ", Tags: " + tags);
        } while (results.next());
    }

    // 검색 결과 없을 때 메시지 출력 메서드
    private void printNoResultsMessage(String keyword) {
        System.out.println("No results for \"" + keyword + "\"");
        System.out.println("Try searching for something else, or check your Search settings to see if they’re protecting you from potentially sensitive content.");
    }
}
