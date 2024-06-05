package DB2024Team03;

import java.sql.*;

public class BookmarkController {
    public static void showBookmarkList(int member_id){
        String bookmarkquery = "SELECT production_name, mealkit_id FROM DB2024_Bookmarklist_view WHERE member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(bookmarkquery)) {

            statement.setInt(1, member_id);

            if(UtilController.checkItemNum(member_id, "DB2024_Bookmark", conn) > 0){
                ResultSet rs = statement.executeQuery();

                System.out.println("[상품 ID]\t[상품이름]");
                while (rs.next()) {
                    int mealkit_id = rs.getInt("mealkit_id");
                    String production_name = rs.getString("production_name");

                    System.out.printf("%d\t\t\t%s%n", mealkit_id, production_name);
                }
            }else
                System.out.println("북마크된 상품이 없습니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBookmarkItem(int id, int mealkitId) {
        String deleteItem = "DELETE FROM DB2024_Bookmark WHERE member_id=? AND mealkit_id=?";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = DBconnect.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            statement = conn.prepareStatement(deleteItem);
            statement.setInt(1, id);
            statement.setInt(2, mealkitId);

            try {
                if (UtilController.checkIdExist(mealkitId, id, "DB2024_Bookmark", conn)) {
                    statement.executeUpdate();
                    System.out.print("상품이 제거되었습니다.\n");
                    conn.commit(); // 변경사항 커밋
                } else {
                    System.out.print("해당 상품ID가 북마크 목록에 존재하지 않습니다..\n");
                    conn.commit(); // 트랜잭션 완료 (실제로 삭제할 항목이 없는 경우에도)
                }
            } catch (SQLException e) {
                conn.rollback(); // 예외 발생 시 롤백
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true); // 자동 커밋 모드 재설정
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        conn.close(); // 연결 닫기
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close(); // PreparedStatement 닫기
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 연결을 재사용하는 checkIdExist 메서드
    public static boolean checkIdExist(int itemId, int memberId, String tableName, Connection conn) {
        String checkIdExist = "SELECT mealkit_id FROM " + tableName + " WHERE member_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(checkIdExist)) {
            statement.setInt(1, memberId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int mealkit_id = rs.getInt("mealkit_id");
                    if (mealkit_id == itemId) {
                        return true;
                    }
                }
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}