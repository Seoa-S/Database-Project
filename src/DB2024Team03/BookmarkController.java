package DB2024Team03;

import java.sql.*;

public class BookmarkController {
    public static void displayBookmarkList(int member_id){
        String bookmarkquery = "SELECT M.name" +
                " FROM DB2024_Bookmark B INNER JOIN DB2024_Mealkit M ON B.mealkit_id = M.mealkit_id " +
                "WHERE B.member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(bookmarkquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[상품이름]");
            while (rs.next()) {
                String name = rs.getString("name");

                System.out.printf("%s%n", name);
                System.out.println("---------------------------------------------");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}