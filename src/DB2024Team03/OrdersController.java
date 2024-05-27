package DB2024Team03;

import java.sql.*;

public class OrdersController {
    public static void displayOrdersList(int member_id) throws SQLException {
        String ordersquery = "SELECT M.name, M.orderdate" +
                " FROM DB2024_Orders O INNER JOIN DB2024_Mealkit M ON O.mealkit_id = M.mealkit_id " +
                "WHERE O.member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(ordersquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[주문내역]");
            System.out.println("상품이름:\t\t\t주문날짜:");
            while (rs.next()) {
                String name = rs.getString("name");
                Date orderdate = rs.getDate("orderdate");

                System.out.printf("%s\t\t\t%s%n", name, orderdate);
                System.out.println("---------------------------------------------");


            }catch(SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}
