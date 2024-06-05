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
}
