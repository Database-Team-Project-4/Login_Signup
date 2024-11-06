import java.sql.*;
import java.util.Scanner;

public class Search {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", "root", "chlworud7104")) {

                System.out.print("SEARCH: ");
                String keyword = scanner.nextLine(); // 모든 검색 기준에 사용할 검색어 입력

                // 통합 검색 
                boolean hasResults = defaultSearch(con, keyword);

                if (!hasResults) { // 결과 X
                    System.out.println("No results for \"" + keyword + "\"");
                    System.out.println("Try searching for something else, or check your Search settings to see if they’re protecting you from potentially sensitive content.");
                } else { // 통합 검색 결과 존재 시 검색 기준 결정     
                    boolean continueSearching = true;
                    while (continueSearching) {
                        System.out.print("\nChoose filter: (1) Latest, (2) People, (3) Media, (4) Tags, (5) Exit: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1 -> defaultSearch(con, keyword); // 통합 검색 결과 출력(최신순)
                            case 2 -> peopleSearch(con, keyword); // People 기준 검색 결과 출력
                            case 3 -> mediaSearch(con, keyword);  // Media 기준 검색 결과 출력
                            case 4 -> searchByTag(con, keyword); // 태그 검색 결과 출력
                            case 5 -> continueSearching = false; // 종료
                            default -> System.out.println("Please choose in 1~5");
                        }
                    }
                }
            } catch (SQLException e) { // 쿼리 연결 문제 시
                System.out.println("!QUERY ERROR!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    // 기본 통합 검색 (최신 순)
    public static boolean defaultSearch(Connection con, String keyword) {
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
    public static void peopleSearch(Connection con, String keyword) {
        String query = "SELECT user, content FROM post WHERE user LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- People ---");
                hasResults = true;
            }

            // 결과 X
            if (!hasResults) {
                System.out.println("No results for \"" + keyword + "\"");
                System.out.println("Try searching for something else, or check your Search settings to see if they’re protecting you from potentially sensitive content.");
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
    }

    // Media 기준 검색
    public static void mediaSearch(Connection con, String keyword) {
        String query = "SELECT user, content FROM post WHERE content LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- Media ---");
                hasResults = true;
            }

            // 결과 X
            if (!hasResults) {
                System.out.println("No results for \"" + keyword + "\"");
                System.out.println("Try searching for something else, or check your Search settings to see if they’re protecting you from potentially sensitive content.");
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
    }

    // 태그로 검색
    public static void searchByTag(Connection con, String keyword) {
        String query = "SELECT user, content FROM post WHERE tags LIKE ?";
        boolean hasResults = false;

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%"); // 태그에 포함된 게시물만 검색
            ResultSet results = pstmt.executeQuery();

            if (results.next()) {
                printResults(results, "--- Tags ---");
                hasResults = true;
            }

            // 결과 X
            if (!hasResults) {
                System.out.println("No results for \"" + keyword + "\"");
                System.out.println("Try searching for something else, or check your Search settings to see if they’re protecting you from potentially sensitive content.");
            }
        } catch (SQLException e) {
            System.out.println("!QUERY ERROR!");
        }
    }

    // 검색 결과 출력 
    public static void printResults(ResultSet results, String header) throws SQLException {
        System.out.println("\n" + header);
        do {
            String user = results.getString("user");
            String content = results.getString("content");
            String tags = results.getString("tags"); // 태그 출력 추가
            System.out.println("User: " + user + ", Content: " + content + ", Tags: " + tags);
        } while (results.next());
    }
}
