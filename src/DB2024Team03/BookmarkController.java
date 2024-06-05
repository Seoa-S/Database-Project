package DB2024Team03;

import java.sql.*;

public class BookmarkController {
    public static void showBookmarkList(int member_id){
        String bookmarkquery = "SELECT production_name, mealkit_id FROM DB2024_Bookmarklist_view WHERE member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(bookmarkquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[상품 ID]\t[상품이름]");
            while (rs.next()) {
                int mealkit_id = rs.getInt("mealkit_id");
                String production_name = rs.getString("production_name");

                System.out.printf("%d\t\t\t%s%n", mealkit_id, production_name);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBookmarkItem(int id, int MealkitId){
        String deleteItem = "DELETE FROM DB2024_Bookmark WHERE member_id=? AND mealkit_id=?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteItem);
        ){

            statement.setInt(1, id);
            statement.setInt(2, MealkitId);

            try {
                if (UtilController.checkIdExist(MealkitId, id, "DB2024_Bookmark" )) {
                    statement.executeUpdate();

                    System.out.print("상품이 제거되었습니다.\n");
                    return;
                }

                else {
                    System.out.print("해당 상품ID가 북마크 목록에 존재하지 않습니다..\n");
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}