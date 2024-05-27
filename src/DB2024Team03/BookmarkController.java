package DB2024Team03;

import java.sql.*;

public class BookmarkController {
    public static void displayBookmarkList(int member_id){
        String bookmarkquery = "SELECT production_name FROM bookmarklist_view WHERE member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(bookmarkquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[상품이름]");
            while (rs.next()) {
                String production_name = rs.getString("production_name");

                System.out.printf("%s%n", production_name);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}