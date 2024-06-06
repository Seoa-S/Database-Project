package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilController {

    //테이블 안에 있는 상품의 개수 세기
    public static int checkItemNum(int memberId, String DBName, Connection conn){
        String checkBasket = "SELECT COUNT(*) FROM " +DBName+ " WHERE member_id=?";

        try (// DB 연결을 위한 정보를 설정
             PreparedStatement statement = conn.prepareStatement(checkBasket);
        ){

            statement.setInt(1, memberId);


            try(ResultSet resultSet = statement.executeQuery()){
                // 상품 개수 받아오기
                if (resultSet.next()){
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
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

