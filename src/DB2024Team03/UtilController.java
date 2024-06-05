package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilController {
    public static int checkBasket(int id){
        String checkBasket = "SELECT COUNT(*) FROM DB2024_BASKET WHERE member_id=?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(checkBasket);
        ){

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                // 장바구니 아이템 개수 받아오기
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

