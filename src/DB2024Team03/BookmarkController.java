package DB2024Team03;

import java.sql.*;

public class BookmarkController {
    public static void displayBookmarkList(int member_id){
        String bookmarkquery = "SELECT production_name, bookmark_id FROM bookmarklist_view WHERE member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(bookmarkquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[상품 ID]\t[상품이름]");
            while (rs.next()) {
                int bookmark_id = rs.getInt("bookmark_id");
                String production_name = rs.getString("production_name");

                System.out.printf("%d\t\t\t%s%n", bookmark_id, production_name);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBookmarkItem(int id, int BookmarkId){
        String deleteItem = "DELETE FROM DB2024_Bookmark WHERE member_id=? AND bookmark_id=?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteItem);
        ){

            statement.setInt(1, id);
            statement.setInt(2, BookmarkId);

            try {
                statement.executeUpdate();

                System.out.print("상품이 제거되었습니다.\n");
                return;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}