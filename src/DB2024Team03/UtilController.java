package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilController {

    //테이블 안에 있는 상품의 개수 세기
    public static int checkItemNum(int memberId, String DBName){
        String checkBasket = "SELECT COUNT(*) FROM " +DBName+ " WHERE member_id=?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
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

    public static boolean checkIdExist(int itemId, int memberId, String dbName) {
        String checkIdExist = "SELECT mealkit_id FROM " + dbName + " WHERE member_id = ?";

        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(checkIdExist)) {

            statement.setInt(1, memberId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int mealkit_id = rs.getInt("mealkit_id");

                if (mealkit_id == itemId) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

