package DB2024Team03;

import java.sql.*;

public class PdlistDTO{
    public static void displayProductList() {
        String query = "SELECT * FROM DB2024_Mealkit";
        try (Connection conn = DBconnect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            /*
            System.out.println("==================상품 목록==================");
            while (rs.next()) {
                int id = rs.getInt("mealkit_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                String category = rs.getString("category");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Price: " + price);
                System.out.println("Category: " + category);
                System.out.println("---------------------------------------------");
                */
            System.out.println("==================상품 목록==================");
            System.out.println("\n상품번호:\t\t상품이름:\t\t\t가격:\t\t카테고리:");
            while (rs.next()) {
                int id = rs.getInt("mealkit_id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                String category = rs.getString("category");

                System.out.printf("%d\t\t\t%s\t\t%d\t\t%s%n", id, name, price, category);
                System.out.println("---------------------------------------------");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
