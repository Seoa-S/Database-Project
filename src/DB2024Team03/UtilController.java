package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilController {

    //테이블 안에 있는 튜플의 개수 세는 함수
    public static int checkItemNum(int memberId, String DBName, Connection conn){ //memberId, 데이터베이스 테이블 이름, connection을 입력받는다.
        String checkItemNum = "SELECT COUNT(*) FROM " +DBName+ " WHERE member_id=?"; // DBName 테이블에서 member_id가 일치하는 행의 개수를 세는 SQL 쿼리를 문자열로 생성

        try (
             PreparedStatement statement = conn.prepareStatement(checkItemNum); // DB와의 연결을 위한 PreparedStatement를 생성
        ){

            statement.setInt(1, memberId); // SQL 쿼리의 첫 번째 매개변수'?'에 memberId를 설정

            try(ResultSet resultSet = statement.executeQuery()){ // SQL 쿼리를 실행하고, 그 결과를 ResultSet 객체로 받아온다.
                // 상품 개수 받아오기
                if (resultSet.next()){ // ResultSet에 다음 행이 존재한다면 즉, 쿼리의 결과가 존재한다면
                    return resultSet.getInt(1); // 첫 번째 열 즉, 여기서는 COUNT(*)의 결과의 값을 int로 받아와서 반환한다.
                }
            }
        } catch (SQLException e) {
            // SQL 관련 예외가 발생하면, 런타임 예외로 전환하여 던진다.
            throw new RuntimeException(e);
        }

        return 0; // SQL 쿼리의 결과가 없거나 예외가 발생했을 때, 0을 반환한다.
    }

    //피라미터로 가져온 itemId가 tableName의 테이블에 존재하는지 확인하여 존재하면 true, 존재하지 않으면 false를 반환하는 함수
    public static boolean checkIdExist(int itemId, int memberId, String tableName, Connection conn) {
        String checkIdExist = "SELECT mealkit_id FROM " + tableName + " WHERE member_id = ?";

        try (PreparedStatement statement = conn.prepareStatement(checkIdExist)) {
            statement.setInt(1, memberId); //피라미터로 가져온 memberId를 sql문에 넣기

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int mealkit_id = rs.getInt("mealkit_id"); //mealkit_id 가져오기
                    if (mealkit_id == itemId) { //가져온 mealkit_id와 입력받은 itemId가 일치하는 경우
                        return true; //true 반환
                    }
                }
                return false; //모든 행에 대해 확인해봤지만 없을 경우 false 반환
            }
        } catch (SQLException e) { //오류
            throw new RuntimeException(e);
        }
    }

}

