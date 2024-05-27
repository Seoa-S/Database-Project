package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasketController {


    public static void showBasketList(int id) {
        String basketlist = "SELECT name, price, category From Mealkit, Basket WHERE Basket.mealkit_id=Mealkit.mealkit_id AND Basket.member_id = ?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(basketlist);
        ){

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                System.out.println("==================장바구니 목록=====================");
                System.out.println("상품명:\t\t\t\t가격:\t\t\t카테고리:");
                // 결과를 출력합니다.
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    String category = resultSet.getString("category");

                    System.out.printf("%s\t\t\t%d\t\t\t%s\n", name, price, category);
                }
                System.out.println("=================================================");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
