package DB2024Team03;

import java.sql.*;

public class BookmarkController {

    //사용자별로 자신의 북마크 목록을 보여주는 함수
    public static void showBookmarkList(int member_id){
        // member_id에 해당하는 상품명과 밀키트 Id를 보여주는 SQL 쿼리
        String bookmarkquery = "SELECT production_name, mealkit_id FROM DB2024_Bookmarklist_view WHERE member_id = ?";
        try (
                Connection conn = DBconnect.getConnection(); //데이터베이스 연결
                //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성
                PreparedStatement statement = conn.prepareStatement(bookmarkquery)) {

            statement.setInt(1, member_id); //피라미터로 가져온 member_id를 sql쿼리문에 넣기

            //DB2024_Bookmark에서 사용자의 데이터 개수가 존재하는 경우
            if(UtilController.checkItemNum(member_id, "DB2024_Bookmark", conn) > 0){ // 해당 사용자의 북마크 항목 수를 확인해 북마크 항목이 존재하는 경우에만 결과를 조회한다.
                ResultSet rs = statement.executeQuery(); // 쿼리 실행 및 결과 집합 받기

                // 결과의 헤더를 출력한다.
                System.out.println("[상품 ID]\t[상품이름]");
                // 결과 집합을 순회하면서 각 행의 밀키트 ID와 상품 이름을 출력한다.
                while (rs.next()) {
                    int mealkit_id = rs.getInt("mealkit_id"); //mealkit_id 가져오기
                    String production_name = rs.getString("production_name"); //production_name 가져오기

                    System.out.printf("%d\t\t\t%s%n", mealkit_id, production_name); //가져온 정보들을 목록형식으로 보여주기
                }

            }else //DB2024_Bookmark에서 사용자의 데이터 개수가 존재하지 않는 경우
                System.out.println("북마크된 상품이 없습니다."); //북마크된 상품이 없습니다. 출력하기
        } catch (SQLException e) { //예외처리
            throw new RuntimeException(e);
        }
    }

    //북마크 목록에서 원하는 상품을 제거하는 함수

    public static void deleteBookmarkItem(int id, int mealkitId) {
        String deleteItem = "DELETE FROM DB2024_Bookmark WHERE member_id=? AND mealkit_id=?";

        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = DBconnect.getConnection(); //데이터베이스 연결
            conn.setAutoCommit(false); // 트랜잭션 시작

            statement = conn.prepareStatement(deleteItem); //SQL 문장을 실행하기 위해 PreparedStatement 객체 생성
            statement.setInt(1, id); //피라미터로 가져온 id(삭제하려는 상품 ID)를 SQL문에 넣기
            statement.setInt(2, mealkitId); //피라미터로 가져온 mealkitId를 SQL문에 넣기

            try {
                //자신의 북마크 목록에 삭제하려고 입력한 상품이 있는 경우
                if (UtilController.checkIdExist(mealkitId, id, "DB2024_Bookmark", conn)) {
                    statement.executeUpdate(); //SQL 쿼리문 실행 (북마크 목록에서 해당 상품 삭제)
                    System.out.print("상품이 제거되었습니다.\n"); //안내문 출력
                    conn.commit(); // 변경사항 커밋
                } else { //자신의 북마크 목록에 삭제하려고 입력한 상품이 없는 경우
                    System.out.print("해당 상품ID가 북마크 목록에 존재하지 않습니다..\n"); //안내문 출력
                    conn.commit(); // 트랜잭션 완료 (실제로 삭제할 항목이 없는 경우에도)
                }
            } catch (SQLException e) {
                conn.rollback(); // 예외 발생 시 롤백
                throw new RuntimeException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true); // 자동 커밋 모드 재설정
                    } catch (SQLException e) { //예외처리
                        throw new RuntimeException(e);
                    }
                    try {
                        conn.close(); // 연결 닫기
                    } catch (SQLException e) { //예외처리
                        throw new RuntimeException(e);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close(); // PreparedStatement 닫기
                    } catch (SQLException e) { //예외처리
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) { //예외처리
            throw new RuntimeException(e);
        }
    }

}