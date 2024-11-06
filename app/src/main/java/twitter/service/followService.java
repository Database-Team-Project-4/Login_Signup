package twitter.service;

import java.sql.*;
import java.util.Scanner;
import twitter.User;

public class followService {
    private static Scanner s = new Scanner(System.in);

    public static void follow(Connection conn, User currentuser) {
        Statement stmt = null;
        ResultSet rs = null;
        int u_id = currentuser.getId();

        try {
            stmt = conn.createStatement();
            System.out.println("Input user ID to follow:");
            int followid = s.nextInt();

            if (followid == u_id) {
                System.out.println("Can't follow yourself");
            } else {
                String checkQuery = "SELECT follower_id FROM following WHERE follower_id = '" + followid + "' AND user_id = '" + u_id + "'";
                rs = stmt.executeQuery(checkQuery);

                if (rs.next()) {
                    System.out.println("Already following the user. Please try again!");
                } else {
                    String insertQuery = "INSERT INTO following (follower_id, user_id) VALUES ('" + followid + "', '" + u_id + "')";
                    stmt.executeUpdate(insertQuery);
                    System.out.println("Successfully followed user: " + followid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }
    }

    public static void seeFollower(Connection conn, User currentuser) {
        Statement stmt = null;
        ResultSet rs = null;
        int u_id = currentuser.getId();

        try {
            stmt = conn.createStatement();
            String query = "SELECT user_id FROM follow WHERE follower_id = " + u_id;
            rs = stmt.executeQuery(query);

            boolean hasFollowers = false;

            System.out.println("Followers of user " + u_id + ":");
            while (rs.next()) {
                hasFollowers = true;
                int followerId = rs.getInt("user_id");
                System.out.println("Follower ID: " + followerId);
            }
            if (!hasFollowers) {
                System.out.println("No followers found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }
    }

    public static void seeFollowing(Connection conn, User currentuser) {
        Statement stmt = null;
        ResultSet rs = null;
        int u_id = currentuser.getId();

        try {
            stmt = conn.createStatement();
            String query = "SELECT follower_id FROM follow WHERE user_id = " + u_id;
            rs = stmt.executeQuery(query);

            boolean hasFollowings = false;

            System.out.println("Followers of user " + u_id + ":");
            while (rs.next()) {
                hasFollowings = true;
                int followingId = rs.getInt("follower_id");
                System.out.println("Following ID: " + followingId);
            }
            if (!hasFollowings) {
                System.out.println("No follows found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt);
        }
    }

    private static void closeResources(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
