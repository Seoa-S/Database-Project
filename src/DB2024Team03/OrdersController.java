package DB2024Team03;

import java.sql.*;
import java.util.Scanner;

public class OrdersController {
    public static void displayOrdersList(int member_id, Scanner sc) {
        String ordersquery = "SELECT M.name, O.orderdate, M.price, O.mealkit_id " +
                "FROM DB2024_Orders O INNER JOIN DB2024_Mealkit M ON O.mealkit_id = M.mealkit_id " +
                "WHERE O.member_id = ?";
        try (
                Connection conn = DBconnect.getConnection();
                PreparedStatement statement = conn.prepareStatement(ordersquery)) {

            statement.setInt(1, member_id);

            ResultSet rs = statement.executeQuery();

            System.out.println("[주문내역]");
            System.out.println("[상품 ID]\t[상품 이름]\t\t[가격]\t[주문 날짜]");
            while (rs.next()) {
                System.out.printf("%d\t\t\t%s\t\t%d\t%s%n",
                        rs.getInt("mealkit_id"),
                        rs.getString("name"),
                        rs.getInt("price"),  // Assuming you want to display the price
                        rs.getDate("orderdate"));
            }

            System.out.print("[1]리뷰하기 [2]마이페이지로 돌아가기 >> ");
            int action = sc.nextInt();
            if (action == 1) {
                System.out.print("리뷰할 상품 ID 입력 >> ");
                int productId = sc.nextInt();
                sc.nextLine();
                ReviewController reviewController = new ReviewController();
                reviewController.displayReview(productId);
                System.out.println("리뷰를 작성하세요 (엔터를 누르면 작성 완료): ");
                String reviewContent = sc.nextLine();
                reviewController.createReview(member_id, productId, reviewContent, sc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
