package DB2024Team03;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasketController {

    public static void showBasketList(int id) {
        String basketlist = "SELECT DB2024_Mealkit.mealkit_id, name, price, category " +
                "From DB2024_Mealkit, DB2024_Basket " +
                "WHERE DB2024_Basket.mealkit_id=DB2024_Mealkit.mealkit_id " +
                "AND DB2024_Basket.member_id = ?";


        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(basketlist);
        ){

            statement.setInt(1, id);

            try(ResultSet resultSet = statement.executeQuery()){
                System.out.println("==================장바구니 목록=====================");
                System.out.println("[상품 번호]\t[상품명]\t\t\t[가격]\t\t[카테고리]");
                // 결과 출력
                while (resultSet.next()) {
                    int mealkitId = resultSet.getInt("mealkit_id");
                    String name = resultSet.getString("name");
                    int price = resultSet.getInt("price");
                    String category = resultSet.getString("category");

                    System.out.printf("%d\t\t\t%s\t\t%d\t\t%s\n", mealkitId, name, price, category);
                }
                System.out.println("=================================================");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBasketItem(int id, int mealkitId){
        String deleteItem = "DELETE FROM DB2024_Basket WHERE member_id=? AND mealkit_id=?";

        try (// DB 연결을 위한 정보를 설정
             Connection conn = DBconnect.getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteItem);
        ){


            statement.setInt(1, id);
            statement.setInt(2, mealkitId);

            try {
                if (UtilController.checkIdExist(mealkitId, id, "DB2024_Bookmark")) {
                    statement.executeUpdate();
                    System.out.print("상품이 제거되었습니다.\n");
                    return;
                }

                else {
                    System.out.print("해당 상품ID가 장바구니에 존재하지 않습니다.\n");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void updateOrderList(int id, Connection conn){
        String updateOrderList = "INSERT INTO DB2024_Orders (mealkit_id, member_id, orderdate) " +
                "SELECT B.mealkit_id, B.member_id, CURRENT_DATE() AS orderdate " +
                "FROM DB2024_Basket B " +
                "JOIN DB2024_Mealkit M ON B.mealkit_id = M.mealkit_id " +
                "WHERE B.member_id = ? AND M.stock > 0 ";

        try (
                //Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(updateOrderList)
        ) {
            statement.setInt(1, id);

            try{

                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void removeItems(int id, Connection conn){
        String removeItems = "DELETE FROM DB2024_Basket " +
                "WHERE member_id = ? AND mealkit_id IN (" +
                "    SELECT mealkit_id" +
                "    FROM DB2024_Mealkit" +
                "    WHERE stock > 0)";

        try (
            //Connection conn = DBconnect.getConnection();
            PreparedStatement statement = conn.prepareStatement(removeItems)
        ) {
            statement.setInt(1, id);

            try{

                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public static void stockUpdate(int id, Connection conn){
        String stockUpdate = "UPDATE DB2024_Mealkit M " +
                "INNER JOIN DB2024_Basket B ON M.mealkit_id = B.mealkit_id " +
                "SET M.stock = M.stock - 1 " +
                "WHERE B.member_id = ? AND stock >= 0";

        try (
                //Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(stockUpdate)
        ) {
            statement.setInt(1, id);

            try{

                statement.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
