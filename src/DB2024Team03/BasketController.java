package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BasketController {

    public static void showBasketList(int id) {
        String basketlist = "SELECT basket_id, name, price, category From DB2024_Mealkit, DB2024_Basket WHERE DB2024_Basket.mealkit_id=DB2024_Mealkit.mealkit_id AND DB2024_Basket.member_id = ?";


        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(basketlist);
        ){

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                System.out.println("==================장바구니 목록=====================");
                System.out.println("상품 번호:\t\t상품명:\t\t\t\t가격:\t\t\t카테고리:");
                // 결과를 출력합니다.
                while (resultSet.next()) {
                    int basketId = resultSet.getInt("basket_id");
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    String category = resultSet.getString("category");

                    System.out.printf("%d\t\t\t%s\t\t\t%d\t\t\t%s\n", basketId, name, price, category);
                }
                System.out.println("=================================================");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBasketItem(int id, int basketId){
        String deleteItem = "DELETE FROM DB2024_Basket WHERE member_id=? AND basket_id=?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteItem);
        ){

            statement.setInt(1, id);
            statement.setInt(2, basketId);

            try {
                statement.executeUpdate();

                //입력된 상품 제거하기
                System.out.print("상품이 제거되었습니다.");
                return;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void orderItems(int id){
        String orderItems = "DELETE FROM DB2024_Basket WHERE member_id=?";

        try (
            Connection conn = DBconnect.getConnection();
            PreparedStatement statement = conn.prepareStatement(orderItems)
        ) {


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
